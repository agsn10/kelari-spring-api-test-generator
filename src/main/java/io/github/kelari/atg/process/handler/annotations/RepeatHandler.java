package io.github.kelari.atg.process.handler.annotations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.util.Constants;

/**
 * {@code RepeatHandler} is a {@link MethodSpecHandler} implementation that adds the {@code @Repeat}
 * annotation to the generated test method if the {@link CaseTest#getRepeat()} value is greater than 1.
 * <p>
 * The {@code @Repeat} annotation is used to repeat the execution of a test method multiple times. If
 * the {@code repeat} field in {@link CaseTest} is 1 or less, the method is annotated with {@code @Test}
 * instead, ensuring that only the specified number of repetitions occurs.
 * <p>
 * Example generated output when {@code repeat} is greater than 1:
 * <pre>{@code
 * @Repeat(5)
 * }</pre>
 *
 * <p>When the repeat count is 1 or less, the default {@code @Test} annotation will be used instead:
 * <pre>{@code
 * @Test
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class RepeatHandler implements MethodSpecHandler {

    /**
     * Adds the {@code @Repeat} annotation to the test method if the {@code repeat} value in
     * {@link CaseTest} is greater than 1. If {@code repeat} is 1 or less, the method is annotated
     * with {@code @Test} instead.
     *
     * @param builder           the method builder to which the annotation will be added
     * @param codeBlockBuilder the code block builder for the method body (not used here)
     * @param spec              the test scenario specification (not used here)
     * @param test              the test case containing the repeat count
     * @param fullPath          the full request path (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (test.getRepeat() > 1) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.REPEAT)
                    .addMember("value", "$L", test.getRepeat())
                    .build());
        } else {
            builder.addAnnotation(Constants.Imports.TEST);
        }
    }
}
