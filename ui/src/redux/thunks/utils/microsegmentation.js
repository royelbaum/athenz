import API from '../../../api';
import {
    deleteInboundFromStore,
    deleteOutboundFromStore,
} from '../../actions/microsegmentation';
import { deleteAssertionPolicyVersionFromStore } from '../../actions/policies';
import apiUtils from '../../../server/utils/apiUtils';
import {
    selectOnlyActivePolicies,
    selectPolicy,
} from '../../selectors/policies';
import { getFullName, mapToList } from '../../utils';
import { selectRoleMembers } from '../../selectors/roles';
import { addRole } from '../roles';
import {
    addAssertion,
    addAssertionConditions,
    deleteAssertion,
    deleteAssertionConditions,
    getPolicy,
} from '../policies';
import { deleteRoleFromStore } from '../../actions/roles';
import { policyDelimiter, roleDelimiter } from '../../config';
import { getPolicyFullName } from './policies';

const api = API();

export const deleteTransportRuleApiCall = async (
    domain,
    deletePolicyName,
    deletePolicyVersion,
    assertionIdx,
    deleteRoleName,
    auditRef,
    _csrf,
    dispatch
) => {
    try {
        await api.deleteTransportRule(
            domain,
            deletePolicyName,
            assertionIdx,
            deleteRoleName,
            auditRef,
            _csrf
        );
        dispatch(
            deleteAssertionPolicyVersionFromStore(
                getPolicyFullName(domain, deletePolicyName),
                deletePolicyVersion,
                assertionIdx
            )
        );
        dispatch(
            deleteRoleFromStore(
                getFullName(domain, roleDelimiter, deleteRoleName)
            )
        );
        switch (getCategoryFromPolicyName(deletePolicyName)) {
            case 'inbound':
                dispatch(deleteInboundFromStore(assertionIdx));
                break;
            case 'outbound':
                dispatch(deleteOutboundFromStore(assertionIdx));
                break;
            default:
        }
    } catch (e) {
        throw e;
    }
};

const getCategoryFromPolicyName = (policyName) => {
    return policyName.slice(policyName.lastIndexOf('.') + 1);
};

export const buildInboundOutbound = async (domainName, state) => {
    let jsonData = {
        inbound: [],
        outbound: [],
    };
    const policies = selectOnlyActivePolicies(state);
    if (Array.isArray(policies)) {
        policies.forEach((item, index) => {
            if (item.name.startsWith(domainName + policyDelimiter + 'acl.')) {
                let temp = item.name.split('.');
                //sample policy name - ACL.<service-name>.[inbound/outbound]
                let serviceName = temp[temp.length - 2];
                let category = '';
                const assertionsList = mapToList(item.assertions);
                assertionsList.forEach((assertionItem, assertionIdx) => {
                    if (
                        !apiUtils
                            .getMicrosegmentationActionRegex()
                            .test(assertionItem.action)
                    ) {
                        return;
                    }
                    let tempData = {};
                    let tempProtocol = assertionItem.action.split('-');
                    tempData['layer'] = apiUtils.omitUndefined(tempProtocol[0]);
                    let tempPort = assertionItem.action.split(':');
                    tempData['source_port'] = apiUtils.omitUndefined(
                        tempPort[1]
                    );
                    tempData['destination_port'] = apiUtils.omitUndefined(
                        tempPort[2]
                    );
                    if (assertionItem.conditions) {
                        tempData['conditionsList'] = [];

                        assertionItem.conditions['conditionsList'].forEach(
                            (condition) => {
                                let tempCondition = {};
                                Object.keys(condition['conditionsMap']).forEach(
                                    (key) => {
                                        tempCondition[key] =
                                            condition['conditionsMap'][key][
                                                'value'
                                            ];
                                    }
                                );
                                tempCondition['id'] = condition['id'];
                                tempCondition['assertionId'] =
                                    assertionItem['id'];
                                tempCondition['policyName'] = item.name;
                                tempData['conditionsList'].push(tempCondition);
                            }
                        );
                    }
                    let index = 0;
                    if (item.name.includes('inbound')) {
                        category = 'inbound';
                        tempData['destination_service'] = serviceName;
                        tempData['source_services'] = [];
                        tempData['assertionIdx'] = assertionItem.id;
                        jsonData['inbound'].push(tempData);
                        index = jsonData['inbound'].length;
                    } else if (item.name.includes('outbound')) {
                        category = 'outbound';
                        tempData['source_service'] = serviceName;
                        tempData['destination_services'] = [];
                        tempData['assertionIdx'] = assertionItem.id;
                        jsonData['outbound'].push(tempData);
                        index = jsonData['outbound'].length;
                    }
                    //assertion convention for microsegmentation:
                    //GRANT [Action: <transport layer>-IN / <transport layer>-OUT]:[Source Port]:[Destination Port] [Resource:<service-name>] ON <role-name>
                    // role name will be of the form : <domain>:role.<roleName>
                    let roleName = assertionItem.role.substring(
                        domainName.length + 6
                    );
                    const roleMembers = selectRoleMembers(
                        state,
                        domainName,
                        roleName
                    );
                    if (roleMembers) {
                        roleMembers.forEach((roleMember, idx) => {
                            if (category === 'inbound') {
                                jsonData[category][index - 1][
                                    'source_services'
                                ].push(roleMember.memberName);
                            } else if (category === 'outbound') {
                                jsonData[category][index - 1][
                                    'destination_services'
                                ].push(roleMember.memberName);
                            }
                        });
                    }
                    let substringPrefix = '.' + category + '-';
                    let identifier = roleName.substring(
                        roleName.indexOf(substringPrefix) +
                            substringPrefix.length
                    );
                    jsonData[category][index - 1]['identifier'] = identifier;
                });
            }
        });
    } else {
        // TODO - throw error
    }
    return jsonData;
};

