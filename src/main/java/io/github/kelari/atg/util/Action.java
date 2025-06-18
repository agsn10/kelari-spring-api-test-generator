package io.github.kelari.atg.util;

/**
 * A functional interface representing an action that can be executed on an object of type {@code T}.
 *
 * <p>This interface defines a single method, {@link #execute}, that accepts an object of type {@code T}
 * and performs some action on it. It is typically used in scenarios where you want to encapsulate an
 * operation or action that will be applied to an object of type {@code T}.
 *
 * @param <T> the type of the object that this action will operate on
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved
 *
 */
@FunctionalInterface
public interface Action<T> {

    /**
     * Executes the action on the given object.
     *
     * @param t the object on which the action is executed
     */
    void execute(T t);
}
