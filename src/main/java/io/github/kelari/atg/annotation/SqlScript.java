package io.github.kelari.atg.annotation;

import io.github.kelari.atg.annotation.enums.ExecutionPhase;

import java.lang.annotation.*;

/**
 * Annotation to define an SQL script that will be executed before or after the test case execution.
 * <p>
 * This annotation allows you to specify the location of the SQL script, the execution phase (before or after the test),
 * and whether to ignore errors during script execution.
 * </p>
 *
 * <h3>Example usage:</h3>
 * <pre>{@code
 * @SqlScript(path = "classpath:scripts/init.sql", phase = SqlScript.ExecutionPhase.BEFORE)
 * @SqlScript(path = "classpath:scripts/cleanup.sql", phase = SqlScript.ExecutionPhase.AFTER)
 * public class ClientControllerTest {
 *     // Test methods go here
 * }
 * }</pre>
 *
 * <h3>Attributes:</h3>
 * <ul>
 *     <li><b>path</b>: The path to the SQL script in the classpath (e.g., "scripts/init.sql").</li>
 *     <li><b>phase</b>: The phase in which the SQL script is executed. It can either be {@link SqlScript.ExecutionPhase#BEFORE}
 *         (before the test method) or {@link SqlScript.ExecutionPhase#AFTER} (after the test method).</li>
 *     <li><b>ignoreErrors</b>: If set to true, errors in script execution are ignored.</li>
 * </ul>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface SqlScript {

    /**
     * The path to the SQL script in the classpath (e.g., "scripts/init.sql").
     *
     * @return The path of the SQL script to be executed.
     */
    String path();

    /**
     * The execution phase of the script: BEFORE or AFTER the test execution.
     * <p>
     * <strong>BEFORE</strong> executes before the test method,
     * <strong>AFTER</strong> executes after the test method.
     * </p>
     *
     * @return The execution phase.
     */
    ExecutionPhase phase() default ExecutionPhase.BEFORE;

    /**
     * Whether to ignore errors during the execution of the script.
     *
     * @return Whether to ignore script execution errors.
     */
    boolean ignoreErrors() default false;
}