export const editMicrosegmentationHandler = async (
    domainName,
    roleChanged,
    assertionChanged,
    assertionConditionChanged,
    data,
    _csrf,
    dispatch,
    state
) => {
    let roleName = '';
    let policyName = '';
    let resourceName = '';
    let action = '';
    let tempMembers = [];
    let conditionsList = [];
    let auditRef = 'Updated using MicroSegmentation UI';
    if (data['category'] === 'inbound') {
        roleName =
            'acl.' +
            data['destination_service'] +
            '.inbound-' +
            data['identifier'];
        policyName = 'acl.' + data['destination_service'] + '.inbound';
        resourceName = domainName + ':' + data['destination_service'];
        tempMembers = data['source_services'];
        action =
            data['layer'] +
            '-IN:' +
            data['source_port'] +
            ':' +
            data['destination_port'];
    } else {
        roleName =
            'acl.' + data['source_service'] + '.outbound-' + data['identifier'];
        policyName = 'acl.' + data['source_service'] + '.outbound';
        resourceName = domainName + ':' + data['source_service'];
        tempMembers = data['destination_services'];
        action =
            data['layer'] +
            '-OUT:' +
            data['source_port'] +
            ':' +
            data['destination_port'];
    }

    if (assertionChanged || assertionConditionChanged) {
        for (const condition of data['conditionsList']) {
            const { enforcementstate, id, instances } = condition;
            conditionsList.push({ enforcementstate, id, instances });
        }
    }

    if (roleChanged) {
        let role = {
            name: roleName,
            selfServe: false,
            roleMembers: tempMembers.map((member) => {
                return {
                    memberName: member,
                    expiration: '',
                    reviewReminder: '',
                };
            }),
        };
        await dispatch(addRole(roleName, auditRef, role, _csrf));
    }

    if (assertionChanged) {
        let assertion = {
            role: domainName + ':role.' + roleName,
            resource: resourceName,
            effect: 'ALLOW',
            action: action,
            caseSensitive: true,
        };
        await dispatch(getPolicy(domainName, policyName));
        const policy = selectPolicy(state, domainName, policyName);
        let foundAssertionMatch = false;
        policy.assertions.forEach((element) => {
            if (element.action.localeCompare(action) === 0) {
                foundAssertionMatch = true;
            }
        });
        if (foundAssertionMatch) {
            throw {
                status: '500',
                message: {
                    message: 'Policy with the assertion already exists',
                },
            };
        }
        const newAssertion = await dispatch(
            addAssertion(
                domainName,
                policyName,
                assertion.role,
                assertion.resource,
                assertion.action,
                assertion.effect,
                assertion.caseSensitive,
                _csrf
            )
        );
        await dispatch(
            addAssertionConditions(
                domainName,
                policyName,
                newAssertion.id,
                conditionsList,
                auditRef,
                _csrf
            )
        );
        await dispatch(
            deleteAssertion(
                domainName,
                policyName,
                data['assertionIdx'],
                auditRef,
                _csrf
            )
        );
    } else if (assertionConditionChanged) {
        try {
            await dispatch(
                deleteAssertionConditions(
                    domainName,
                    policyName,
                    data['assertionIdx'],
                    auditRef,
                    _csrf
                )
            );
        } catch (err) {
            if (err.status !== 404) {
                throw err;
            }
        }

        await dispatch(
            addAssertionConditions(
                domainName,
                policyName,
                data['assertionIdx'],
                conditionsList,
                auditRef,
                _csrf
            )
        );
    }
};
