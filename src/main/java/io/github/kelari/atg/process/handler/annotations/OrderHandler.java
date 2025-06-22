package io.github.kelari.atg.process.handler.annotations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.util.Constants;

/**
 * {@code OrderHandler} is a {@link MethodSpecHandler} implementation that adds the {@code @Order}
 * annotation to the generated test method.
 * <p>
 * The {@code @Order} annotation defines the execution order of test methods in a test class.
 * It is useful for ensuring that some tests are run before others, especially in integration testing.
 * <p>
 * The order value is retrieved from the {@code timeout} field in the {@link CaseTest}.
 * This may be a mistake; logically, the value should come from an {@code order} field.
 * If this is intentional, consider renaming the field or updating the logic.
 *
 * <p>Example generated output:
 * <pre>{@code
 * @Order(3)
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class OrderHandler implements MethodSpecHandler {

    /**
     * Adds the {@code @Order} annotation to the test method if the timeout value in {@link CaseTest}
     * is greater than zero. Note that it currently uses {@code getTimeout()} as the source for the order,
     * which may require clarification or adjustment.
     *
     * @param builder           the method builder to which the annotation will be added
     * @param codeBlockBuilder the code block builder for the method body (not used here)
     * @param spec              the test scenario specification (not used here)
     * @param test              the test case containing the timeout (used as order)
     * @param fullPath          the full request path (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (test.getTimeout() > 0) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.ORDER)
                    .addMember("value", "$L", test.getOrder())
                    .build());
        }
    }
}
