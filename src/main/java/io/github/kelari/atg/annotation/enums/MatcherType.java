package io.github.kelari.atg.annotation.enums;

/**
 * Defines the types of matchers supported for JSON path assertions in API test cases.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public enum MatcherType {
    /**
     * Verifies that the value is exactly equal to the expected one.
     */
    EQUAL_TO,
    /**
     * Verifies that a collection contains at least one occurrence of the specified item.
     */
    HAS_ITEM,
    /**
     * Verifies that the value is {@code null}.
     */
    NULL_VALUE,
    /**
     * Verifies that the value is not {@code null}.
     */
    NOT_NULL_VALUE,
    /**
     * Applies logical negation to the specified matcher.
     */
    NOT,
    /**
     * Verifies that the value is an instance of a specified type.
     */
    INSTANCE_OF,
    /**
     * Verifies that the value is greater than the expected one.
     */
    GREATER_THAN,
    /**
     * Verifies that the value is less than the expected one.
     */
    LESS_THAN,
    /**
     * Verifies that the string contains a given substring.
     */
    CONTAINS_STRING,
    /**
     * Verifies that the string starts with a given prefix.
     */
    STARTS_WITH,
    /**
     * Verifies that the string ends with a given suffix.
     */
    ENDS_WITH,
    /**
     * Verifies that the value matches any of the provided matchers.
     */
    ANY_OF,
    /**
     * Verifies that a collection contains a specific element.
     */
    CONTAINS,
    /**
     * Uses a custom matcher class provided by the user for advanced validation logic.
     */
    CUSTOM_CLASS
}