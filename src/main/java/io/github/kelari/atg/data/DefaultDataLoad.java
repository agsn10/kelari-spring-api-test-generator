package io.github.kelari.atg.data;

/**
 * Default no-op data provider.
 * Acts as a placeholder when no data is specified.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
public class DefaultDataLoad implements DataLoad {
    @Override
    public java.util.Map<String, Object> load() {
        return java.util.Collections.emptyMap();
    }
}