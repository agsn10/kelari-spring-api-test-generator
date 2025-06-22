package io.github.kelari.atg.process.handler.expectations;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.Cookie;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link FluentMethodSpecHandler} that generates
 * test expectations for HTTP cookies returned in the response.
 * <p>
 * This handler processes the list of expected cookies defined in the
 * {@link CaseTest} and appends appropriate `.expectCookie().valueEquals(...)`
 * statements to the generated test method.
 *
 * <p>Example generated output:
 * <pre>{@code
 * .expectCookie().valueEquals("sessionId", "abc123")
 * }</pre>
 *
 * This helps to assert that a cookie with the given name and value
 * is present in the HTTP response.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class ExpectCookieHandler implements FluentMethodSpecHandler {

    /**
     * Appends cookie value assertions to the given statement builder if any
     * expected cookies are present in the test case.
     *
     * @param statement the StringBuilder where the test method is being composed
     * @param args the list of arguments (used for templating in code generation)
     * @param spec the specification containing all scenarios
     * @param test the individual test case being processed
     * @param fullPath the full endpoint path being tested (unused here)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (Objects.nonNull(test.getExpectedCookies())) {
            for (Cookie cookie : test.getExpectedCookies()) {
                statement.append("\n\t.expectCookie().valueEquals($S, $S)");
                args.add(cookie.getName());
                args.add(cookie.getValue());
            }
        }
    }
}
