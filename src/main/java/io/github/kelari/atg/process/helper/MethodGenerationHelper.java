package io.github.kelari.atg.process.helper;

import io.github.kelari.atg.model.ParameterMetadataTest;

import java.util.Arrays;
import java.util.Map;

/**
 * Utility class for supporting method generation logic related to HTTP requests,
 * such as determining body requirements, handling multipart form data, and constructing URIs.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public final class MethodGenerationHelper {

    private MethodGenerationHelper() {}

    /**
     * Determines if the given HTTP method typically requires a request body.
     *
     * @param httpMethod the HTTP method name (e.g., "GET", "POST")
     * @return {@code true} if the method is POST, PUT, or PATCH; {@code false} otherwise
     */
    public static boolean requiresBody(String httpMethod) {
        return Arrays.asList("post", "put", "patch").contains(httpMethod.toLowerCase());
    }

    /**
     * Determines if a request using the given HTTP method and parameters requires
     * a multipart/form-data content type.
     *
     * @param httpMethod the HTTP method name (e.g., "POST", "PUT")
     * @param params the parameter metadata used to inspect body content
     * @return {@code true} if multipart is required based on body content; {@code false} otherwise
     */
    public static boolean requiresMultipartFormData(String httpMethod, ParameterMetadataTest params) {
        if (!"post".equalsIgnoreCase(httpMethod) && !"put".equalsIgnoreCase(httpMethod))
            return false;
        if (params == null)
            return false;
        return params.isMultipart() || params.getBody().values().stream()
                .anyMatch(value -> {
                    String className = String.valueOf(value);
                    return className.contains("MultipartFile") || className.contains("Resource") || className.contains("byte[]");
                });
    }

    /**
     * Replaces path variables in the given path template with matrix parameter segments.
     *
     * @param pathTemplate the URI path template containing placeholders (e.g., "/products/{filters}")
     * @param matrixParams a map where each key is a path variable name and its value is a map of matrix parameters
     * @return the path template with replaced matrix parameter segments
     */
    public static String generatePathWithMatrixParams(String pathTemplate, Map<String, Map<String, String>> matrixParams) {
        if (matrixParams == null || matrixParams.isEmpty()) return pathTemplate;
        for (Map.Entry<String, Map<String, String>> entry : matrixParams.entrySet()) {
            String pathVar = entry.getKey(); // e.g., "filters"
            Map<String, String> params = entry.getValue(); // e.g., {color=..., size=...}
            StringBuilder segment = new StringBuilder(pathVar);
            for (Map.Entry<String, String> paramEntry : params.entrySet()) {
                segment.append(";")
                        .append(paramEntry.getKey())
                        .append("=\" + safeString(data.get(\"")
                        .append(paramEntry.getKey())
                        .append("\")) + \"");
            }
            pathTemplate = pathTemplate.replace("{" + pathVar + "}", segment.toString());
        }

        return pathTemplate;
    }

    /**
     * Builds a complete URI expression by processing path parameters, query parameters,
     * and matrix parameters into a concatenated string suitable for runtime evaluation.
     *
     * @param pathTemplate the URI path template (e.g., "/items/{category}")
     * @param pathParams a map of path parameters to be replaced
     * @param queryParams a map of query parameters to be appended
     * @param matrixParams a map of matrix parameter groups for specific path variables
     * @return the final URI expression as a Java string with concatenations for runtime value injection
     */
    public static String prepareUriExpression(
            String pathTemplate,
            Map<String, String> pathParams,
            Map<String, String> queryParams,
            Map<String, Map<String, String>> matrixParams
    ) {
        // First apply Matrix Variables to {pathVar}
        pathTemplate = generatePathWithMatrixParams(pathTemplate, matrixParams);
        // Starts with the string in quotes
        StringBuilder uriExpr = new StringBuilder("\"" + pathTemplate + "\"");
        // Replace {param} in path with concatenations with safeString(data.get("param"))
        if (pathParams != null && !pathParams.isEmpty()) {
            for (Map.Entry<String, String> entry : pathParams.entrySet()) {
                String key = entry.getKey();
                int start = uriExpr.indexOf("{" + key + "}");
                if (start != -1) {
                    int end = start + key.length() + 2;
                    uriExpr.replace(start, end, "\" + safeString(data.get(\"" + key + "\")) + \"");
                }
            }
        }
        // Add query parameters with safe concatenation
        if (queryParams != null && !queryParams.isEmpty()) {
            uriExpr.append(" + \"?");
            boolean first = true;
            for (String key : queryParams.keySet()) {
                if (!first)
                    uriExpr.append("&");
                uriExpr.append(key).append("=\" + safeString(data.get(\"").append(key).append("\"))");
                first = false;
            }
        }
        // Final cleaning
        String finalExpr = uriExpr.toString()
                .replace("+ \"\"", "")
                .replace("\"\" +", "");
        if (finalExpr.endsWith("+ "))
            finalExpr = finalExpr.substring(0, finalExpr.length() - 2);
        return finalExpr;
    }
}