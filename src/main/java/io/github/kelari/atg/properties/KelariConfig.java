package io.github.kelari.atg.properties;

import java.util.Objects;

public class KelariConfig {

    private AuthConfig auth;
    private TestContainer container;

    public AuthConfig getAuth() {
        if(Objects.isNull(auth))
            auth = new AuthConfig();
        return auth;
    }

    public void setAuth(AuthConfig auth) {
        this.auth = auth;
    }

    public TestContainer getContainer() {
        if(Objects.isNull(container))
            container = new TestContainer();
        return container;
    }

    public void setContainer(TestContainer container) {
        this.container = container;
    }
}
