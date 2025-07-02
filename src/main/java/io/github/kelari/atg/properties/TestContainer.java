package io.github.kelari.atg.properties;

import java.util.Objects;
import java.util.Properties;

public class TestContainer {

    private String image;
    private String database;
    private String username;
    private String password;
    private int startupTimeoutSeconds;
    private String[] env;
    private int exposedPort;

    public void loadData(Properties props){
        this.image = requireProperty(props, "kelari.test.container.image");
        this.database = requireProperty(props, "kelari.test.container.database");
        this.username = requireProperty(props, "kelari.test.container.username");
        this.password = requireProperty(props, "kelari.test.container.password");
        this.startupTimeoutSeconds = Integer.parseInt(requireProperty(props, "kelari.test.container.startupTimeoutSeconds"));
        this.env = requireProperty(props, "kelari.test.container.env").split(",");
        this.exposedPort = Integer.parseInt(requireProperty(props, "kelari.test.container.exposedPort"));
    }

    private String requireProperty(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank())
            throw new IllegalStateException("Required property not found: " + key);
        return value;
    }

    public String getImage() {
        return image;
    }
    public String getDatabase() {
        return database;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getStartupTimeoutSeconds() {
        return startupTimeoutSeconds;
    }
    public String[] getEnv() {
        return env;
    }
    public int getExposedPort() {
        return exposedPort;
    }
}
