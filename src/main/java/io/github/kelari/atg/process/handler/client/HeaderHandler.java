package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;
import java.util.Optional;

/**
 * {@code HeaderHandler} is an implementation of {@link FluentMethodSpecHandler} responsible for handling HTTP
 * headers in a test scenario. This handler processes the headers specified in the {@code CaseTest} and generates
 * the corresponding code to add HTTP headers to the WebTestClient request.
 * <p>
 * It checks the provided test case for any header parameters and generates a statement to set the headers
 * using the WebTestClient's {@code header()} method. Each header key and value is taken from the test data.
 * </p>
 * <p>
 * The generated code adds headers to the request with the format: {@code header(key, value)}.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class HeaderHandler implements FluentMethodSpecHandler {

    /**
     * Handles the generation of the HTTP header statement for the test, adding the necessary headers to the request.
     * This method retrieves the header parameters from the {@code CaseTest} and generates the code to add those
     * headers to the WebTestClient request.
     *
     * @param statement         the {@link StringBuilder} used to build the generated test code
     * @param args             the list of arguments that are added to the statement (e.g., header key-value pairs)
     * @param spec             the specification for the test scenario
     * @param test             the individual test case containing the header parameters
     * @param fullPath         the full path of the test (not used here)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {

        // Check for header parameters in the test case
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getHeaderParams)
                .ifPresent(params -> {
                    // Iterate through the header parameters and generate header statements
                    for (String key : params.keySet()) {
                        statement.append("\n\t.header($S, safeString(data.get($S)))");
                        args.add(key);
                        args.add(key);
                    }
                });
    }
}