package io.github.kelari.atg.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents an expected HTTP response header for an API test case.
 * <p>
 * This annotation is typically used inside another annotation (e.g., {@code @ApiTestCase})
 * to declare expected headers that should be present in the HTTP response.
 * </p>
 *
 * <pre>{@code
 * @ApiTestCase(
 *     expectedHeaders = {
 *         @Header(name = "Content-Type", value = "application/json"),
 *         @Header(name = "X-Custom", value = {"value1", "value2"})
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
public @interface Header {

    /**
     * The name of the HTTP header.
     *
     * @return the header name
     */
    String name();

    /**
     * The expected value(s) for the header.
     * If multiple values are specified, they must all be present in the response.
     *
     * @return the expected header values
     */
    String[] value();
}