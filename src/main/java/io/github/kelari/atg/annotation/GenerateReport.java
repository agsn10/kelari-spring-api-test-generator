package io.github.kelari.atg.annotation;

import io.github.kelari.atg.annotation.enums.ReportFormat;

import java.lang.annotation.*;

/**
 * This annotation is used to enable or disable the generation of reports for a specification.
 * Additionally, it allows defining the format of the exported report.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface GenerateReport {
    /**
     * Defines whether the report generation is enabled or not.
     *
     * @return true if the report generation is enabled, false otherwise
     */
    boolean enableReport() default false;
    /**
     * Defines the format of the report when enabled.
     * Possible values could be "PDF", "HTML", or "CSV".
     *
     * @return the report format
     */
    ReportFormat reportFormat() default ReportFormat.JSON;
}
