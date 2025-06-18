package io.github.kelari.atg.model;

import java.util.*;

public class SpecScenariosTest {

    private String methodName = "";
    private String pathMethod = "";
    private String httpMethod = "";
    private List<CaseTest> caseTestList = new ArrayList<>(0);

    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPathMethod() {
        return pathMethod;
    }
    public void setPathMethod(String pathMethod) {
        this.pathMethod = pathMethod;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<CaseTest> getCaseTestList() {
        return caseTestList;
    }
    public void setCaseTestList(List<CaseTest> caseTestList) {
        this.caseTestList = caseTestList;
    }
}