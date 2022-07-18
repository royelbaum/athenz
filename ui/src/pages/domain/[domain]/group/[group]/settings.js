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
import Header from '../../../../../components/header/Header';
import UserDomains from '../../../../../components/domain/UserDomains';
import API from '../../../../../api';
import styled from '@emotion/styled';
import Head from 'next/head';

import CollectionDetails from '../../../../../components/header/CollectionDetails';
import RequestUtils from '../../../../../components/utils/RequestUtils';
import NameHeader from '../../../../../components/header/NameHeader';
import Error from '../../../../_error';
import GroupTabs from '../../../../../components/header/GroupTabs';
import { getDomainData } from '../../../../../redux/thunks/domain';
import { getGroup } from '../../../../../redux/thunks/groups';
import { connect } from 'react-redux';
import { selectIsLoading } from '../../../../../redux/selectors';
import { selectGroup } from '../../../../../redux/selectors/group';
import {
    selectAuthorityAttributes,
    selectDomainAuditEnabled,
    selectDomainData,
} from '../../../../../redux/selectors/domainData';
import SettingTable from '../../../../../components/settings/SettingTable';

const AppContainerDiv = styled.div`
    align-items: stretch;
    flex-flow: row nowrap;
    height: 100%;
    display: flex;
    justify-content: flex-start;
`;

const MainContentDiv = styled.div`
    flex: 1 1 calc(100vh - 60px);
    overflow: hidden;
    font: 300 14px HelveticaNeue-Reg, Helvetica, Arial, sans-serif;
`;

const RolesContainerDiv = styled.div`
    align-items: stretch;
    flex: 1 1;
    height: calc(100vh - 60px);
    overflow: auto;
    display: flex;
    flex-direction: column;
`;

const RolesContentDiv = styled.div``;

const PageHeaderDiv = styled.div`
    background: linear-gradient(to top, #f2f2f2, #fff);
    padding: 20px 30px 0;
`;

export async function getServerSideProps(context) {
    let api = API(context.req);
    let reload = false;
    let notFound = false;
    let error = null;
    const groups = await Promise.all([api.getForm()]).catch((err) => {
        let response = RequestUtils.errorCheckHelper(err);
        reload = response.reload;
        error = response.error;
        return [{}, {}, {}, {}, {}, {}, {}];
    });
    return {
        props: {
            reload,
            notFound,
            error,
            groupName: context.query.group,
            domainName: context.query.domain,
            _csrf: groups[0],
            nonce: context.req.headers.rid,
        },
    };
}

class GroupSettingsPage extends React.Component {
    constructor(props) {
        super(props);
        this.api = API();
    }
    componentDidMount() {
        const { domainName, userName, getDomainData, groupName, getGroup } =
            this.props;
        getDomainData(domainName, userName);
        getGroup(domainName, groupName);
    }
    render() {
        const {
            domainName,
            reload,
            groupDetails,
            groupName,
            domainData,
            _csrf,
            isLoading,
        } = this.props;
        if (reload) {
            window.location.reload();
            return <div />;
        }
        if (this.props.error) {
            return <Error err={this.props.error} />;
        }
        return isLoading.length !== 0 ? (
            <h1>Loading...</h1>
        ) : (
            <div data-testid='group-settings'>
                <Head>
                    <title>Athenz</title>
                </Head>
                <Header showSearch={true} />
                <MainContentDiv>
                    <AppContainerDiv>
                        <RolesContainerDiv>
                            <RolesContentDiv>
                                <PageHeaderDiv>
                                    <NameHeader
                                        category={'group'}
                                        domain={domainName}
                                        collection={groupName}
                                        collectionDetails={
                                            groupDetails ? groupDetails : {}
                                        }
                                    />
                                    <CollectionDetails
                                        collectionDetails={
                                            groupDetails ? groupDetails : {}
                                        }
                                        _csrf={_csrf}
                                    />
                                    <GroupTabs
                                        domain={domainName}
                                        group={groupName}
                                        selectedName={'settings'}
                                    />
                                </PageHeaderDiv>
                                <SettingTable
                                    domain={domainName}
                                    collection={groupName}
                                    collectionDetails={
                                        groupDetails ? groupDetails : {}
                                    }
                                    _csrf={_csrf}
                                    justificationRequired={
                                        this.props.auditEnabled
                                    }
                                    category={'group'}
                                    userAuthorityAttributes={
                                        this.props.userAuthorityAttributes
                                    }
                                />
                            </RolesContentDiv>
                        </RolesContainerDiv>
                        <UserDomains domain={domainName} />
                    </AppContainerDiv>
                </MainContentDiv>
            </div>
        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        ...props,
        isLoading: selectIsLoading(state),
        domainData: selectDomainData(state),
        auditEnabled: selectDomainAuditEnabled(state),
        groupDetails: selectGroup(state, props.domainName, props.groupName),
        userAuthorityAttributes: selectAuthorityAttributes(state),
    };
};

const mapDispatchToProps = (dispatch) => ({
    getDomainData: (domainName, userName) =>
        dispatch(getDomainData(domainName, userName)),
    getGroup: (domainName, groupName) =>
        dispatch(getGroup(domainName, groupName)),
});

export default connect(mapStateToProps, mapDispatchToProps)(GroupSettingsPage);
