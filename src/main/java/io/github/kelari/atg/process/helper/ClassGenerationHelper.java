package io.github.kelari.atg.process.helper;

import com.squareup.javapoet.*;
import io.github.kelari.atg.model.*;
import io.github.kelari.atg.process.handler.ClientNameResolver;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandlerChain;
import io.github.kelari.atg.process.handler.MethodSpecHandlerChain;
import io.github.kelari.atg.process.handler.annotations.DisplayNameHandler;
import io.github.kelari.atg.process.handler.annotations.OrderHandler;
import io.github.kelari.atg.process.handler.annotations.RepeatHandler;
import io.github.kelari.atg.process.handler.annotations.TimeoutHandler;
import io.github.kelari.atg.process.handler.client.*;
import io.github.kelari.atg.process.handler.expectations.ExpectCookieHandler;
import io.github.kelari.atg.process.handler.expectations.ExpectHeaderHandler;
import io.github.kelari.atg.process.handler.expectations.ExpectJsonPathHandler;
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
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);

        CodeBlock.Builder codeBlock = CodeBlock.builder();
        new MethodSpecHandlerChain()
                .add(RepeatHandler::new)
                .add(TimeoutHandler::new)
                .add(OrderHandler::new)
                .add(DisplayNameHandler::new)
                .add(ClientInitializationHandler::new)
                .add(DataLoadHandler::new)
                .applyAll(builder, codeBlock, spec, test, fullPath);

        String clientName = ClientNameResolver.resolve(test);

        StringBuilder statement = new StringBuilder(clientName + "\n\t.$L()");
        List<Object> args = new ArrayList<>();
        args.add(httpMethod);

        new FluentMethodSpecHandlerChain()
                .add(UriHandler::new)
                .add(HeaderHandler::new)
                .add(AuthHandler::new)
                .add(CookieHandler::new)
                .add(BodyHandler::new)
                .add(() -> new ExchangeHandler(expectMethod))
                .add(ExpectCookieHandler::new)
                .add(ExpectHeaderHandler::new)
                .add(ExpectJsonPathHandler::new)
                .applyAll(statement, args, spec, test, fullPath);

        codeBlock.addStatement(statement.toString(), args.toArray());
        builder.addCode(codeBlock.build());

        return builder.build();
    }

    /**
     * Generates a method for building multipart data for HTTP requests.
     *
     * <p>This method generates a multipart data map for the WebTestClient requests.
     * It supports various types of data, including files and plain text.</p>
     *
     * @return a {@link MethodSpec} representing the generated multipart data builder method.
     */
    public static MethodSpec generateBuildMultipartDataMethod() {
        return  MethodSpec.methodBuilder("buildMultipartData")
                .addModifiers(Modifier.PRIVATE)
                .returns(ParameterizedTypeName.get(Constants.Imports.MULTI_VALUE_MAP, ClassName.get(String.class), ParameterizedTypeName.get(Constants.Imports.HTTP_ENTITY, WildcardTypeName.subtypeOf(Object.class))))
                .addParameter(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(String.class), ClassName.get(Object.class)), "data")
                .addCode(CodeBlock.builder()
                        .addStatement("$T<String, $T<?>> body = new $T<>()", Constants.Imports.MULTI_VALUE_MAP, Constants.Imports.HTTP_ENTITY, Constants.Imports.LINKED_MULTI_VALUE_MAP)
                        .beginControlFlow("for ($T.Entry<String, Object> entry : data.entrySet())", Map.class)
                        .addStatement("String key = entry.getKey()")
                        .addStatement("Object value = entry.getValue()")
                        .addStatement("$T headers = new $T()", Constants.Imports.HTTP_HEADERS, Constants.Imports.HTTP_HEADERS)
                        .beginControlFlow("if (value instanceof $T || value instanceof byte[])", Constants.Imports.RESOURCE_CLASS)
                        .addStatement("headers.setContentType($T.MULTIPART_FORM_DATA)", Constants.Imports.MEDIA_TYPE)
                        .addStatement("$T<?> part = new $T<>(value, headers)", Constants.Imports.HTTP_ENTITY, Constants.Imports.HTTP_ENTITY)
                        .addStatement("body.add(key, part)")
                        .nextControlFlow("else")
                        .addStatement("headers.setContentType($T.TEXT_PLAIN)", Constants.Imports.MEDIA_TYPE)
                        .addStatement("$T<String> part = new $T<>(safeString(value), headers)", Constants.Imports.HTTP_ENTITY, Constants.Imports.HTTP_ENTITY)
                        .addStatement("body.add(key, part)")
                        .endControlFlow()
                        .endControlFlow()
                        .addStatement("return body")
                        .build())
                .build();
    }

    /**
     * Generates a logging method for WebTestClient requests.
     *
     * <p>This method logs the request details including headers and cookies for each request made with WebTestClient.</p>
     *
     * @return a {@link MethodSpec} representing the generated logRequest method.
     */
    public static MethodSpec generateLogRequestMethod() {
        return MethodSpec.methodBuilder("logRequest")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(Constants.Imports.EXCHANGE_FILTER_FUNCTION)
                .addCode(CodeBlock.builder()
                        .addStatement("return $T.ofRequestProcessor(request -> {\n" +
                                        "    System.out.println(\"[REQUEST] → \" + request.method() + \" \" + request.url());\n" +
                                        "    request.headers().forEach((k, v) -> System.out.println(\"[HEADER] \" + k + \": \" + v));\n" +
                                        "    request.cookies().forEach((k, v) -> System.out.println(\"[COOKIE] \" + k + \": \" + v));\n" +
                                        "    // [BODY] Logging not implemented. Next release.\n" +
                                        "    return $T.just(request);\n" +
                                        "})",
                                Constants.Imports.EXCHANGE_FILTER_FUNCTION,
                                Constants.Imports.MONO)
                        .build())
                .build();
    }

    /**
     * Generates a logging method for WebTestClient responses.
     *
     * <p>This method logs the response details including status code and headers for each response received.</p>
     *
     * @return a {@link MethodSpec} representing the generated logResponse method.
     */
    public static MethodSpec generateLogResponseMethod() {
        return MethodSpec.methodBuilder("logResponse")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(Constants.Imports.EXCHANGE_FILTER_FUNCTION)
                .addCode(CodeBlock.builder()
                        .addStatement("return $T.ofResponseProcessor(response -> {\n" +
                                "    System.out.println(\"[RESPONSE] ← Status: \" + response.statusCode());\n" +
                                "    response.headers().asHttpHeaders()\n" +
                                "        .forEach((k, v) -> System.out.println(\"[HEADER] \" + k + \": \" + v));\n" +
                                "    return $T.just(response);\n" +
                                "})", Constants.Imports.EXCHANGE_FILTER_FUNCTION, Constants.Imports.MONO)
                        .build())
                .build();
    }
}