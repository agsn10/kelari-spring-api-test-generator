package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;
import java.util.Optional;

/**
 * {@code CookieHandler} is an implementation of {@link FluentMethodSpecHandler} responsible for handling cookie parameters
 * in API test cases. It processes the cookie parameters from the test's method metadata and appends the appropriate statements
 * to the generated test code.
 * <p>
 * This handler will extract the cookie parameters from the test case's method metadata and construct statements to include
 * cookies in the WebTestClient request.
 * </p>
 * <p>
 * Example generated output:
 * <pre>{@code
 * .cookie("cookieName", safeString(data.get("cookieName")))
 * }</pre>
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class CookieHandler implements FluentMethodSpecHandler {

    /**
     * Handles the inclusion of cookies in the WebTestClient request by processing the cookie parameters defined in the test case.
     * For each cookie parameter, a statement is generated to add it to the WebTestClient request.
     *
     * @param statement the StringBuilder used to append the generated statements
     * @param args      the list of arguments for the generated statements
     * @param spec      the specification for the test scenario
     * @param test      the individual test case containing the method parameters with cookie information
     * @param fullPath  the full path of the test (not used here)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getCookieParams)
                .ifPresent(params -> {
                    for (String key : params.keySet()) {
                        statement.append("\n\t.cookie($S, safeString(data.get($S)))");
                        args.add(key);
                        args.add(key);
                    }
                });
    }
}
