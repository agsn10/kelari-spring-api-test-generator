package io.github.kelari.atg.process.handler.expectations;

import com.squareup.javapoet.CodeBlock;
import io.github.kelari.atg.model.CaseTest;
import io.github.kelari.atg.model.Header;
import io.github.kelari.atg.model.SpecScenariosTest;
import io.github.kelari.atg.process.handler.FluentMethodSpecHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FluentMethodSpecHandler} responsible for generating
 * expectations for HTTP headers in the response.
 * <p>
 * This handler processes the list of expected headers defined in a {@link CaseTest}
 * and generates Java code to assert that the response contains the expected header
 * names and values.
 *
 * <p>Example generated output:
 * <pre>{@code
 * .expectHeader().valueEquals("X-Custom-Header", "value1", "value2")
 * }</pre>
 *
 * <p>This class uses {@link CodeBlock} to build the comma-separated list of header values
 * for correct code generation via JavaPoet.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class ExpectHeaderHandler implements FluentMethodSpecHandler {

    /**
     * Appends header assertions to the test method's statement builder if
     * any expected headers are defined in the test case.
     *
     * @param statement the builder accumulating the test method body
     * @param args the list of arguments to be used with code templates
     * @param spec the scenario specification being processed
     * @param test the individual test case
     * @param fullPath the full endpoint path (not used directly in this handler)
     */
    @Override
    public void handle(StringBuilder statement,
                       List<Object> args,
                       SpecScenariosTest spec,
                       CaseTest test,
                       String fullPath) {
        if (Objects.nonNull(test.getExpectedHeaders())) {
            for (Header header : test.getExpectedHeaders()) {
                statement.append("\n\t.expectHeader().valueEquals($S, $L)");
                args.add(header.getName());
                args.add(CodeBlock.of("$L", Arrays.stream(header.getValues())
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", "))));
            }
        }
    }
}