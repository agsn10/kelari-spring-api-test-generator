package io.github.kelari.atg.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kelari.atg.data.DataLoad;

import java.util.Collections;
import java.util.Map;

/**
 * Utility class for handling test data loading and JSON body formatting.
 * <p>
 * This class provides helper methods used during the automatic test generation
 * process by the Kelari library.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * Map<String, Object> data = DataLoadUtil.getData("com.example.data.MyDataProvider");
 * String json = DataLoadUtil.formatBody(myObject);
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public final class DataLoadUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private DataLoadUtil() {}

    /**
     * Dynamically instantiates a class provided by its fully qualified name and invokes
     * the {@code load()} method if the class implements {@link DataLoad}.
     *
     * @param className the fully qualified name of the class implementing {@link DataLoad}
     * @return a {@code Map<String, Object>} containing the data returned by the implementation
     * @throws IllegalArgumentException if the class does not implement {@link DataLoad}
     */
    public static Map<String, Object> getData(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof DataLoad)
                return ((DataLoad) instance).load();
            else
                throw new IllegalArgumentException("Class " + className + " does not implement DataLoad");
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    /**
     * Serializes any given object into a JSON string.
     *
     * @param body the object to be serialized
     * @return the JSON string representation of the object
     * @throws RuntimeException if serialization fails
     */
    public static String formatBody(Object body) {
        String json = "";
        try {
            json = new ObjectMapper().writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public static String safeString(Object value) {
        return value != null ? value.toString() : "";
    }

}