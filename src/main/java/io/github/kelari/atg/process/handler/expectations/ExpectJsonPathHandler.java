package io.github.kelari.atg.process.handler.expectations;

import com.squareup.javapoet.ClassName;
import io.github.kelari.atg.annotation.MatcherType;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.JsonPath;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.List;
import java.util.Set;

/**
 * Fluent handler responsible for generating WebTestClient assertions
 * based on JSONPath expressions defined in the test metadata.
 *
 * Supports a variety of matcher types including standard Hamcrest matchers,
 * class instance checks, negations, and custom matcher injection.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class ExpectJsonPathHandler implements FluentMethodSpecHandler {

    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {

        Set<JsonPath> paths = test.getJsonPaths();
        if (paths == null || paths.isEmpty()) return;

        boolean first = true;

        for (JsonPath jsonPath : paths) {
            if (jsonPath.getPath().isBlank() || jsonPath.getType() == null) continue;

            if (first) {
                statement.append("\n\t.expectBody()");
                first = false;
            }

            statement.append("\n\t.jsonPath($S)");
            args.add(jsonPath.getPath());

            MatcherType type = jsonPath.getType();
            String value = jsonPath.getValue();
            String matcherClass = jsonPath.getMatcherClass();

            statement.append(".value(");
            switch (type) {
                case NOT_NULL_VALUE -> {
                    statement.append("$T.notNullValue()");
                    args.add(org.hamcrest.Matchers.class);
                }
                case NULL_VALUE -> {
                    statement.append("$T.nullValue()");
                    args.add(org.hamcrest.Matchers.class);
                }
                case EQUAL_TO -> {
                    statement.append("$T.equalTo($S)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case CONTAINS_STRING -> {
                    statement.append("$T.containsString($S)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case STARTS_WITH -> {
                    statement.append("$T.startsWith($S)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case ENDS_WITH -> {
                    statement.append("$T.endsWith($S)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case GREATER_THAN -> {
                    statement.append("$T.greaterThan($L)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(parseNumber(value));
                }
                case LESS_THAN -> {
                    statement.append("$T.lessThan($L)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(parseNumber(value));
                }
                case INSTANCE_OF -> {
                    try {
                        Class<?> clazz = Class.forName(value);
                        statement.append("$T.instanceOf($T.class)");
                        args.add(org.hamcrest.Matchers.class);
                        args.add(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("Class not found: " + value);
                    }
                }
                case NOT -> {
                    statement.append("$T.not($T.equalTo($S))");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case ANY_OF -> {
                    String[] values = value.split(",");
                    statement.append("$T.anyOf(");
                    args.add(org.hamcrest.Matchers.class);
                    for (int i = 0; i < values.length; i++) {
                        statement.append("$T.equalTo($S)");
                        args.add(org.hamcrest.Matchers.class);
                        args.add(values[i].trim());
                        if (i < values.length - 1) statement.append(", ");
                    }
                    statement.append(")");
                }
                case CONTAINS -> {
                    String[] values = value.split(",");
                    statement.append("$T.contains(");
                    args.add(org.hamcrest.Matchers.class);
                    for (int i = 0; i < values.length; i++) {
                        statement.append("$T.equalTo($S)");
                        args.add(org.hamcrest.Matchers.class);
                        args.add(values[i].trim());
                        if (i < values.length - 1) statement.append(", ");
                    }
                    statement.append(")");
                }
                case HAS_ITEM -> {
                    statement.append("$T.hasItem($S)");
                    args.add(org.hamcrest.Matchers.class);
                    args.add(value);
                }
                case CUSTOM_CLASS -> {
                    if (matcherClass == null)
                        throw new IllegalStateException("CUSTOM_CLASS requires a valid matcherClass.");
                    statement.append("new $T()");
                    args.add(ClassName.bestGuess(matcherClass));
                }
                default -> {
                    statement.append("$T.anything()");
                    args.add(org.hamcrest.Matchers.class);
                }
            }

            statement.append(")");
        }
    }

    private static Number parseNumber(String input) {
        try {
            if (input.contains(".")) return Double.parseDouble(input);
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value: " + input);
        }
    }
}