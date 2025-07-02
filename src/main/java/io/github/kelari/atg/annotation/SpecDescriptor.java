package io.github.kelari.atg.annotation;

import io.github.kelari.atg.annotation.enums.SeverityLevel;

import java.lang.annotation.*;

/**
 * This annotation is used to provide descriptive metadata for a specification.
 * It allows the user to define an epic, feature, story, severity level, and tags associated with the specification.
 *
 * Note: For this annotation to work correctly, the {@link KelariGenerateApiTest#report()} must have the {@link GenerateReport#enableReport()} attribute set to true.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface SpecDescriptor {
    /**
     * Defines the epic under which this specification is categorized.
     *
     * @return the epic name
     */
    String epic() default "";
    /**
     * Defines the feature that this specification represents.
     *
     * @return the feature name
     */
    String feature() default "";
    /**
     * Defines the user story related to this specification.
     *
     * @return the story name
     */
    String story() default "";
    /**
     * Defines the severity level of the specification.
     * Default is {@link SeverityLevel#MEDIUM}.
     *
     * @return the severity level
     */
    SeverityLevel severity() default SeverityLevel.MEDIUM;
    /**
     * Defines tags associated with this specification.
     *
     * @return an array of tags
     */
    String[] tags() default {};
}