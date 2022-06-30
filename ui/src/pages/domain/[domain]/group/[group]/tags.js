/*
 * Copyright The Athenz Authors
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
import RequestUtils from '../../../../../components/utils/RequestUtils';
import Error from '../../../../_error';
import NameHeader from '../../../../../components/header/NameHeader';
import CollectionDetails from '../../../../../components/header/CollectionDetails';
import GroupTabs from '../../../../../components/header/GroupTabs';
import {getDomainData} from '../../../../../redux/thunks/domain';
import {getGroup} from '../../../../../redux/thunks/groups';
import {connect} from 'react-redux';
import {selectIsLoading} from '../../../../../redux/selectors';
import {selectGroup, selectGroupTags,} from '../../../../../redux/selectors/group';
import {selectDomainData} from '../../../../../redux/selectors/domainData';
import TagList from '../../../../../components/tag/TagList';

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

const TagsContainerDiv = styled.div`
    align-items: stretch;
    flex: 1 1;
    height: calc(100vh - 60px);
    overflow: auto;
    display: flex;
    flex-direction: column;
`;

const TagsContentDiv = styled.div``;

const PageHeaderDiv = styled.div`
    background: linear-gradient(to top, #f2f2f2, #fff);
    padding: 20px 30px 0;
`;

export async function getServerSideProps(context) {
    let api = API(context.req);
    let reload = false;
    let notFound = false;
    let error = null;
    const tagsData = await Promise.all([api.getForm()]).catch((err) => {
        let response = RequestUtils.errorCheckHelper(err);
        reload = response.reload;
        error = response.error;
        return [{}, {}, {}, {}, {}];
    });

    return {
        props: {
            reload,
            notFound,
            error,
            groupName: context.query.group,
            userName: context.req.session.shortId,
            _csrf: tagsData[0],
            domainName: context.query.domain,
            nonce: context.req.headers.rid,
        },
    };
}

class GroupTagsPage extends React.Component {
    constructor(props) {
        super(props);
        this.api = API();
    }

    componentDidMount() {
        const {domainName, userName, getDomainData, groupName, getGroup} =
            this.props;
        getDomainData(domainName, userName);
        getGroup(domainName, groupName);
    }

    render() {
        const {
            domainName,
            domainData,
            reload,
            groupName,
            groupDetails,
            groupTags,
            _csrf,
            isLoading,
        } = this.props;
        if (reload) {
            window.location.reload();
            return <div/>;
        }
        if (this.props.error) {
            return <Error err={this.props.error}/>;
        }

        return isLoading.length !== 0 ? (
            <h1>Loading... </h1>
        ) : (
            <div data-testid='group-tags'>
                <Head>
                    <title>Athenz Group Tags</title>
                </Head>
                <Header
                    showSearch={true}
                    headerDetails={domainData.headerDetails}
                    pending={this.props.pending}
                />
                <MainContentDiv>
                    <AppContainerDiv>
                        <TagsContainerDiv>
                            <TagsContentDiv>
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
                                        api={this.api}
                                        _csrf={_csrf}
                                        productMasterLink={
                                            domainData.headerDetails
                                                ? domainData.headerDetails
                                                    .productMasterLink
                                                : ''
                                        }
                                    />
                                    <GroupTabs
                                        api={this.api}
                                        domain={domainName}
                                        group={groupName}
                                        selectedName={'tags'}
                                    />
                                </PageHeaderDiv>
                                <TagList
                                    api={this.api}
                                    collectionDetails={groupDetails}
                                    domain={domainName}
                                    collectionName={groupName}
                                    tags={groupTags}
                                    category={'group'}
                                    _csrf={this.props._csrf}
                                />
                            </TagsContentDiv>
                        </TagsContainerDiv>
                        <UserDomains api={this.api} domain={domainName}/>
                    </AppContainerDiv>
                </MainContentDiv>
            </div>
        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        ...props,
        domainData: selectDomainData(state),
        isLoading: selectIsLoading(state),
        groupDetails: selectGroup(state, props.groupName),
        groupTags: selectGroupTags(state, props.groupName),
    };
};

const mapDispatchToProps = (dispatch) => ({
    getDomainData: (domainName, userName) =>
        dispatch(getDomainData(domainName, userName)),
    getGroup: (domainName, groupName) =>
        dispatch(getGroup(domainName, groupName)),
});

export default connect(mapStateToProps, mapDispatchToProps)(GroupTagsPage);
