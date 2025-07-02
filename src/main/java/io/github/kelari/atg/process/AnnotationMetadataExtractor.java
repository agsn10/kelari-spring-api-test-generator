package io.github.kelari.atg.process;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;


/**
 * Utility class for extracting metadata from Java annotation elements
 * during annotation processing.
 *
 * <p>This class is primarily designed to support annotation processors
 * by simplifying the retrieval of values from {@link AnnotationMirror}
 * objects—especially those values associated with attributes like {@code value()}
 * commonly found in Spring-style annotations such as {@code @RequestMapping},
 * {@code @GetMapping}, {@code @PostMapping}, etc.</p>
 *
 * <p>Example use case:</p>
 * <pre>{@code
 * for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
 *     if (isRequestMapping(annotation)) {
 *         String uri = AnnotationMetadataExtractor.extractUri(annotation);
 *         ...
 *     }
 * }
 * }</pre>
 *
 * <p>This helper is intended for internal use during compile-time
 * annotation processing and assumes the annotation values are well-formed.</p>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public final class AnnotationMetadataExtractor {

    /**
     * Private constructor to prevent instantiation.
     */
    private AnnotationMetadataExtractor() {}

    /**
     * Extracts the first URI string from the {@code value()} element
     * of the given annotation.
     *
     * <p>This method handles both single {@code String} values and
     * {@code String[]} array-like values wrapped in a {@code List<AnnotationValue>}.
     * It returns the first entry as a sanitized URI string.</p>
     *
     * @param am the {@link AnnotationMirror} from which to extract the URI
     * @return the URI string if present, or an empty string if not found
     */
    public static String extractUri(AnnotationMirror am) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals("value")) {
                Object value = entry.getValue().getValue();
                if (value instanceof List) {
                    List<? extends AnnotationValue> values = (List<? extends AnnotationValue>) value;
                    if (!values.isEmpty())
                        return sanitizeUri(values.get(0).getValue().toString());
                } else if (value instanceof String)
                    return sanitizeUri((String) value);
            }
        }
        return "";
    }

    /**
     * Sanitizes the extracted URI by removing surrounding double quotes,
     * if they exist.
     *
     * @param uri the raw URI string (possibly quoted)
     * @return the sanitized URI
     */
    private static String sanitizeUri(String uri) {
        if (uri.startsWith("\"") && uri.endsWith("\""))
            uri = uri.substring(1, uri.length() - 1);
        return uri;
    }
}