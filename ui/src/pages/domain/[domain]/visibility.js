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

import styled from '@emotion/styled';
import API from '../../../api';
import RequestUtils from '../../../components/utils/RequestUtils';
import React from 'react';
import createCache from '@emotion/cache';
import Error from '../../_error';
import Head from 'next/head';
import Header from '../../../components/header/Header';
import DomainNameHeader from '../../../components/header/DomainNameHeader';
import DomainDetails from '../../../components/header/DomainDetails';
import Tabs from '../../../components/header/Tabs';
import VisibilityList from '../../../components/visibility/VisibilityList';
import UserDomains from '../../../components/domain/UserDomains';
import { getDomainData } from '../../../redux/thunks/domain';
import { connect } from 'react-redux';
import { getServiceDependencies } from '../../../redux/thunks/visibility';
import { selectServiceDependencies } from '../../../redux/selectors/visibility';
import { selectDomainData } from '../../../redux/selectors/domainData';

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

const VisibilityContainerDiv = styled.div`
    align-items: stretch;
    flex: 1 1;
    height: calc(100vh - 60px);
    overflow: auto;
    display: flex;
    flex-direction: column;
`;

const VisibilityContentDiv = styled.div``;

const PageHeaderDiv = styled.div`
    background: linear-gradient(to top, #f2f2f2, #fff);
    padding: 20px 30px 0;
`;

export async function getServerSideProps(context) {
    let api = API(context.req);
    let reload = false;
    let notFound = false;
    let error = null;

    const domains = await Promise.all([
        api.getForm(),
        api.getRolePrefix(),
    ]).catch((err) => {
        let response = RequestUtils.errorCheckHelper(err);
        reload = response.reload;
        error = response.error;
        return [{}, {}];
    });
    return {
        props: {
            reload,
            notFound,
            error,
            userName: context.req.session.shortId,
            domain: context.query.domain,
            _csrf: domains[0],
            prefixes: domains[1].allPrefixes,
            nonce: context.req.headers.rid,
        },
    };
}

class VisibilityPage extends React.Component {
    constructor(props) {
        super(props);
        this.api = API();
        this.cache = createCache({
            key: 'athenz',
            nonce: this.props.nonce,
        });
    }

    componentDidMount() {
        const { getServiceDependencies, domain, getDomainData, userName } =
            this.props;
        getServiceDependencies(domain);
        getDomainData(domain, userName);
    }

    render() {
        const { domain, reload, domainData, prefixes, isLoading, _csrf } =
            this.props;
        if (reload) {
            window.location.reload();
            return <div />;
        }
        if (this.props.error) {
            return <Error err={this.props.error} />;
        }

        return isLoading.length === 0 ? (
            <div data-testid='visibility'>
                <Head>
                    <title>Athenz</title>
                </Head>
                <Header showSearch={true} />
                <MainContentDiv>
                    <AppContainerDiv>
                        <VisibilityContainerDiv>
                            <VisibilityContentDiv>
                                <PageHeaderDiv>
                                    <DomainNameHeader domainName={domain} />
                                    <DomainDetails
                                        api={this.api}
                                        _csrf={_csrf}
                                    />
                                    <Tabs
                                        domain={domain}
                                        selectedName={'visibility'}
                                    />
                                </PageHeaderDiv>
                                <VisibilityList
                                    key={'dependencyVisibilityList'}
                                    domain={domain}
                                    _csrf={_csrf}
                                    prefixes={prefixes}
                                />
                            </VisibilityContentDiv>
                        </VisibilityContainerDiv>
                        <UserDomains domain={domain} />
                    </AppContainerDiv>
                </MainContentDiv>
            </div>
        ) : (
            <h1>Loading...</h1>
        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        ...props,
        isLoading: state.isLoading,
        domainData: selectDomainData(state),
        serviceDependencies: selectServiceDependencies(state),
    };
};

const mapDispatchToProps = (dispatch) => ({
    getServiceDependencies: (domainName) =>
        dispatch(getServiceDependencies(domainName)),
    getDomainData: (domainName, userName) =>
        dispatch(getDomainData(domainName, userName)),
});

export default connect(mapStateToProps, mapDispatchToProps)(VisibilityPage);
