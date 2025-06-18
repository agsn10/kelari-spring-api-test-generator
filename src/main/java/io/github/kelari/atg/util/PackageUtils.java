package io.github.kelari.atg.util;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Utility class for operations related to Java package names.
 * <p>
 * This class provides a method to sanitize package names by removing
 * duplicate segments while preserving their original order.
 * </p>
 *
 * <p>Example:</p>
 * <pre>
 *     sanitizePackageName("com.example.example.utils")
 *     // returns "com.example.utils"
 * </pre>
 *
 * This class cannot be instantiated.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public final class PackageUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private PackageUtils() {}

    /**
     * Sanitizes a raw package name by removing duplicate segments while
     * preserving their original order.
     *
     * @param rawPackage the raw package name string (e.g., "com.example.example.utils")
     * @return a sanitized package name with duplicates removed (e.g., "com.example.utils")
     */
    public static String sanitizePackageName(String rawPackage) {
        String[] parts = rawPackage.split("\\.");
        LinkedHashSet<String> uniqueParts = new LinkedHashSet<>(Arrays.asList(parts));
        return String.join(".", uniqueParts);
    }
}