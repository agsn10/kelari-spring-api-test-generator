package io.github.kelari.atg.model;

import java.util.LinkedHashMap;

public class ClassTest extends LinkedHashMap<String, SpecScenariosTest> {

    private String name = "";
    private String pathBase = "";
    private String packageName = "";
    private AuthTest authTest;

    public String getPathBase() {
        return pathBase;
    }
    public void setPathBase(String pathBase) {
        this.pathBase = pathBase;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public AuthTest getAuthTest() {
        return authTest;
    }
    public void setAuthTest(AuthTest authTest) {
        this.authTest = authTest;
    }

}
