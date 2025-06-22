package io.github.kelari.atg.process.handler;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;

import java.util.List;

/**
 * A functional interface representing a handler that modifies a test method's statement and arguments.
 * This handler is part of a chain that applies modifications sequentially to the test method's construction.
 *
 * <p>The {@link FluentMethodSpecHandler} is responsible for handling specific parts of a dynamically generated test method,
 * such as modifying the URI, adding headers, body, cookies, or any other part of the method. The handler applies modifications
 * by modifying the provided {@link StringBuilder} (statement) and {@link List} of arguments.</p>
 *
 * <p>This interface is intended to be used in conjunction with the {@link FluentMethodSpecHandlerChain}, which applies
 * multiple handlers to generate the complete test method.</p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 * @see FluentMethodSpecHandlerChain
 * @see CaseTest
 * @see SpecScenariosTest
 */
@FunctionalInterface
public interface FluentMethodSpecHandler {

    /**
     * Handles a part of the test method's statement and arguments.
     *
     * <p>This method is intended to apply modifications to the test method's statement and arguments,
     * such as appending specific parts to the {@link StringBuilder} statement or adding arguments
     * to the {@link List} args based on the provided test data and scenario.</p>
     *
     * @param statement a {@link StringBuilder} that accumulates the test method's statement
     * @param args      a list of arguments that will be used within the test method's statement
     * @param spec      the {@link SpecScenariosTest} that contains the test scenario specifications
     * @param test      the {@link CaseTest} that contains the specific case test data
     * @param fullPath  the full URI path of the endpoint being tested
     */
    void handle(StringBuilder statement,
                List<Object> args,
                SpecScenariosTest spec,
                CaseTest test,
                String fullPath);
}
