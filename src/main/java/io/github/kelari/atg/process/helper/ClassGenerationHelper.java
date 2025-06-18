package io.github.kelari.atg.process.helper;

import com.squareup.javapoet.*;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.ParameterMetadataTest;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.util.Constants;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.function.Function;

/**
 * Helper class responsible for generating test methods dynamically using JavaPoet,
 * particularly for WebTestClient-based integration tests. It provides utilities for
 * generating authentication methods, endpoint tests, and multipart data construction.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public class ClassGenerationHelper {

    private static final Map<Integer, String> STATUS_METHODS = createStatusMethodsMap();

    private static Map<Integer, String> createStatusMethodsMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(200, Constants.StatusMethods.IS_OK);
        map.put(201, Constants.StatusMethods.IS_CREATED);
        map.put(204, Constants.StatusMethods.IS_NO_CONTENT);
        map.put(400, Constants.StatusMethods.IS_BAD_REQUEST);
        map.put(401, Constants.StatusMethods.IS_UNAUTHORIZED);
        map.put(403, Constants.StatusMethods.IS_FORBIDDEN);
        map.put(404, Constants.StatusMethods.IS_NOT_FOUND);
        map.put(500, Constants.StatusMethods.IS_5xx_SERVER_ERROR);
        return Collections.unmodifiableMap(map);
    }

    private static final Function<Integer, String> EXPECT_STATUS_METHOD =
            code -> STATUS_METHODS.getOrDefault(code, Constants.StatusMethods.DEFAULT);

    /**
     * Generates a JUnit {@code @BeforeEach} method for performing authentication
     * before each test, using WebTestClient.
     *
     * @param authUrl             the authentication endpoint URL
     * @param username            the username to use for authentication
     * @param password            the password to use for authentication
     * @param parameterTokenName  the JSON path name of the token in the response
     * @return a {@link MethodSpec} representing the generated authentication method
     */
    public static MethodSpec generateAuthBeforeEachMethod(String authUrl, String username, String password, String parameterTokenName) {
        return MethodSpec.methodBuilder("authenticate")
                .addAnnotation(Constants.Imports.BEFORE_EACH)
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.builder()
                        .beginControlFlow("if ($S.isEmpty())", authUrl)
                        .addStatement("return")
                        .endControlFlow()
                        .addStatement("$T<String, String> credentials = new $T<>()", Map.class, HashMap.class)
                        .addStatement("credentials.put($S, $S)", "username", username)
                        .addStatement("credentials.put($S, $S)", "password", password)
                        .addStatement("webTestClient.post()\n" +
                                        "               .uri($S)\n" +
                                        "               .contentType($T.APPLICATION_JSON)\n" +
                                        "               .body($T.just(credentials), $T.class)\n" +
                                        "               .exchange()\n" +
                                        "               .expectStatus()\n" +
                                        "               .is2xxSuccessful()\n" +
                                        "               .expectBody()\n" +
                                        "               .jsonPath($S)\n" +
                                        "               .value(token -> bearerToken = $S + token)",
                                authUrl,
                                Constants.Imports.MEDIA_TYPE,
                                Constants.Imports.MONO,
                                Constants.Imports.MAP,
                                "$." + parameterTokenName, "Bearer ")
                        .build())
                .build();
    }

    /**
     * Generates a test method for a given {@link SpecScenariosTest} and {@link CaseTest},
     * constructing the WebTestClient invocation dynamically based on HTTP method, expected
     * status, and parameter metadata.
     *
     * @param spec      the specification scenario describing the endpoint and method
     * @param test      the test case containing expected results and parameter values
     * @param fullPath  the full URI path of the endpoint to be tested
     * @return a {@link MethodSpec} representing the dynamically generated test method
     */
    public static MethodSpec generateTestMethod(SpecScenariosTest spec, CaseTest test, String fullPath) {
        String httpMethod = spec.getHttpMethod().toLowerCase();
        int statusCode = test.getExpectedStatusCode();
        String expectMethod = EXPECT_STATUS_METHOD.apply(statusCode);
        String methodName = String.format("%s_%d", spec.getMethodName(), statusCode);
        MethodSpec.Builder builder = MethodSpec.methodBuilder(methodName)
                .addAnnotation(Constants.Imports.TEST)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
        if (test.getOrder() > 0) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.ORDER)
                    .addMember("value", "$L", test.getOrder())
                    .build());
        }
        if (test.getTimeout() > 0) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.TIMEOUT)
                    .addMember("value", "$L", test.getTimeout())
                    .build());
        }
        if (!test.getDisplayName().isEmpty() || Objects.nonNull(test.getDisplayName())) {
            builder.addAnnotation(AnnotationSpec.builder(Constants.Imports.DISPLAY_NAME)
                    .addMember("value", "$S", test.getDisplayName())
                    .build());
        }
        // Start of code block
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        // Date statement
        codeBlockBuilder.addStatement("$T<String, Object> data = getData($S)", Constants.Imports.MAP, test.getDataProviderClassName());
        // Declaration of body objects (if any)
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getBody)
                .ifPresent(bodyMap -> {
                    // Apenas gera os casts se NÃO for multipart/form-data
                    if (MethodGenerationHelper.requiresBody(httpMethod)
                            && !MethodGenerationHelper.requiresMultipartFormData(httpMethod, test.getMethodParameters())) {
                        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                            String bodyVarName = entry.getKey();
                            String bodyClass = entry.getValue();
                            codeBlockBuilder.addStatement(
                                    "$L $L = ($L) data.get($S)",
                                    bodyClass, bodyVarName, bodyClass, bodyVarName
                            );
                        }
                    }
                });
        // webTestClient + fluent call
        StringBuilder statement = new StringBuilder("webTestClient\n\t.$L()");
        List<Object> args = new ArrayList<>();
        args.add(httpMethod);
        // URI
        String uriExpr = MethodGenerationHelper.prepareUriExpression(
                fullPath,
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getPathParams).orElse(null),
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getQueryParams).orElse(null),
                Optional.ofNullable(test.getMethodParameters()).map(ParameterMetadataTest::getMatrixParams).orElse(null)
        );
        statement.append("\n\t.uri(").append(uriExpr).append(")");
        // Headers
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getHeaderParams)
                .ifPresent(params -> {
                    for (String key : params.keySet()) {
                        statement.append("\n\t.header($S, safeString(data.get($S)))");
                        args.add(key);
                        args.add(key);
                    }
                });
        if (test.isRequiresAuth()) {
            statement.append("\n\t.header($S, $L)");
            args.add("Authorization");
            args.add("bearerToken");
        }
        // Cookies
        Optional.ofNullable(test.getMethodParameters())
                .map(ParameterMetadataTest::getCookieParams)
                .ifPresent(params -> {
                    for (String key : params.keySet()) {
                        statement.append("\n\t.cookie($S, safeString(data.get($S)))");
                        args.add(key);
                        args.add(key);
                    }
                });
        // Body (multipart ou JSON)
        if (MethodGenerationHelper.requiresMultipartFormData(httpMethod, test.getMethodParameters())) {
            statement.append("\n\t.contentType($T.MULTIPART_FORM_DATA)");
            args.add(Constants.Imports.MEDIA_TYPE);
            // Support buildMultipartData with full map
            statement.append("\n\t.body($T.fromMultipartData(buildMultipartData(data)))");
            args.add(Constants.Imports.BODY_INSERTERS);
        } else if (MethodGenerationHelper.requiresBody(httpMethod)) {
            statement.append("\n\t.contentType($T.APPLICATION_JSON)");
            args.add(Constants.Imports.MEDIA_TYPE);
            Optional.ofNullable(test.getMethodParameters())
                    .map(ParameterMetadataTest::getBody)
                    .ifPresent(bodyMap -> {
                        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                            String bodyVarName = entry.getKey();
                            // NOTE: if the value is a complex object (e.g. UploadRequest),
                            // it needs to be converted to JSON (via formatBody)
                            statement.append("\n\t.bodyValue(formatBody($L))");
                            args.add(bodyVarName);
                        }
                    });
        }
        // End of thread
        statement.append("\n\t.exchange()\n\t.expectStatus().$L");
        args.add(expectMethod);
        if ("DEFAULT".equals(expectMethod)) {
            statement.append("($L)");
            args.add(statusCode);
        } else
            statement.append("()");
        // Add chaining to the block
        codeBlockBuilder.addStatement(statement.toString(), args.toArray());
        // Add the block to the method
        builder.addCode(codeBlockBuilder.build());

        return builder.build();
    }

    public static MethodSpec generateBuildMultipartDataMethod() {
        ClassName multiValueMap = ClassName.get("org.springframework.util", "MultiValueMap");
        ClassName httpEntity = ClassName.get("org.springframework.http", "HttpEntity");
        ClassName linkedMultiValueMap = ClassName.get("org.springframework.util", "LinkedMultiValueMap");
        ClassName httpHeaders = ClassName.get("org.springframework.http", "HttpHeaders");
        ClassName resourceClass = ClassName.get("org.springframework.core.io", "Resource");
        ClassName mediaType = ClassName.get("org.springframework.http", "MediaType");

        // Método buildMultipartData
        return  MethodSpec.methodBuilder("buildMultipartData")
                .addModifiers(Modifier.PRIVATE)
                .returns(ParameterizedTypeName.get(multiValueMap, ClassName.get(String.class), ParameterizedTypeName.get(httpEntity, WildcardTypeName.subtypeOf(Object.class))))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Object.class)), "data")
                .addCode(CodeBlock.builder()
                        .addStatement("$T<String, $T<?>> body = new $T<>()", multiValueMap, httpEntity, linkedMultiValueMap)
                        .beginControlFlow("for ($T.Entry<String, Object> entry : data.entrySet())", Map.class)
                        .addStatement("String key = entry.getKey()")
                        .addStatement("Object value = entry.getValue()")
                        .addStatement("$T headers = new $T()", httpHeaders, httpHeaders)
                        .beginControlFlow("if (value instanceof $T || value instanceof byte[])", resourceClass)
                        .addStatement("headers.setContentType($T.MULTIPART_FORM_DATA)", mediaType)
                        .addStatement("$T<?> part = new $T<>(value, headers)", httpEntity, httpEntity)
                        .addStatement("body.add(key, part)")
                        .nextControlFlow("else")
                        .addStatement("headers.setContentType($T.TEXT_PLAIN)", mediaType)
                        .addStatement("$T<String> part = new $T<>(safeString(value), headers)", httpEntity, httpEntity)
                        .addStatement("body.add(key, part)")
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("return body")
                        .build())
                .build();
    }
}