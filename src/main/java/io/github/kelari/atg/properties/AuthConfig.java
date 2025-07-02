package io.github.kelari.atg.properties;

import java.util.Objects;
import java.util.Properties;

public class AuthConfig {

    private String authUrl;
    private String authUsername;
    private String authPassword;
    private String parameterTokenName = "token";

    public void loadData(Properties props){
        this.authUrl = requireProperty(props, "kelari.auth.url");
        this.authUsername = requireProperty(props, "kelari.auth.username");
        this.authPassword = requireProperty(props, "kelari.auth.password");
        if(Objects.nonNull(props.getProperty("kelari.auth.parameterTokenName")))
            this.parameterTokenName = requireProperty(props, "kelari.auth.parameterTokenName");
    }

    private String requireProperty(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank())
            throw new IllegalStateException("Required property not found: " + key);
        return value;
    }

    public String getAuthUrl() {
        return authUrl;
    }
    public String getAuthUsername() {
        return authUsername;
    }
    public String getAuthPassword() {
        return authPassword;
    }
    public String getParameterTokenName() {
        return parameterTokenName;
    }
}
