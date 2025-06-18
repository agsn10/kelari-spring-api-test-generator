package io.github.kelari.atg.annotation;

import io.github.kelari.atg.data.DataLoad;

import java.lang.annotation.*;

/**
 * Defines the execution goals for an API test, such as execution order,
 * timeout duration, and expected HTTP status codes.
 *
 * <p>This annotation is useful for describing test control parameters,
 * enabling automatic test generation or better test organization
 * in API testing frameworks.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @ApiTestCase(
 *     displayName = "🚀 Should create a new customer successfully",
 *     order = 1,
 *     timeout = 5,
 *     expectedStatusCodes = HttpURLConnection.HTTP_OK
 * )
 * }
 * </pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Target(ElementType.METHOD) // Applicable to methods only
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime
@Documented // Included in the generated JavaDoc
public @interface ApiTestCase {

    /**
     * Display name used in test reports, logs, or generated documentation.
     * Helps identify the test’s purpose in a human-readable format.
     *
     * @return a descriptive display name for the test
     */
    String displayName() default "";

    /**
     * Defines the order in which the test should be executed.
     * Useful for chaining or logically organizing tests.
     *
     * @return the execution order of the test
     */
    int order() default 0;

    /**
     * Timeout duration in seconds for the test execution.
     * If the test exceeds this time, it should be considered failed.
     *
     * @return maximum allowed execution time in seconds
     */
    int timeout() default 0;

    /**
     * Expected HTTP status code for the API response.
     * Useful for validating test results automatically.
     *
     * @return the expected HTTP status code
     */
    int expectedStatusCode() default 0;

    /**
     * Defines a class that will load test data to be used during test execution.
     * The class must implement the {@link DataLoad} interface.
     *
     * @return a class implementing {@link DataLoad}, default is None.class (no data)
     */
    String dataProviderClassName() default "";

    /**
     * Indicates whether the test requires authentication.
     * Useful for including authorization headers in the test automatically.
     *
     * @return true if the endpoint requires authentication; false otherwise
     */
    boolean requiresAuth() default false;
}