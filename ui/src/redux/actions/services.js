export const LOAD_SERVICES = 'LOAD_SERVICES';
export const loadServices = (services, domainName, expiry) => ({
    type: LOAD_SERVICES,
    payload: { services: services, domainName: domainName, expiry: expiry },
});

export const RETURN_SERVICES = 'RETURN_SERVICES';
export const returnServices = () => ({
    type: RETURN_SERVICES,
});

export const ADD_SERVICE_TO_STORE = 'ADD_SERVICE_TO_STORE';
export const addServiceToStore = (serviceFullName, serviceData) => ({
    type: ADD_SERVICE_TO_STORE,
    payload: {
        serviceFullName: serviceFullName,
        serviceData: serviceData,
    },
});

export const DELETE_SERVICE_FROM_STORE = 'DELETE_SERVICE_FROM_STORE';
export const deleteServiceFromStore = (serviceFullName) => ({
    type: DELETE_SERVICE_FROM_STORE,
    payload: {
        serviceFullName: serviceFullName,
    },
});

export const DELETE_KEY_FROM_STORE = 'DELETE_KEY_FROM_STORE';
export const deleteKeyFromStore = (serviceFullName, keyId) => ({
    type: DELETE_KEY_FROM_STORE,
    payload: {
        serviceFullName: serviceFullName,
        keyId: keyId,
    },
});
export const ADD_KEY_TO_STORE = 'ADD_KEY_TO_STORE';
export const addKeyToStore = (serviceFullName, keyId, keyValue) => ({
    type: ADD_KEY_TO_STORE,
    payload: {
        serviceFullName,
        keyId,
        keyValue,
    },
});

export const ADD_PROVIDER_TO_STORE = 'ADD_PROVIDER_TO_STORE';
export const addProviderToStore = (
    serviceFullName,
    provider,
    allProviders
) => ({
    type: ADD_PROVIDER_TO_STORE,
    payload: {
        serviceFullName,
        provider,
        allProviders,
    },
});

export const ALLOW_PROVIDER_TEMPLATE_TO_STORE =
    'ALLOW_PROVIDER_TEMPLATE_TO_STORE';
export const allowProviderTemplateToStore = (serviceFullName, providerId) => ({
    type: ALLOW_PROVIDER_TEMPLATE_TO_STORE,
    payload: {
        serviceFullName,
        providerId,
    },
});
