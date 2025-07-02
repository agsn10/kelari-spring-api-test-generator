package io.github.kelari.atg.annotation;

import io.github.kelari.atg.data.DataLoad;

import java.lang.annotation.*;

/**
 * Defines the execution goals and expectations for an individual API test case,
 * such as order, timeout, authentication requirements, response validations, and metadata checks.
 *
 * <p>This annotation allows precise control over how a test should be executed and validated,
 * enabling automation frameworks like <strong>Kelari</strong> to generate, organize and execute
 * API tests consistently.</p>
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * @ApiTestCase(
 *     displayName = "🚀 Should create a new customer successfully",
 *     order = 1,
 *     timeout = 5,
 *     expectedStatusCode = HttpURLConnection.HTTP_OK,
 *     requiresAuth = true,
 *     jsonPaths = {
 *         @JsonPath(expression = "$.id", expected = "\\d+"),
 *         @JsonPath(expression = "$.name", expected = "John Doe")
 *     }
 * )
 * }</pre>
 *
 * <p>Attributes:</p>
 * <ul>
 *     <li><b>displayName</b>: Human-readable test name for reports/logs.</li>
 *     <li><b>order</b>: Execution order within the test suite.</li>
 *     <li><b>timeout</b>: Time limit for test execution in seconds.</li>
 *     <li><b>expectedStatusCode</b>: Expected HTTP response code.</li>
 *     <li><b>dataProviderClassName</b>: Fully qualified name(s) of classes implementing {@link DataLoad} to provide dynamic test data.</li>
 *     <li><b>requiresAuth</b>: Whether a bearer token is required.</li>
 *     <li><b>jsonPaths</b>: JSONPath validations against the response body.</li>
 *     <li><b>enableLogging</b>: Enables verbose logging for the test.</li>
 *     <li><b>parameterizedTest</b>: Marks the test as parameterized.</li>
 *     <li><b>expectedHeaders</b>: Expected headers in the response.</li>
 *     <li><b>expectedCookies</b>: Expected cookies in the response.</li>
 *     <li><b>repeat</b>: Number of times to repeat the test.</li>
 *     <li><b>responseTimeoutSeconds</b>: Max duration (in seconds) to wait for the response. Overrides the global timeout if set.</li>
 *     <li><b>specDescriptor</b>: Descriptive metadata for display or documentation purposes.</li>
 * </ul>
 *
 * @see JsonPath
 * @see DataLoad
 * @see Header
 * @see Cookie
 * @see SpecDescriptor
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
     * Defines a class or classes that will load test data to be used during test execution.
     * The class must implement the {@link DataLoad} interface.
     *
     * @return an array of fully qualified class names that implement {@link DataLoad}
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
    boolean parameterizedTest() default false;

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

    /**
     * Descriptive metadata used for test categorization, grouping, or documentation.
     *
     * @return a {@link SpecDescriptor} providing high-level test metadata
     */
    SpecDescriptor specDescriptor() default @SpecDescriptor();
}
