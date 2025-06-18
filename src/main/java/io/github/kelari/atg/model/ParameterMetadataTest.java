package io.github.kelari.atg.model;

import java.util.HashMap;
import java.util.Map;

public class ParameterMetadataTest {

    private String httpMethod;
    private int expectedStatusCode;

    private Map<String, String> pathParams = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> headerParams = new HashMap<>();
    private Map<String, String> cookieParams = new HashMap<>();
    private Map<String, Map<String, String>> matrixParams = new HashMap<>();
    private Map<String, String> body = new HashMap<>();
    private Map<String, String> formParams = new HashMap<>();
    private Map<String, String> fileParams = new HashMap<>();

    private boolean multipart;

    // Getters e Setters

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public int getExpectedStatusCode() { return expectedStatusCode; }
    public void setExpectedStatusCode(int expectedStatusCode) { this.expectedStatusCode = expectedStatusCode; }

    public Map<String, String> getPathParams() { return pathParams; }
    public void setPathParams(Map<String, String> pathParams) { this.pathParams = pathParams; }

    public Map<String, String> getQueryParams() { return queryParams; }
    public void setQueryParams(Map<String, String> queryParams) { this.queryParams = queryParams; }

    public Map<String, String> getHeaderParams() { return headerParams; }
    public void setHeaderParams(Map<String, String> headerParams) { this.headerParams = headerParams; }

    public Map<String, String> getCookieParams() { return cookieParams; }
    public void setCookieParams(Map<String, String> cookieParams) { this.cookieParams = cookieParams; }

    public Map<String, Map<String, String>> getMatrixParams() { return matrixParams; }
    public void setMatrixParams(Map<String, Map<String, String>> matrixParams) { this.matrixParams = matrixParams; }

    public Map<String, String> getBody() { return body; }
    public void setBody(Map<String, String> body) { this.body = body; }

    public Map<String, String> getFormParams() { return formParams; }
    public void setFormParams(Map<String, String> formParams) { this.formParams = formParams; }

    public Map<String, String> getFileParams() { return fileParams; }
    public void setFileParams(Map<String, String> fileParams) { this.fileParams = fileParams; }

    public boolean isMultipart() { return multipart; }
    public void setMultipart(boolean multipart) { this.multipart = multipart; }

    @Override
    public String toString() {
        return "ParameterMetadataTest{" +
                "httpMethod='" + httpMethod + '\'' +
                ", expectedStatusCode=" + expectedStatusCode +
                ", pathParams=" + pathParams +
                ", queryParams=" + queryParams +
                ", headerParams=" + headerParams +
                ", cookieParams=" + cookieParams +
                ", matrixParams=" + matrixParams +
                ", body=" + body +
                ", formParams=" + formParams +
                ", fileParams=" + fileParams +
                ", multipart=" + multipart +
                '}';
    }
}