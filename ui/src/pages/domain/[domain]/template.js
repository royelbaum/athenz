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
import Header from '../../../components/header/Header';
import UserDomains from '../../../components/domain/UserDomains';
import API from '../../../api';
import styled from '@emotion/styled';
import Head from 'next/head';

import RequestUtils from '../../../components/utils/RequestUtils';
import Tabs from '../../../components/header/Tabs';
import Error from '../../_error';
import createCache from '@emotion/cache';
import DomainNameHeader from '../../../components/header/DomainNameHeader';
import { getDomainData } from '../../../redux/thunks/domain';
import { connect } from 'react-redux';
import { getTemplates } from '../../../redux/thunks/templates';
import { selectIsLoading } from '../../../redux/selectors';
import { selectDomainData } from '../../../redux/selectors/domainData';
import TemplateList from '../../../components/template/TemplateList';
import DomainDetails from '../../../components/header/DomainDetails';

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

const ServicesContainerDiv = styled.div`
    align-items: stretch;
    flex: 1 1;
    height: calc(100vh - 60px);
    overflow: auto;
    display: flex;
    flex-direction: column;
`;

const ServicesContentDiv = styled.div``;

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
    const domains = await Promise.all([
        api.getForm(),
        api.getServicePageConfig(),
        api.getServerTemplateDetailsList(),
    ]).catch((err) => {
        let response = RequestUtils.errorCheckHelper(err);
        reload = response.reload;
        error = response.error;
        return [{}, {}, {}];
    });
    return {
        props: {
            reload,
            notFound,
            error,
            domainName: context.query.domain,
            userName: context.req.session.shortId,
            _csrf: domains[0],
            pageConfig: domains[1],
            serverTemplateDetails: domains[2],
            nonce: context.req.headers.rid,
        },
    };
}

class TemplatePage extends React.Component {
    constructor(props) {
        super(props);
        this.api = API();
        this.cache = createCache({
            key: 'athenz',
            nonce: this.props.nonce,
        });
    }
    componentDidMount() {
        const {
            // getServices,
            getDomainData,
            getTemplates,
            domainName,
            userName,
        } = this.props;
        getTemplates(domainName);
        getDomainData(domainName, userName);
        // getServices(domainName);
    }

    render() {
        const {
            domainName,
            reload,
            domainData,
            serverTemplateDetails,
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
        return isLoading.length > 0 ? (
            <h1>Loading...</h1>
        ) : (
            <div data-testid='template'>
                <Head>
                    <title>Athenz for Template</title>
                </Head>
                <Header showSearch={true} />
                <MainContentDiv>
                    <AppContainerDiv>
                        <ServicesContainerDiv>
                            <ServicesContentDiv>
                                <PageHeaderDiv>
                                    <DomainNameHeader domainName={domainName} />
                                    <DomainDetails
                                        api={this.api}
                                        _csrf={_csrf}
                                    />
                                    <Tabs
                                        domain={domainName}
                                        selectedName={'templates'}
                                    />
                                </PageHeaderDiv>
                                <TemplateList
                                    api={this.api}
                                    domain={domainName}
                                    serverTemplateDetails={
                                        serverTemplateDetails
                                    }
                                    _csrf={_csrf}
                                    pageConfig={this.props.pageConfig}
                                />
                            </ServicesContentDiv>
                        </ServicesContainerDiv>
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
        store: state,
    };
};

const mapDispatchToProps = (dispatch) => ({
    getDomainData: (domainName, userName) =>
        dispatch(getDomainData(domainName, userName)),
    getTemplates: (domainName) => dispatch(getTemplates(domainName)),
});

export default connect(mapStateToProps, mapDispatchToProps)(TemplatePage);
