package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;

/**
 * {@code ExchangeHandler} is an implementation of {@link FluentMethodSpecHandler} responsible for handling the HTTP
 * exchange statement and the expected status code in a test scenario.
 * <p>
 * This handler processes the expected HTTP status code for the given test case and generates the corresponding
 * code for the WebTestClient's `exchange()` method, which performs the HTTP request and asserts the status code.
 * </p>
 * <p>
 * The {@code expectMethod} can be configured to use different expectation types such as "DEFAULT" or others.
 * If the {@code expectMethod} is set to "DEFAULT", the handler will add the expected status code to the statement.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class ExchangeHandler implements FluentMethodSpecHandler {

    private String expectMethod;

    /**
     * Constructor for the {@code ExchangeHandler} that sets the expectation method for the status code.
     *
     * @param expectMethod the expectation method (e.g., "DEFAULT" or others) used to assert the expected status code
     */
    public ExchangeHandler(String expectMethod) {
        this.expectMethod = expectMethod;
    }

    /**
     * Handles the creation of the HTTP exchange statement for the test, asserting the expected status code.
     * This method generates the code for WebTestClient's {@code exchange()} method with an expected status code check.
     *
     * @param statement         the {@link StringBuilder} used to build the generated test code
     * @param args             the list of arguments that are added to the statement (e.g., status code, methods)
     * @param spec             the specification for the test scenario
     * @param test             the individual test case containing the expected status code
     * @param fullPath         the full path of the test (not used here)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {

        int statusCode = test.getExpectedStatusCode();
        statement.append("\n\t.exchange()\n\t.expectStatus().$L");
        args.add(this.expectMethod);
        if ("DEFAULT".equals(this.expectMethod)) {
            statement.append("($L)");
            args.add(statusCode);
        } else {
            statement.append("()");
        }
    }
}