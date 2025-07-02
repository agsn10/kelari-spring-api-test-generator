package io.github.kelari.atg.annotation;

import java.lang.annotation.*;

/**
 * Annotation to mark a Spring Boot controller for automatic API test generation.
 * <p>
 * This annotation is applied at the class level (e.g., on a controller) and provides metadata
 * for test generation, such as authentication configuration to retrieve tokens before running test cases.
 * </p>
 *
 * <h3>Example usage on a controller class:</h3>
 *
 * <pre>{@code
 * @RestController
 * @RequestMapping("/clients")
 * @KelariGenerateApiTest(
 *     auth = @AuthConfig(
 *         authUrl = "/auth/token",
 *         username = "admin",
 *         password = "123456"
 *     ),
 *     suiteConfig = @SuiteConfig(generate = true, order = 1)
 * )
 * public class ClientController {
 *     ...
 * }
 * }</pre>
 *
 * These credentials can be used to authenticate before running test cases
 * and automatically inject the token when {@link ApiTestCase#requiresAuth()} is true.
 *
 * <p>
 * The annotation allows you to:
 * <ul>
 *     <li>Configure authentication details, such as login credentials for token retrieval.</li>
 *     <li>Enable or disable the automatic generation of API test cases for the annotated controller.</li>
 *     <li>Configure test container properties for a more isolated and controlled testing environment.</li>
 *     <li>Provide SQL scripts for initialization before test execution.</li>
 *     <li>Configure test suite generation behavior and execution order.</li>
 * </ul>
 * </p>
 *
 * <p>
 * The annotation is part of the <strong>Kelari</strong> framework, a tool for automating API test generation.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>
 *         [<a href="https://github.com/kelari">Kelari</a>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 * @see ApiTestSpec
 * @see ApiTestCase
 * @see AuthConfig
 * @see TestContainerConfig
 * @see SqlScript
 * @see SuiteConfig
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KelariGenerateApiTest {

    boolean enabledAuth() default false;

    boolean enabledTestContainer() default false;

    boolean isolatedTestContainer() default false;

    TestContainerConfig isolatedContainerTestConfig() default @TestContainerConfig;
    /**
     * Configuration for report generation.
     * <p>
     * This defines whether a report will be generated and its format.
     * </p>
     *
     * @return Report generation configuration
     */
    GenerateReport report() default @GenerateReport();

    /**
     * SQL scripts to execute before running the tests.
     * <p>
     * These scripts will be run to initialize the database or the environment for the test.
     * </p>
     *
     * @return Array of SQL scripts to execute before tests.
     */
    SqlScript[] sqlScripts() default {};

    /**
     * Configuration for test suite generation and execution order.
     * <p>
     * Allows control over whether a test suite should be generated for this controller,
     * and in what order it should be executed relative to other suites.
     * </p>
     *
     * @return Suite generation and ordering configuration.
     */
    SuiteConfig suiteConfig() default @SuiteConfig;
}
