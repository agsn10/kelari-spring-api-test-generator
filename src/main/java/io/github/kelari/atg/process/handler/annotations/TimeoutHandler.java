package io.github.kelari.atg.process.handler.annotations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.util.Constants;

/**
 * {@code TimeoutHandler} is a {@link MethodSpecHandler} implementation that adds the {@code @Timeout}
 * annotation to the generated test method if the {@link CaseTest#getTimeout()} value is greater than 0.
 * <p>
 * The {@code @Timeout} annotation is used to specify the maximum duration allowed for a test method
 * to run. If the {@code timeout} field in {@link CaseTest} is greater than 0, the method is annotated
 * with {@code @Timeout}. Otherwise, the test method will not have a timeout annotation.
 * <p>
 * Example generated output when {@code timeout} is greater than 0:
 * <pre>{@code
 * @Timeout(5000) // Timeout of 5000 milliseconds
 * }</pre>
 *
 * <p>If the {@code timeout} value is 0 or less, no annotation is added to the test method.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class TimeoutHandler implements MethodSpecHandler {

    /**
     * Adds the {@code @Timeout} annotation to the test method if the {@code timeout} value in
     * {@link CaseTest} is greater than 0.
     *
     * @param builder           the method builder to which the annotation will be added
     * @param codeBlockBuilder the code block builder for the method body (not used here)
     * @param spec              the test scenario specification (not used here)
     * @param test              the test case containing the timeout value
     * @param fullPath          the full request path (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (test.getTimeout() > 0) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.TIMEOUT)
                    .addMember("value", "$L", test.getTimeout())
                    .build());
        }
    }
}
