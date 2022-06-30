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
import Header from '../../../components/header/Header';
import UserDomains from '../../../components/domain/UserDomains';
import API from '../../../api';
import styled from '@emotion/styled';
import Head from 'next/head';

import DomainDetails from '../../../components/header/DomainDetails';
import Tabs from '../../../components/header/Tabs';
import RequestUtils from '../../../components/utils/RequestUtils';
import Error from '../../_error';
import DomainNameHeader from '../../../components/header/DomainNameHeader';
import {connect} from 'react-redux';
import {getDomainData} from '../../../redux/thunks/domain';
import TagList from '../../../components/tag/TagList';
import {selectDomainData, selectDomainTags,} from '../../../redux/selectors/domainData';
import {selectIsLoading} from '../../../redux/selectors';

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

const TitleDiv = styled.div`
    font: 600 20px HelveticaNeue-Reg, Helvetica, Arial, sans-serif;
    margin-bottom: 10px;
`;

export async function getServerSideProps(context) {
    let api = API(context.req);
    let reload = false;
    let notFound = false;
    let error = null;
    var bServicesParams = {
        category: 'domain',
        attributeName: 'businessService',
        userName: context.req.session.shortId,
    };
    var bServicesParamsAll = {
        category: 'domain',
        attributeName: 'businessService',
    };
    const tagsData = await Promise.all([
        // api.listUserDomains(),
        // api.getHeaderDetails(),
        // api.getDomain(context.query.domain),
        api.getForm(),
        // api.getFeatureFlag(),
        // api.getMeta(bServicesParams),
        api.getMeta(bServicesParamsAll),
        // api.getPendingDomainMembersCountByDomain(context.query.domain),
    ]).catch((err) => {
        let response = RequestUtils.errorCheckHelper(err);
        reload = response.reload;
        error = response.error;
        return [{}, {}];
    });
    // let businessServiceOptions = [];
    // if (tagsData[5] && tagsData[5].validValues) {
    //     tagsData[5].validValues.forEach((businessService) => {
    //         let bServiceOnlyId = businessService.substring(
    //             0,
    //             businessService.indexOf(':')
    //         );
    //         let bServiceOnlyName = businessService.substring(
    //             businessService.indexOf(':') + 1
    //         );
    //         businessServiceOptions.push({
    //             value: bServiceOnlyId,
    //             name: bServiceOnlyName,
    //         });
    //     });
    // }
    let businessServiceOptionsAll = [];
    if (tagsData[1] && tagsData[1].validValues) {
        tagsData[1].validValues.forEach((businessService) => {
            let bServiceOnlyId = businessService.substring(
                0,
                businessService.indexOf(':')
            );
            let bServiceOnlyName = businessService.substring(
                businessService.indexOf(':') + 1
            );
            businessServiceOptionsAll.push({
                value: bServiceOnlyId,
                name: bServiceOnlyName,
            });
        });
    }
    return {
        props: {
            reload,
            notFound,
            error,
            _csrf: tagsData[0],
            domainName: context.query.domain,
            userName: context.req.session.shortId,
            nonce: context.req.headers.rid,
            validBusinessServicesAll: businessServiceOptionsAll,
        },
    };
}

export class TagsPage extends React.Component {
    constructor(props) {
        super(props);
        this.api = API();
    }

    componentWillMount() {
        const {getDomainData, domainName, userName} = this.props;
        getDomainData(domainName, userName);
    }

    render() {
        const {domainName, reload, domainData, domainTags, _csrf, isLoading} =
            this.props;
        if (reload) {
            window.location.reload();
            return <div/>;
        }
        if (this.props.error) {
            return <Error err={this.props.error}/>;
        }

        return isLoading.length !== 0 ? (
            <h1>Loading...</h1>
        ) : (
            <div data-testid='tags'>
                <Head>
                    <title>Athenz Domain Tags</title>
                </Head>
                <Header
                    showSearch={true}
                    headerDetails={domainData.headerDetails}
                    pending={domainData.pendingMembersList}
                />
                <MainContentDiv>
                    <AppContainerDiv>
                        <TagsContainerDiv>
                            <TagsContentDiv>
                                <PageHeaderDiv>
                                    <DomainNameHeader
                                        domainName={domainName}
                                        pendingCount={
                                            domainData.pendingMembersList
                                                ? domainData.pendingMembersList
                                                    .length
                                                : 0
                                        }
                                    />
                                    <DomainDetails
                                        domainDetails={
                                            domainData.domainDetails
                                                ? domainData.domainDetails
                                                : {}
                                        }
                                        api={this.api}
                                        _csrf={_csrf}
                                        productMasterLink={
                                            domainData.headerDetails
                                                ? domainData.headerDetails
                                                    .productMasterLink
                                                : ''
                                        }
                                        validBusinessServices={
                                            domainData.businessData
                                        }
                                        validBusinessServicesAll={
                                            this.props.validBusinessServicesAll
                                        }
                                    />
                                    <Tabs
                                        api={this.api}
                                        domain={domainName}
                                        selectedName={'tags'}
                                        featureFlag={domainData.featureFlag}
                                    />
                                </PageHeaderDiv>
                                <TagList
                                    api={this.api}
                                    domain={domainName}
                                    collectionDetails={domainData}
                                    collectionName={domainName}
                                    tags={domainTags}
                                    category={'domain'}
                                    _csrf={_csrf}
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
        isLoading: selectIsLoading(state),
        domainData: selectDomainData(state),
        domainTags: selectDomainTags(state),
    };
};

const mapDispatchToProps = (dispatch) => ({
    getDomainData: (domainName, userName) =>
        dispatch(getDomainData(domainName, userName)),
});

export default connect(mapStateToProps, mapDispatchToProps)(TagsPage);
