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
import AddModal from '../modal/AddModal';
import AddServiceForm from './AddServiceForm';
import ServiceKeyUtils from '../utils/ServiceKeyUtils';
import RequestUtils from '../utils/RequestUtils';
import {connect} from 'react-redux';
import {addService} from '../../redux/thunks/services';

class AddService extends React.Component {
    constructor(props) {
        super(props);
        this.api = this.props.api;
        this.onSubmit = this.onSubmit.bind(this);
        this.onChange = this.onChange.bind(this);
        this.state = {
            showModal: !!this.props.showAddService,
        };
    }

    onSubmit() {
        if (!this.state.name || this.state.name === '') {
            this.setState({
                errorMessage: 'Service name is required.',
            });
            return;
        }

        let keyValue = this.state.keyValue;

        if (keyValue && keyValue !== '') {
            keyValue = ServiceKeyUtils.y64Encode(
                ServiceKeyUtils.trimKey(keyValue)
            );
        }
        let onSuccess = () => {
            this.setState({showModal: false});
            this.props.onSubmit(`${this.state.name}`, false);
        };

        let onFail = (err) => {
            this.setState({
                errorMessage: RequestUtils.xhrErrorCheckHelper(err),
            });
        };
        let service = {
            description: this.state.description,
            providerEndpoint: this.state.providerEndpoint,
            keyId: this.state.keyId,
            keyValue,
        };

        this.props.addService(
            this.state.name,
            service,
            this.props._csrf,
            onSuccess,
            onFail
        );
    }

    onChange(key, value) {
        this.setState({[key]: value});
    }

    render() {
        return (
            <AddModal
                isOpen={this.state.showModal}
                cancel={this.props.onCancel}
                submit={this.onSubmit}
                title={`Add Service to ${this.props.domain}`}
                errorMessage={this.state.errorMessage}
                sections={
                    <AddServiceForm
                        api={this.api}
                        domain={this.props.domain}
                        onChange={this.onChange}
                        pageConfig={this.props.pageConfig}
                    />
                }
            />
        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        ...props,
        isLoading: state.isLoading,
        services: state.services,
    };
};

const mapDispatchToProps = (dispatch) => ({
    addService: (serviceName, service, _csrf, onSuccess, onFail) =>
        dispatch(addService(serviceName, service, _csrf, onSuccess, onFail)),
});

export default connect(mapStateToProps, mapDispatchToProps)(AddService);
