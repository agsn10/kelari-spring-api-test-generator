package io.github.kelari.atg.annotation;

import io.github.kelari.atg.annotation.defaults.NoOpSetup;

import java.lang.annotation.*;

/**
 * Container annotation that provides metadata for API test methods,
 * including a display name and a set of execution goals.
 *
 * <p>This annotation serves as a higher-level descriptor for API test
 * documentation and automation, allowing a test method to declare
 * its purpose and expectations through nested {@link ApiTestCase} annotations.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @ApiTestSpec(
 *     scenarios = {
 *         @ApiTestCase(displayName = "🚀 Should create a new customer successfully",
 *                      order = 1,
 *                      timeout = 5,
 *                      expectedStatusCodes = HttpURLConnection.HTTP_OK
 *                      )
 *         }
 * )
 * }
 * </pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Target(ElementType.METHOD) // Annotation applicable to methods only
@Retention(RetentionPolicy.RUNTIME) // Available at runtime
@Documented // Included in Javadoc
public @interface ApiTestSpec {
    /**
     * One or more goals that define how the test should be executed and validated,
     * such as order, timeout, and expected HTTP status codes.
     *
     * @return an array of {@link ApiTestCase} defining test behavior
     */
    ApiTestCase[] scenarios() default {};
}