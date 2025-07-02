package io.github.kelari.atg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration for test suite generation.
 * <p>
 * Controls whether a test suite should be generated and its execution order.
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SuiteConfig {

    /**
     * Indicates whether a test suite should be generated for this controller.
     * <p>
     * When true, a higher-level test suite class will be generated to execute grouped test scenarios.
     * </p>
     *
     * @return true to enable test suite generation; false otherwise.
     */
    boolean generate() default false;

    /**
     * Defines the execution order of this test class within the test suite.
     * <p>
     * Lower values indicate higher priority (executed earlier).
     * </p>
     *
     * @return the order value. Default is 0.
     */
    int order() default 0;
}
