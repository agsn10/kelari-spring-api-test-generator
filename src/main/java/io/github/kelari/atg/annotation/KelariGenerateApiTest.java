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
 *     authUrl = "/auth/token",
 *     username = "admin",
 *     password = "123456"
 * )
 * public class ClientController {
 *     ...
 * }
 * }</pre>
 *
 * These credentials can be used to authenticate before running test cases
 * and automatically inject the token when {@link ApiTestCase#requiresAuth()} is true.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 * @see ApiTestSpec
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KelariGenerateApiTest {

    /**
     * Endpoint URL for authentication to obtain the token.
     *
     * @return the authentication URL (e.g., "/auth/token")
     */
    String authUrl() default "";

    /**
     * Username used for authentication.
     *
     * @return the username
     */
    String username() default "";

    /**
     * Password used for authentication.
     *
     * @return the password
     */
    String password() default "";

    /**
     * Name of the JSON field that contains the authentication token.
     * It will be used as: "$.{parameterTokenName}"
     */
    String parameterTokenName() default "token";
}