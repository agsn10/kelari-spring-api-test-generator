package io.github.kelari.atg.model;

import java.util.LinkedHashMap;

public class CaseTest extends LinkedHashMap<String, CaseTest> {

    private String displayName;
    private int order;
    private int timeout;
    private int expectedStatusCode;
    private boolean requiresAuth;
    private String dataProviderClassName;
    private ParameterMetadataTest methodParameters;

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
}