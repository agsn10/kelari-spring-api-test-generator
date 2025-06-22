package io.github.kelari.atg.process.handler;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A helper class responsible for managing a chain of {@link FluentMethodSpecHandler} instances.
 * This class follows the Chain of Responsibility design pattern to apply multiple handlers sequentially
 * to modify a {@link StringBuilder} and a list of arguments, ultimately building a dynamic test method.
 *
 * <p>Each {@link FluentMethodSpecHandler} is responsible for handling a specific part of the test method,
 * such as URI, headers, body, cookies, etc. The {@link FluentMethodSpecHandlerChain} allows these handlers
 * to be applied in sequence to modify the test method construction process dynamically.</p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 * @see FluentMethodSpecHandler
 * @see CaseTest
 * @see SpecScenariosTest
 */
public class FluentMethodSpecHandlerChain {
    private final List<FluentMethodSpecHandler> handlers = new ArrayList<>();

    /**
     * Adds a {@link FluentMethodSpecHandler} to the chain.
     *
     * @param supplier a supplier that provides an instance of the {@link FluentMethodSpecHandler} to be added
     * @return the current instance of {@link FluentMethodSpecHandlerChain} for method chaining
     */
    public FluentMethodSpecHandlerChain add(Supplier<FluentMethodSpecHandler> supplier) {
        handlers.add(supplier.get());
        return this;
    }

    /**
     * Applies all handlers in the chain to modify the provided {@link StringBuilder} and list of arguments
     * based on the test scenario and case test data.
     *
     * <p>This method iterates over all handlers in the chain and calls their {@link FluentMethodSpecHandler#handle}
     * method to apply modifications to the statement and arguments.</p>
     *
     * @param statement a {@link StringBuilder} that accumulates the test method's statement
     * @param args      a list of arguments that will be used within the test method's statement
     * @param spec      the {@link SpecScenariosTest} that contains the test scenario specifications
     * @param test      the {@link CaseTest} that contains the specific case test data
     * @param fullPath  the full URI path of the endpoint being tested
     */
    public void applyAll(StringBuilder statement,
                         List<Object> args,
                         SpecScenariosTest spec,
                         CaseTest test,
                         String fullPath) {
        for (FluentMethodSpecHandler handler : handlers) {
            handler.handle(statement, args, spec, test, fullPath);
        }
    }
}
