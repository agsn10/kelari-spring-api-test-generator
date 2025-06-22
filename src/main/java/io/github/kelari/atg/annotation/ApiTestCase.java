package io.github.kelari.atg.annotation;

import io.github.kelari.atg.data.DataLoad;

import java.lang.annotation.*;
import java.time.Duration;

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
 *     expectedStatusCode = HttpURLConnection.HTTP_OK
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
     * @return a class implementing {@link DataLoad}, default is empty string (no provider)
     */
    String[] dataProviderClassName() default {};

    /**
     * Indicates whether the test requires authentication.
     * Useful for including authorization headers in the test automatically.
     *
     * @return true if the endpoint requires authentication; false otherwise
     */
    boolean requiresAuth() default false;

    /**
     * Defines a list of expected JSONPath expressions and their expected values
     * to be validated against the API response body.
     *
     * @return an array of {@link JsonPath} validations
     */
    JsonPath[] jsonPaths() default {};

    /**
     * Enables or disables logging for this specific test case.
     * Useful for debugging or tracing execution details.
     *
     * @return true if logging should be enabled; false otherwise
     */
    boolean enableLogging() default false;

    /**
     * Indicates whether this test case should be executed as a parameterized test.
     * Useful when executing the same logic with different inputs.
     *
     * @return true if the test is parameterized; false otherwise
     */
    //boolean parameterizedTest() default false;

    /**
     * Defines the list of HTTP headers expected to be present in the response.
     * Useful for validating response metadata.
     *
     * @return an array of expected response headers
     */
    Header[] expectedHeaders() default {};

    /**
     * Defines the list of HTTP cookies expected to be present in the response.
     * Useful for validating response metadata.
     *
     * @return an array of expected response cookies
     */
    Cookie[] expectedCookies() default {};

    /**
     * Defines how many times the test should be executed consecutively.
     * Useful for stability testing or retries.
     *
     * @return the number of times to repeat the test
     */
    int repeat() default 1;

    /**
     * Defines the maximum duration to wait for a response, in seconds.
     * Overrides the global timeout if specified.
     *
     * @return the response timeout in seconds; -1 means no override
     */
    long responseTimeoutSeconds() default -1;
}