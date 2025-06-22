package io.github.kelari.atg.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an expected HTTP cookie in the response for an API test case.
 * <p>
 * This annotation is typically used as part of {@code @ApiTestCase} to assert
 * that a cookie with the specified name and value is present in the HTTP response.
 * </p>
 *
 * <pre>{@code
 * @ApiTestCase(
 *     expectedCookies = {
 *         @Cookie(name = "sessionId", value = "abc123"),
 *         @Cookie(name = "locale", value = "en-US")
 *     }
 * )
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Cookie {

    /**
     * The name of the expected HTTP cookie.
     *
     * @return the cookie name
     */
    String name();

    /**
     * The expected value of the HTTP cookie.
     *
     * @return the cookie value
     */
    String value();
}