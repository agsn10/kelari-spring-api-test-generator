package io.github.kelari.atg.annotation;

import java.lang.annotation.*;

/**
 * Container annotation that provides metadata for API test methods,
 * including a display name, execution scenarios, and container setup configuration.
 *
 * <p>This annotation serves as a higher-level descriptor for API test
 * documentation and automation, allowing a test method to declare
 * its purpose and expectations through nested {@link ApiTestCase} annotations.</p>
 *
 * <h3>Example usage:</h3>
 * <pre>
 * {@code
 * @ApiTestSpec(
 *     scenarios = {
 *         @ApiTestCase(
 *             displayName = "🚀 Should create a new customer successfully",
 *             order = 1,
 *             timeout = 5,
 *             expectedStatusCodes = HttpURLConnection.HTTP_OK
 *         )
 *     },
 *     containerSetup = @ContainerSetup(
 *         isolated = true,
 *         sqlScripts = {
 *             @SqlScript(path = "classpath:sql/init.sql", phase = SqlScript.ExecutionPhase.BEFORE),
 *             @SqlScript(path = "classpath:sql/cleanup.sql", phase = SqlScript.ExecutionPhase.AFTER)
 *         }
 *     )
 * )
 * </pre>
 *
 * <p>Attributes:</p>
 * <ul>
 *     <li><b>scenarios</b>: One or more {@link ApiTestCase} instances that describe the expected behavior and validations for the test method.</li>
 *     <li><b>containerSetup</b>: Configuration for container isolation and SQL script execution related to the test method.</li>
 * </ul>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>
 *         [<a href="https://github.com/kelari">Kelari</a>] – Initial implementation.
 * @since 1.0
 * @see ApiTestCase
 * @see ContainerSetup
 * @see SqlScript
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiTestSpec {

    /**
     * One or more goals that define how the test should be executed and validated,
     * such as order, timeout, and expected HTTP status codes.
     *
     * @return an array of {@link ApiTestCase} defining test behavior
     */
    ApiTestCase[] scenarios() default {};
}
