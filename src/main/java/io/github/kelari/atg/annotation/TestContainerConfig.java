package io.github.kelari.atg.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to configure the TestContainer settings for a controller/test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface TestContainerConfig {

    /**
     * Name of the Docker image (e.g., "postgres:15", "mysql:8", etc.)
     */
    String image() default "";

    /**
     * Name of the database to be created in the container.
     */
    String database() default "";

    /**
     * Username for authentication in the database.
     */
    String username() default "";

    /**
     * Password for authentication in the database.
     */
    String password() default "";

    /**
     * Port that the container will expose for local access (e.g., 5432).
     * If set to 0, the port will be assigned dynamically.
     */
    int exposedPort() default 0;

    /**
     * Additional environment variables to be passed to the container.
     */
    String[] env() default {};

    /**
     * Maximum wait time (in seconds) until the container is ready.
     */
    int startupTimeoutSeconds() default 60;
}
