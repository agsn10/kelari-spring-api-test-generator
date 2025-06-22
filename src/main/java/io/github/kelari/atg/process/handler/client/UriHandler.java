package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;
import io.github.kelari.atg.process.helper.MethodGenerationHelper;

import java.util.List;
import java.util.Optional;

/**
 * {@code UriHandler} is an implementation of {@link FluentMethodSpecHandler} that handles the generation of the
 * URI for the WebTestClient request in a test scenario.
 * <p>
 * It processes the URI expression by taking into account any path parameters, query parameters, and matrix parameters
 * that may be part of the test case. The generated URI expression is then appended to the statement, which is used
 * to build the test code.
 * </p>
 * <p>
 * The generated code includes a call to {@code uri()} with the appropriate URI expression.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class UriHandler implements FluentMethodSpecHandler {

    /**
     * Handles the generation of the URI expression for the test, incorporating any path, query, and matrix parameters
     * specified in the test case. The URI expression is then added to the WebTestClient request statement.
     *
     * @param statement         the {@link StringBuilder} used to build the generated test code
     * @param args             the list of arguments that are added to the statement (not used here)
     * @param spec             the specification for the test scenario
     * @param test             the individual test case containing the URI parameters
     * @param fullPath         the full path of the test, which is used to prepare the URI expression
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        String uriExpr = MethodGenerationHelper.prepareUriExpression(
                fullPath,
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getPathParams).orElse(null),
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getQueryParams).orElse(null),
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getMatrixParams).orElse(null)
        );
        statement.append("\n\t.uri(").append(uriExpr).append(")");
    }
}