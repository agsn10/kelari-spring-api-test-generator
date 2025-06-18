package io.github.kelari.atg.data;

import java.util.Map;

/**
 * Functional interface for loading external or synthetic data
 * required during test execution or dynamic test generation.
 * <p>
 * Implementations of this interface provide a data map
 * that can be injected into test methods or used during
 * the test class generation process.
 * </p>
 *
 * <p>Typical use cases include:</p>
 * <ul>
 *   <li>Loading static fixture data</li>
 *   <li>Generating dynamic parameters for parameterized tests</li>
 *   <li>Integrating external services or configuration values into tests</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * public class MyCustomDataLoader implements DataLoad {
 *     @Override
 *     public Map<String, Object> load() {
 *         return Map.of("username", "john.doe", "age", 30);
 *     }
 * }
 * }</pre>
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public interface DataLoad {

    /**
     * Loads a map of key-value pairs representing named data
     * to be used in generated or dynamic test scenarios.
     *
     * @return a map of data to inject or use during testing
     */
    Map<String, Object> load();
}
