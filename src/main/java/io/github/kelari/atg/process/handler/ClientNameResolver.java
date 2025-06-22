package io.github.kelari.atg.process.handler;

import io.github.kelari.atg.model.CaseTest;

/**
 * A utility class responsible for resolving the client name based on the configuration of the {@link CaseTest}.
 * <p>This class determines the appropriate client name to be used in the dynamically generated test methods,
 * based on whether logging is enabled or if a response timeout is configured for the test case.</p>
 *
 * <p>If logging is enabled or a non-zero response timeout is specified, the client name is resolved to "client".
 * Otherwise, it resolves to "webTestClient". This is important for determining which client instance
 * will be used during test execution.</p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a>  [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 * @see CaseTest
 */
public final class ClientNameResolver {

    /**
     * Resolves the client name based on the test case configuration.
     *
     * @param test the {@link CaseTest} containing the test configuration
     * @return a {@code String} representing the resolved client name, either "client" or "webTestClient"
     */
    public static String resolve(CaseTest test){
        return (test.isEnableLogging() || test.getResponseTimeoutSeconds() > 0)
                ? "client"
                : "webTestClient";
    }
}
