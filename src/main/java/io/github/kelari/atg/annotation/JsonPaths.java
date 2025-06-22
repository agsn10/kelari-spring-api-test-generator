package io.github.kelari.atg.annotation;

import java.lang.annotation.*;

/**
 * Container annotation that allows multiple {@link JsonPath} annotations to be applied
 * to a single test method.
 * <p>
 * This is used implicitly when multiple {@code @JsonPath} annotations are declared
 * on the same method.
 *
 * @see JsonPath
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonPaths {
    /**
     * The array of {@link JsonPath} annotations.
     *
     * @return an array of JsonPath annotations
     */
    JsonPath[] value();
}
