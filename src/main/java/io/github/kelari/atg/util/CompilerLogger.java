package io.github.kelari.atg.util;

import javax.tools.Diagnostic;

/**
 * {@code CompilerLogger} is a functional interface that defines methods for logging messages
 * with different severity levels: {@link Diagnostic.Kind#NOTE}, {@link Diagnostic.Kind#WARNING},
 * and {@link Diagnostic.Kind#ERROR}.
 *
 * <p>This interface is used to capture and log messages during the compilation process,
 * providing an abstraction to customize how logs are generated.
 *
 * <p>In addition to the main method {@link #log(Diagnostic.Kind, String)}, there are helper methods
 * for logging messages with the {@code NOTE}, {@code WARNING}, and {@code ERROR} levels.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 */
@FunctionalInterface
public interface CompilerLogger {

    /**
     * Logs a message with the specified diagnostic kind.
     *
     * @param kind the type of diagnostic (such as {@link Diagnostic.Kind#NOTE},
     *             {@link Diagnostic.Kind#WARNING}, or {@link Diagnostic.Kind#ERROR})
     * @param message the log message to be recorded
     */
    void log(Diagnostic.Kind kind, String message);

    /**
     * Logs a message with the {@code NOTE} diagnostic kind.
     *
     * @param message the log message to be recorded
     */
    default void note(String message) {
        log(Diagnostic.Kind.NOTE, message);
    }

    /**
     * Logs a message with the {@code WARNING} diagnostic kind.
     *
     * @param message the log message to be recorded
     */
    default void warning(String message) {
        log(Diagnostic.Kind.WARNING, message);
    }

    /**
     * Logs a message with the {@code ERROR} diagnostic kind.
     *
     * @param message the log message to be recorded
     */
    default void error(String message) {
        log(Diagnostic.Kind.ERROR, message);
    }
}