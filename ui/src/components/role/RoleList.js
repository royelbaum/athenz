/*
 * Copyright 2020 Verizon Media
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import styled from '@emotion/styled';
import {colors} from '../denali/styles';
import Button from '../denali/Button';
import SearchInput from '../denali/SearchInput';
import Alert from '../denali/Alert';
import {MODAL_TIME_OUT} from '../constants/constants';
import ButtonGroup from '../denali/ButtonGroup';
import NameUtils from '../utils/NameUtils';
import {connect} from 'react-redux';
import {addRole} from '../../redux/thunks/roles';
import AddRole from './AddRole';
import RoleTable from './RoleTable';
import UserRoleTable from './UserRoleTable';
import {selectRoles} from '../../redux/selectors/roles';
import {selectDomainAuditEnabled, selectDomainHeaderDetails, selectUserLink,} from '../../redux/selectors/domainData';
import AddMemberToRoles from './AddMemberToRoles';

const RolesSectionDiv = styled.div`
    margin: 20px;
`;

const RoleLabel = styled.label`
    color: ${colors.grey800};
    margin-left: 5px;
    white-space: nowrap;
    font: 300 14px HelveticaNeue-Reg, Helvetica, Arial, sans-serif;
`;

const SliderDiv = styled.div`
    vertical-align: middle;
`;

const StyledSearchInputDiv = styled.div`
    width: 50%;
`;

const AddContainerDiv = styled.div`
    padding-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-flow: row nowrap;
`;

class RoleList extends React.Component {
    constructor(props) {
        super(props);
        this.api = props.api;
        this.state = {
            showuser: false,
            selectedView: 'roles',
            showAddRole: false,
            roles: props.roles || [],
            errorMessage: null,
            searchText: '',
        };
        this.viewRoleByUser = this.viewRoleByUser.bind(this);
        this.toggleAddRole = this.toggleAddRole.bind(this);
        this.toggleAddMemberToRoles = this.toggleAddMemberToRoles.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.reloadRoles = this.reloadRoles.bind(this);
    }

    viewRoleByUser() {
        let selected = this.state.showuser ? 'roles' : 'users';
        this.setState({
            showuser: !this.state.showuser,
            selectedView: selected,
            successMessage: '',
        });
    }

    toggleAddRole() {
        this.setState({
            showAddRole: !this.state.showAddRole,
        });
    }

    toggleAddMemberToRoles() {
        this.setState({
            showAddMemberToRoles: !this.state.showAddMemberToRoles,
        });
    }

    componentDidUpdate = (prevProps) => {
        if (prevProps.domainName !== this.props.domainName) {
            this.setState({
                roles: this.props.roles,
                showuser: false,
                selectedView: 'roles',
                showAddRole: false,
                errorMessage: null,
                searchText: '',
            });
        }
    };

    // TODO roy - need to change it to feet roles Store.
    reloadRoles(successMessage, showSuccess = true) {
        this.setState({
            showAddRole: false,
            showSuccess,
            successMessage,
            errorMessage: null,
        });
        // this is to close the success alert
        setTimeout(
            () =>
                this.setState({
                    showSuccess: false,
                    successMessage: '',
                }),
            MODAL_TIME_OUT
        );
    }

    closeModal() {
        this.setState({showSuccess: null});
    }

    render() {
        let roles = this.props.roles;
        if (this.state.searchText.trim() !== '') {
            roles = this.props.roles.filter((role) => {
                return NameUtils.getShortName(':role.', role.name).includes(
                    this.state.searchText.trim()
                );
            });
        }
        let addRole = this.state.showAddRole ? (
            <AddRole
                api={this.api}
                domain={this.props.domain}
                onSubmit={this.reloadRoles}
                onCancel={this.toggleAddRole}
                _csrf={this.props._csrf}
                showAddRole={this.state.showAddRole}
                justificationRequired={this.props.isDomainAuditEnabled}
                userAuthorityAttributes={this.props.userAuthorityAttributes}
                userProfileLink={this.props.userProfileLink}
            />
        ) : (
            ''
        );
        let addMemberToRoles = this.state.showAddMemberToRoles ? (
            <AddMemberToRoles
                api={this.api}
                domain={this.props.domainName}
                onSubmit={this.reloadRoles}
                onCancel={this.toggleAddMemberToRoles}
                _csrf={this.props._csrf}
                showAddMemberToRoles={this.state.showAddMemberToRoles}
                roles={this.props.roles}
                justificationRequired={this.props.isDomainAuditEnabled}
            />
        ) : (
            ''
        );
        const viewButtons = [
            {id: 'roles', name: 'roles', label: 'Roles'},
            {id: 'users', name: 'users', label: 'Users'},
        ];
        return (
            <RolesSectionDiv data-testid='rolelist'>
                <AddContainerDiv>
                    <SliderDiv>
                        <ButtonGroup
                            buttons={viewButtons}
                            selectedName={this.state.selectedView}
                            onClick={this.viewRoleByUser}
                        />
                    </SliderDiv>
                    <StyledSearchInputDiv>
                        <SearchInput
                            dark={false}
                            name='search'
                            fluid={true}
                            value={this.state.searchText}
                            placeholder={
                                this.state.showuser
                                    ? 'Enter user name'
                                    : 'Enter role name'
                            }
                            error={this.state.error}
                            onChange={(event) =>
                                this.setState({
                                    searchText: event.target.value,
                                    error: false,
                                })
                            }
                        />
                    </StyledSearchInputDiv>
                    <div>
                        <Button secondary onClick={this.toggleAddRole}>
                            Add Role
                        </Button>
                        {addRole}
                        <Button secondary onClick={this.toggleAddMemberToRoles}>
                            Add Member
                        </Button>
                        {addMemberToRoles}
                    </div>
                </AddContainerDiv>
                {this.state.showuser ? (
                    <UserRoleTable
                        users={this.props.users}
                        searchText={this.state.searchText}
                        roles={roles}
                        api={this.api}
                        domain={this.props.domainName}
                        _csrf={this.props._csrf}
                        justificationRequired={this.props.isDomainAuditEnabled}
                        newMember={this.state.successMessage}
                    />
                ) : (
                    <RoleTable
                        api={this.api}
                        domain={this.props.domainName}
                        prefixes={this.props.prefixes}
                        _csrf={this.props._csrf}
                        onSubmit={this.reloadRoles}
                        newRole={this.state.successMessage}
                    />
                )}
                {this.state.showSuccess ? (
                    <Alert
                        isOpen={this.state.showSuccess}
                        title={this.state.successMessage}
                        onClose={this.closeModal}
                        type='success'
                    />
                ) : null}
            </RolesSectionDiv>
        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        ...props,
        roles: selectRoles(state),
        users: selectDomainHeaderDetails(state),
        isDomainAuditEnabled: selectDomainAuditEnabled(state),
        userProfileLink: selectUserLink(state),
        domainName: state.roles.domainName,
    };
};

const mapDispatchToProps = (dispatch) => ({
    addRole: () => dispatch(addRole()),
});

export default connect(mapStateToProps, mapDispatchToProps)(RoleList);
