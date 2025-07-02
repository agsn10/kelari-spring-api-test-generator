package io.github.kelari.atg.annotation;

import io.github.kelari.atg.annotation.defaults.DummyMatcher;
import io.github.kelari.atg.annotation.enums.MatcherType;
import org.hamcrest.Matcher;

import java.lang.annotation.*;

/**
 * Annotation used to define a JSON path assertion for an API test case.
 * <p>
 * This annotation allows specifying a JSON path within the response body
 * and associating it with a matcher to validate the value found at that path.
 * Multiple {@code @JsonPath} annotations can be grouped using {@link JsonPaths}.
 * </p>
 *
 * <pre>{@code
 * @JsonPath(path = "$.data.name", type = MatcherType.EQUAL_TO, value = "Antonio Neto")
 * }</pre>
 *
 * @see JsonPaths
 * @see MatcherType
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(JsonPaths.class)
public @interface JsonPath {

    /**
     * The JSON path to evaluate in the response body.
     * <p>Example: {@code "$.data.name"}</p>
     *
     * @return the JSON path expression
     */
    String path();

    /**
     * The type of matcher to use for the assertion.
     * <p>Examples: {@code EQUAL_TO}, {@code NOT_NULL_VALUE}, {@code CONTAINS_STRING}</p>
     *
     * @return the matcher type
     */
    MatcherType type();

    /**
     * The expected value used in the matcher.
     * <p>
     * This is used for types like {@code EQUAL_TO}, {@code CONTAINS_STRING},
     * {@code NOT}, {@code ANY_OF}, etc. For {@code INSTANCE_OF}, this should
     * be the fully qualified class name (e.g., {@code "java.lang.Integer"}).
     * </p>
     *
     * @return the expected value as a string
     */
    String value() default "";

    /**
     * The custom matcher class to be used when {@code type = CUSTOM_CLASS}.
     * <p>
     * This should be a class that implements {@link Matcher}.
     * Ignored if {@code type} is not {@code CUSTOM_CLASS}.
     * </p>
     *
     * @return the custom matcher class
     */
    Class<? extends Matcher<?>> matcherClass() default DummyMatcher.class;
}
