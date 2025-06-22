package io.github.kelari.atg.process.handler.client;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.util.Constants;

/**
 * {@code ClientInitializationHandler} is an implementation of {@link MethodSpecHandler} that is responsible for
 * initializing the {@link WebTestClient} based on test-specific configurations, such as logging filters and response
 * timeouts.
 * <p>
 * This handler checks the test case configurations for enabling logging and setting response timeouts. If logging is enabled,
 * it adds filters for logging the request and response. If a response timeout is specified, it adds the corresponding timeout configuration.
 * </p>
 * <p>
 * Example generated output:
 * <pre>{@code
 * this.webTestClient
 *     .mutate()
 *     .filter(logRequest())
 *     .filter(logResponse())
 *     .responseTimeout(Duration.ofSeconds(5))
 *     .build();
 * }</pre>
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class ClientInitializationHandler implements MethodSpecHandler {

    /**
     * Handles the initialization of the {@link WebTestClient} by adding the necessary configurations based on the test case settings.
     * It adds filters for logging the request and response, as well as setting the response timeout if configured.
     *
     * @param builder            the builder used to construct the method specification
     * @param codeBlockBuilder   the builder used to generate the code block for the client initialization
     * @param spec               the specification for the test scenario
     * @param test               the individual test case containing test-specific configurations
     * @param fullPath           the full path of the test (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (test.isEnableLogging() || test.getResponseTimeoutSeconds() > 0) {
            CodeBlock.Builder clientBuilder = CodeBlock.builder();
            clientBuilder.add("this.webTestClient\n\t.mutate()");
            if (test.isEnableLogging()) {
                clientBuilder.add("\n\t.filter(logRequest())")
                        .add("\n\t.filter(logResponse())");
            }
            if (test.getResponseTimeoutSeconds() > 0) {
                clientBuilder.add("\n\t.responseTimeout($T.ofSeconds($L))",
                        Constants.Imports.DURATION, test.getResponseTimeoutSeconds());
            }
            clientBuilder.add("\n\t.build()");
            codeBlockBuilder.addStatement("$T client = $L",
                    Constants.Imports.WEB_TEST_CLIENT, clientBuilder.build());
        }
    }
}
