package io.github.kelari.atg.model;

public class AuthTest {
    private String authUrl;
    private String password;
    private String username;
    private String parameterTokenName;

    public String getAuthUrl() {
        return authUrl;
    }
    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }
    public AuthTest authUrl(String authUrl) {
        this.authUrl = authUrl;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public AuthTest password(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public AuthTest username(String username) {
        this.username = username;
        return this;
    }

    public String getParameterTokenName() {
        return parameterTokenName;
    }
    public void setParameterTokenName(String parameterTokenName) {
        this.parameterTokenName = parameterTokenName;
    }
    public AuthTest parameterTokenName(String parameterTokenName) {
        this.parameterTokenName = parameterTokenName;
        return this;
    }
}
