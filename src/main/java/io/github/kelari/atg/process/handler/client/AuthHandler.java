package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;

/**
 * {@code AuthHandler} is an implementation of {@link FluentMethodSpecHandler} that adds the
 * {@code Authorization} header to the HTTP request if the test case requires authentication.
 * <p>
 * This handler checks if the test case {@link CaseTest#isRequiresAuth()} is {@code true}, and if so,
 * it appends an authorization header with a "bearer" token to the HTTP request. The token value
 * used in this case is a placeholder string {@code "bearerToken"}.
 * </p>
 * Example generated output when authentication is required:
 * <pre>{@code
 * .header("Authorization", "bearerToken")
 * }</pre>
 * <p>
 * If the test case does not require authentication, this handler does nothing.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class AuthHandler implements FluentMethodSpecHandler {

    /**
     * Adds the {@code Authorization} header with a bearer token to the HTTP request
     * if authentication is required by the test case.
     *
     * @param statement the statement being constructed for the HTTP request
     * @param args      the arguments to be inserted into the statement
     * @param spec      the specification for the test scenario
     * @param test      the individual test case
     * @param fullPath  the full path of the test (not used here)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {

        if (test.isRequiresAuth()) {
            statement.append("\n\t.header($S, $L)");
            args.add("Authorization");
            args.add("bearerToken");
        }
    }
}
