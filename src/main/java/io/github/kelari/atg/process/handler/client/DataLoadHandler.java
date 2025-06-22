package io.github.kelari.atg.process.handler.client;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.MethodSpecHandler;
import io.github.kelari.atg.process.helper.MethodGenerationHelper;
import io.github.kelari.atg.util.Constants;

import java.util.Map;
import java.util.Optional;

/**
 * {@code DataLoadHandler} is an implementation of {@link MethodSpecHandler} responsible for loading data from a specified
 * data provider class and assigning values from the data to the test case's method parameters.
 * <p>
 * This handler processes the data for each test case by retrieving it from the data provider class,
 * and then assigns the corresponding values to the method parameters for API request handling.
 * </p>
 * <p>
 * The handler supports body parameters that are passed as part of the request, ensuring that the necessary data is available
 * for the HTTP request in the generated test code.
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class DataLoadHandler implements MethodSpecHandler {

    /**
     * Handles the data loading process by retrieving data from the specified data provider class and assigning it to the
     * appropriate test method parameters.
     *
     * @param builder            the {@link MethodSpec.Builder} used to build the test method
     * @param codeBlockBuilder   the {@link CodeBlock.Builder} used to add the generated code blocks
     * @param spec               the specification for the test scenario
     * @param test               the individual test case containing method parameters and data provider class information
     * @param fullPath           the full path of the test (not used here)
     */
    @Override
    public void handle(MethodSpec.Builder builder,
                       CodeBlock.Builder codeBlockBuilder,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        codeBlockBuilder.addStatement(
                "$T<String, Object> data = getData($S)",
                Constants.Imports.MAP,
                test.getDataProviderClassName()
        );
        String httpMethod = spec.getHttpMethod().toLowerCase();
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getBody)
                .ifPresent(bodyMap -> {
                    if (MethodGenerationHelper.requiresBody(httpMethod)
                            && !MethodGenerationHelper.requiresMultipartFormData(httpMethod, test.getMethodParameters())) {
                        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                            // Assign the body values to the method parameters
                            codeBlockBuilder.addStatement(
                                    "$L $L = ($L) data.get($S)",
                                    entry.getValue(), entry.getKey(), entry.getValue(), entry.getKey()
                            );
                        }
                    }
                });
    }
}
