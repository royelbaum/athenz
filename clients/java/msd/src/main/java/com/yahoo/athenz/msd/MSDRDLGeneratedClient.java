//
// This file generated by rdl 1.5.2. Do not modify!
//
package com.yahoo.athenz.msd;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import com.yahoo.rdl.Schema;

public class MSDRDLGeneratedClient {

    private static final int DEFAULT_CLIENT_CONNECT_TIMEOUT_MS = 5000;
    private static final int DEFAULT_CLIENT_READ_TIMEOUT_MS = 30000;

    private String baseUrl;
    private String credsHeader;
    private String credsToken;

    private CloseableHttpClient client;
    private HttpContext httpContext;
    private ObjectMapper jsonMapper;

    protected CloseableHttpClient createHttpClient(HostnameVerifier hostnameVerifier) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(DEFAULT_CLIENT_CONNECT_TIMEOUT_MS)
                .setSocketTimeout(DEFAULT_CLIENT_READ_TIMEOUT_MS)
                .setRedirectsEnabled(false)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setSSLHostnameVerifier(hostnameVerifier)
                .build();
    }

    private static class UriTemplateBuilder {
        private final String baseUrl;
        private String basePath;
        public UriTemplateBuilder(final String url, final String path) {
            baseUrl = url;
            basePath = path;
        }
        public UriTemplateBuilder resolveTemplate(final String key, final Object value) {
            basePath = basePath.replace("{" + key + "}", String.valueOf(value));
            return this;
        }
        public String getUri() {
            return baseUrl + basePath;
        }
    }

    public MSDRDLGeneratedClient(final String url) {
        initClient(url, createHttpClient(null));
    }

    public MSDRDLGeneratedClient(final String url, HostnameVerifier hostnameVerifier) {
        initClient(url, createHttpClient(hostnameVerifier));
    }

    public MSDRDLGeneratedClient(final String url, CloseableHttpClient httpClient) {
        initClient(url, httpClient);
    }

    private void initClient(final String url, CloseableHttpClient httpClient) {
        baseUrl = url;
        client = httpClient;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void close() {
        try {
            client.close();
        } catch (IOException ignored) {
        }
    }

    public void addCredentials(final String header, final String token) {

        credsHeader = header;
        credsToken = token;

        if (header == null) {
            httpContext = null;
        } else if (header.startsWith("Cookie.")) {
            httpContext = new BasicHttpContext();
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie(header.substring(7), token);
            cookie.setPath(baseUrl);
            cookieStore.addCookie(cookie);
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
            credsHeader = null;
        }
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        client = httpClient;
    }

    public TransportPolicyRules getTransportPolicyRules(String matchingTag, java.util.Map<String, java.util.List<String>> headers) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/transportpolicies");
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        if (matchingTag != null) {
            httpUriRequest.addHeader("If-None-Match", matchingTag);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (headers != null) {
                    headers.put("tag", List.of(httpResponse.getFirstHeader("ETag").getValue()));
                }
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), TransportPolicyRules.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public TransportPolicyValidationResponse validateTransportPolicy(TransportPolicyValidationRequest transportPolicy) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/transportpolicy/validate");
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpEntity httpEntity = new StringEntity(jsonMapper.writeValueAsString(transportPolicy), ContentType.APPLICATION_JSON);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
            .setUri(uriBuilder.build())
            .setEntity(httpEntity)
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
                return jsonMapper.readValue(httpResponseEntity.getContent(), TransportPolicyValidationResponse.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public TransportPolicyValidationResponseList getTransportPolicyValidationStatus(String domainName) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/transportpolicy/validationstatus")
            .resolveTemplate("domainName", domainName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
                return jsonMapper.readValue(httpResponseEntity.getContent(), TransportPolicyValidationResponseList.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public TransportPolicyRules getTransportPolicyRulesByDomain(String domainName, String matchingTag, java.util.Map<String, java.util.List<String>> headers) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/transportpolicies")
            .resolveTemplate("domainName", domainName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        if (matchingTag != null) {
            httpUriRequest.addHeader("If-None-Match", matchingTag);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (headers != null) {
                    headers.put("tag", List.of(httpResponse.getFirstHeader("ETag").getValue()));
                }
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), TransportPolicyRules.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public Workloads getWorkloadsByService(String domainName, String serviceName, String matchingTag, java.util.Map<String, java.util.List<String>> headers) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/service/{serviceName}/workloads")
            .resolveTemplate("domainName", domainName)
            .resolveTemplate("serviceName", serviceName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        if (matchingTag != null) {
            httpUriRequest.addHeader("If-None-Match", matchingTag);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (headers != null) {
                    headers.put("tag", List.of(httpResponse.getFirstHeader("ETag").getValue()));
                }
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), Workloads.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public Workloads getWorkloadsByIP(String ip, String matchingTag, java.util.Map<String, java.util.List<String>> headers) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/workloads/{ip}")
            .resolveTemplate("ip", ip);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        if (matchingTag != null) {
            httpUriRequest.addHeader("If-None-Match", matchingTag);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (headers != null) {
                    headers.put("tag", List.of(httpResponse.getFirstHeader("ETag").getValue()));
                }
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), Workloads.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public WorkloadOptions putDynamicWorkload(String domainName, String serviceName, WorkloadOptions options) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/service/{serviceName}/workload/dynamic")
            .resolveTemplate("domainName", domainName)
            .resolveTemplate("serviceName", serviceName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpEntity httpEntity = new StringEntity(jsonMapper.writeValueAsString(options), ContentType.APPLICATION_JSON);
        HttpUriRequest httpUriRequest = RequestBuilder.put()
            .setUri(uriBuilder.build())
            .setEntity(httpEntity)
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 204:
                return null;
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public WorkloadOptions deleteDynamicWorkload(String domainName, String serviceName, String instanceId) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/service/{serviceName}/instanceId/{instanceId}/workload/dynamic")
            .resolveTemplate("domainName", domainName)
            .resolveTemplate("serviceName", serviceName)
            .resolveTemplate("instanceId", instanceId);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.delete()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 204:
                return null;
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public StaticWorkload putStaticWorkload(String domainName, String serviceName, StaticWorkload staticWorkload) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/service/{serviceName}/workload/static")
            .resolveTemplate("domainName", domainName)
            .resolveTemplate("serviceName", serviceName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpEntity httpEntity = new StringEntity(jsonMapper.writeValueAsString(staticWorkload), ContentType.APPLICATION_JSON);
        HttpUriRequest httpUriRequest = RequestBuilder.put()
            .setUri(uriBuilder.build())
            .setEntity(httpEntity)
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 204:
                return null;
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public StaticWorkload deleteStaticWorkload(String domainName, String serviceName, String name) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/service/{serviceName}/name/{name}/workload/static")
            .resolveTemplate("domainName", domainName)
            .resolveTemplate("serviceName", serviceName)
            .resolveTemplate("name", name);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.delete()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 204:
                return null;
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public StaticWorkloadServices getStaticWorkloadServicesByType(String serviceType, String serviceValue) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/services/{serviceType}")
            .resolveTemplate("serviceType", serviceType);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        if (serviceValue != null) {
            uriBuilder.setParameter("value", serviceValue);
        }
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), StaticWorkloadServices.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public Workloads getWorkloadsByDomain(String domainName, String matchingTag, java.util.Map<String, java.util.List<String>> headers) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/domain/{domainName}/workloads")
            .resolveTemplate("domainName", domainName);
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        if (matchingTag != null) {
            httpUriRequest.addHeader("If-None-Match", matchingTag);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
            case 304:
                if (headers != null) {
                    headers.put("tag", List.of(httpResponse.getFirstHeader("ETag").getValue()));
                }
                if (code == 304) {
                    return null;
                }
                return jsonMapper.readValue(httpResponseEntity.getContent(), Workloads.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public NetworkPolicyChangeImpactResponse evaluateNetworkPolicyChange(NetworkPolicyChangeImpactRequest detail) throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/transportpolicy/evaluatenetworkpolicychange");
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpEntity httpEntity = new StringEntity(jsonMapper.writeValueAsString(detail), ContentType.APPLICATION_JSON);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
            .setUri(uriBuilder.build())
            .setEntity(httpEntity)
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
                return jsonMapper.readValue(httpResponseEntity.getContent(), NetworkPolicyChangeImpactResponse.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, ResourceError.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

    public Schema getRdlSchema() throws URISyntaxException, IOException {
        UriTemplateBuilder uriTemplateBuilder = new UriTemplateBuilder(baseUrl, "/schema");
        URIBuilder uriBuilder = new URIBuilder(uriTemplateBuilder.getUri());
        HttpUriRequest httpUriRequest = RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();
        if (credsHeader != null) {
            httpUriRequest.addHeader(credsHeader, credsToken);
        }
        HttpEntity httpResponseEntity = null;
        try (CloseableHttpResponse httpResponse = client.execute(httpUriRequest, httpContext)) {
            int code = httpResponse.getStatusLine().getStatusCode();
            httpResponseEntity = httpResponse.getEntity();
            switch (code) {
            case 200:
                return jsonMapper.readValue(httpResponseEntity.getContent(), Schema.class);
            default:
                final String errorData = (httpResponseEntity == null) ? null : EntityUtils.toString(httpResponseEntity);
                throw (errorData != null && !errorData.isEmpty())
                    ? new ResourceException(code, jsonMapper.readValue(errorData, Object.class))
                    : new ResourceException(code);
            }
        } finally {
            EntityUtils.consumeQuietly(httpResponseEntity);
        }
    }

}
