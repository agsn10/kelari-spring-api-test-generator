package io.github.kelari.atg.model;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class CaseTest extends LinkedHashMap<String, CaseTest> {

    private String displayName;
    private int order;
    private int timeout;
    private int expectedStatusCode;
    private boolean requiresAuth;
    private String dataProviderClassName;
    private int repeat;
    private boolean enableLogging = false;
    private ParameterMetadataTest methodParameters;
    private long responseTimeoutSeconds;
    private Set<Header> expectedHeaders = new HashSet<>(0);
    private Set<Cookie> expectedCookies = new HashSet<>(0);
    private Set<JsonPath> jsonPaths = new HashSet<>(0);

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public CaseTest displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public CaseTest order(int order) {
        this.order = order;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public CaseTest timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getExpectedStatusCode() {
        return expectedStatusCode;
    }
    public void setExpectedStatusCode(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }
    public CaseTest expectedStatusCode(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
        return this;
    }

    public boolean isRequiresAuth() {
        return requiresAuth;
    }
    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }
    public CaseTest requiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
        return this;
    }

    public String getDataProviderClassName() {
        return dataProviderClassName;
    }
    public void setDataProviderClassName(String dataProviderClassName) {
        this.dataProviderClassName = dataProviderClassName;
    }
    public CaseTest dataProviderClassName(String dataProviderClassName) {
        this.dataProviderClassName = dataProviderClassName;
        return this;
    }

    public ParameterMetadataTest getMethodParameters() {
        return this.methodParameters;
    }
    public void setMethodParameters(ParameterMetadataTest methodParameters) {
        this.methodParameters = methodParameters;
    }
    public CaseTest methodParameters(ParameterMetadataTest methodParameters) {
        this.methodParameters = methodParameters;
        return this;
    }

    public int getRepeat() {
        return repeat;
    }
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
    public CaseTest repeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }
    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }
    public CaseTest enableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
        return this;
    }

    public long getResponseTimeoutSeconds() {
        return responseTimeoutSeconds;
    }
    public void setResponseTimeoutSeconds(long responseTimeoutSeconds) {
        this.responseTimeoutSeconds = responseTimeoutSeconds;
    }
    public CaseTest responseTimeoutSeconds(long responseTimeoutSeconds) {
        this.responseTimeoutSeconds = responseTimeoutSeconds;
        return this;
    }

    public Set<Header> getExpectedHeaders() {
        return expectedHeaders;
    }
    public void setExpectedHeaders(Set<Header> expectedHeaders) {
        this.expectedHeaders = expectedHeaders;
    }
    public CaseTest expectedHeaders(Header header) {
        this.expectedHeaders.add(header);
        return this;
    }

    public Set<Cookie> getExpectedCookies() {
        return expectedCookies;
    }
    public void setExpectedCookies(Set<Cookie> expectedCookies) {
        this.expectedCookies = expectedCookies;
    }
    public void expectedCookies(Cookie cookie) {
        this.expectedCookies.add(cookie);
    }

    public Set<JsonPath> getJsonPaths() {
        return jsonPaths;
    }
    public void setJsonPaths(Set<JsonPath> jsonPaths) {
        this.jsonPaths = jsonPaths;
    }
    public CaseTest jsonPaths(JsonPath jsonPath) {
        this.jsonPaths.add(jsonPath);
        return this;
    }
}