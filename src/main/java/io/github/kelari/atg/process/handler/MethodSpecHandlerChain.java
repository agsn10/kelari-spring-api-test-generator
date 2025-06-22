package io.github.kelari.atg.process.handler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A helper class that manages a chain of {@link MethodSpecHandler} instances.
 * This class allows the dynamic construction of a chain of handlers that modify a {@link MethodSpec}
 * and a {@link CodeBlock} based on various conditions in the provided {@link SpecScenariosTest} and {@link CaseTest}.
 * The handlers are applied in the order they are added to the chain.
 *
 * <p>The {@link MethodSpecHandlerChain} class is used to apply various configurations to a test method,
 * such as annotations, parameters, and any necessary setup or teardown operations.</p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @see MethodSpecHandler
 * @copyright 2025 Kelari. All rights reserved.
 */
public class MethodSpecHandlerChain {

    private final List<MethodSpecHandler> handlers = new ArrayList<>();

    /**
     * Adds a new {@link MethodSpecHandler} to the handler chain.
     *
     * @param supplier a {@link Supplier} that provides an instance of the {@link MethodSpecHandler}.
     * @return the current {@link MethodSpecHandlerChain} instance for method chaining.
     */
    public MethodSpecHandlerChain add(Supplier<MethodSpecHandler> supplier) {
        handlers.add(supplier.get());
        return this;
    }

    /**
     * Applies all the handlers in the chain to the provided {@link MethodSpec.Builder} and {@link CodeBlock.Builder}.
     * Each handler in the chain is responsible for modifying the method spec and code block according to the test scenario.
     *
     * <p>Handlers might add annotations, generate code, or configure other aspects of the test method.</p>
     *
     * @param methodBuilder   the {@link MethodSpec.Builder} representing the test method being built.
     * @param codeBlockBuilder the {@link CodeBlock.Builder} used to build the body of the method.
     * @param spec            the {@link SpecScenariosTest} containing the test specifications.
     * @param test            the {@link CaseTest} containing the specific test case data.
     * @param fullPath        the full URI path of the endpoint being tested.
     */
    public void applyAll(MethodSpec.Builder methodBuilder, CodeBlock.Builder codeBlockBuilder, SpecScenariosTest spec, CaseTest test, String fullPath) {
        for (MethodSpecHandler handler : handlers) {
            handler.handle(methodBuilder, codeBlockBuilder, spec, test, fullPath);
        }
    }
}