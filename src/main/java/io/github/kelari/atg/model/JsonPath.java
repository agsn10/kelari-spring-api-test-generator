package io.github.kelari.atg.model;

import io.github.kelari.atg.annotation.enums.MatcherType;

public class JsonPath {

    private String path;
    private MatcherType type;
    private String value;
    private String matcherClass;

    public JsonPath(String path, MatcherType type, String value, String matcherClass) {
        this.path = path;
        this.type = type;
        this.value = value;
        this.matcherClass = matcherClass;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public MatcherType getType() {
        return type;
    }
    public void setType(MatcherType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getMatcherClass() {
        return matcherClass;
    }
    public void setMatcherClass(String matcherClass) {
        this.matcherClass = matcherClass;
    }
}