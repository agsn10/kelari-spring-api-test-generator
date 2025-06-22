package io.github.kelari.atg.process.handler.client;

import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;
import io.github.kelari.atg.process.helper.MethodGenerationHelper;
import io.github.kelari.atg.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@code BodyHandler} is an implementation of {@link FluentMethodSpecHandler} that manages the
 * body content and content type of an HTTP request based on the method and parameters.
 * <p>
 * This handler generates the appropriate body and content type for the HTTP request.
 * It checks the HTTP method type and handles scenarios where the body of the request is required.
 * It also handles multipart form data and JSON bodies, depending on the method and parameters.
 * </p>
 * <p>
 * For multipart form data, it adds the {@code contentType} as {@code MULTIPART_FORM_DATA} and uses
 * a method to build the multipart data.
 * </p>
 * <p>
 * For JSON bodies, it sets the {@code contentType} as {@code APPLICATION_JSON} and includes the
 * body values from the test parameters.
 * </p>
 * <p>
 * Example generated output for multipart form data:
 * <pre>{@code
 * .contentType(MediaType.MULTIPART_FORM_DATA)
 * .body(BodyInserters.fromMultipartData(buildMultipartData(data)))
 * }</pre>
 * Example generated output for JSON body:
 * <pre>{@code
 * .contentType(MediaType.APPLICATION_JSON)
 * .bodyValue(formatBody(entry.getKey()))
 * }</pre>
 * </p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class BodyHandler implements FluentMethodSpecHandler {

    /**
     * Handles the body content and content type for the HTTP request based on the method type and parameters.
     * It adds the necessary content type and body values to the statement depending on the HTTP method
     * (e.g., multipart form data or JSON body).
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

        String httpMethod = spec.getHttpMethod().toLowerCase();

        if (MethodGenerationHelper.requiresMultipartFormData(httpMethod, test.getMethodParameters())) {
            statement.append("\n\t.contentType($T.MULTIPART_FORM_DATA)");
            args.add(Constants.Imports.MEDIA_TYPE);
            statement.append("\n\t.body($T.fromMultipartData(buildMultipartData(data)))");
            args.add(Constants.Imports.BODY_INSERTERS);
        } else if (MethodGenerationHelper.requiresBody(httpMethod)) {
            statement.append("\n\t.contentType($T.APPLICATION_JSON)");
            args.add(Constants.Imports.MEDIA_TYPE);

            Optional.ofNullable(test.getMethodParameters())
                    .map(ParameterMetadataTest::getBody)
                    .ifPresent(bodyMap -> {
                        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                            statement.append("\n\t.bodyValue(formatBody($L))");
                            args.add(entry.getKey());
                        }
                    });
        }
    }
}
