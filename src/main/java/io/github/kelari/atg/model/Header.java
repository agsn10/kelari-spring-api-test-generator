package io.github.kelari.atg.model;

public class Header {
    private String name;
    private String[] values;

    public Header(String name, String... values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }
    public String[] getValues() {
        return values;
    }
}