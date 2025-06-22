package io.github.kelari.atg.process.handler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;

/**
 * A functional interface for handlers that modify a {@link MethodSpec} used to dynamically generate and modify
 * test methods. Each implementation of {@link MethodSpecHandler} is responsible for applying modifications to
 * the test method (such as adding annotations, generating code, or configuring parameters) based on the data
 * provided in a {@link SpecScenariosTest} and a {@link CaseTest}.
 *
 * <p>This interface allows the use of the Chain of Responsibility design pattern, where multiple handlers
 * can be applied sequentially to configure the test method as needed.</p>
 *

 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @see MethodSpec
 * @see SpecScenariosTest
 * @see CaseTest
 * @copyright 2025 Kelari. All rights reserved.
 */
@FunctionalInterface
public interface MethodSpecHandler {

    /**
     * Responsible for modifying the {@link MethodSpec.Builder} and {@link CodeBlock.Builder}
     * based on the provided test scenario specifications and case test data.
     * This method is called within a workflow where multiple handlers can be applied
     * sequentially to dynamically configure the test method.
     *
     * @param methodBuilder     the {@link MethodSpec.Builder} used to construct the test method.
     * @param codeBlockBuilder  the {@link CodeBlock.Builder} used to construct the method body.
     * @param spec              the {@link SpecScenariosTest} that contains the test scenario specifications.
     * @param test              the {@link CaseTest} that contains the specific case test data.
     * @param fullPath          the full URI path of the endpoint being tested.
     */
    void handle(MethodSpec.Builder methodBuilder, CodeBlock.Builder codeBlockBuilder, SpecScenariosTest spec, CaseTest test, String fullPath);
}
