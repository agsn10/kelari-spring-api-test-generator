package io.github.kelari.atg.process.handler.annotations;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.util.Constants;

import java.util.Objects;

/**
 * {@code DisplayNameHandler} is a {@link MethodSpecHandler} implementation responsible
 * for adding the {@code @DisplayName} annotation to the generated test method.
 *
 * <p>This annotation improves test readability by providing a human-friendly description
 * that appears in test reports and IDE test runners.
 *
 * <p>The display name value is taken from the {@code displayName} property of the {@link CaseTest}.
 *
 * <p>Example generated output:
 * <pre>{@code
 * @DisplayName("✅ Should return 200 OK when the ID is valid")
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class DisplayNameHandler implements MethodSpecHandler {

    /**
     * Adds the {@code @DisplayName} annotation to the test method, if the
     * {@code displayName} field is defined in the {@link CaseTest}.
     *
     * @param builder           the method builder to which the annotation will be added
     * @param codeBlockBuilder the code block builder for the method body (not used here)
     * @param spec              the test scenario specification (not used here)
     * @param test              the test case containing the display name
     * @param fullPath          the full request path (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (Objects.nonNull(test.getDisplayName()) && !test.getDisplayName().isEmpty()) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.DISPLAY_NAME)
                    .addMember("value", "$S", test.getDisplayName())
                    .build());
        }
    }
}
