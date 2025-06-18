package io.github.kelari.atg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * A utility class that allows chaining conditional actions to be executed
 * based on matching predicates against an input value.
 * <p>
 * This can be used as a lightweight alternative to complex `if-else` or `switch` statements,
 * providing a more declarative style for handling conditional logic.
 *
 * @param <T> the type of the input data to be evaluated.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public class ConditionPipeline<T> {

    private final List<ConditionalAction<T>> conditionalActions = new ArrayList<>();
    private Action<T> defaultAction = t -> {}; // default: no action

    /**
     * Adds a condition and the corresponding action to the pipeline.
     * The action will be executed if the predicate evaluates to {@code true}.
     *
     * @param condition the condition to evaluate
     * @param action    the action to perform if the condition is true
     * @return the updated pipeline for fluent chaining
     */
    public ConditionPipeline<T> when(Predicate<T> condition, Action<T> action) {
        conditionalActions.add(new ConditionalAction<>(condition, action));
        return this;
    }

    /**
     * Sets the default action to execute when no conditions match.
     *
     * @param action the fallback action
     * @return the updated pipeline for fluent chaining
     */
    public ConditionPipeline<T> otherwise(Action<T> action) {
        this.defaultAction = action;
        return this;
    }

    /**
     * Executes the pipeline by evaluating the input against the defined conditions.
     * The first matching condition's action is executed. If no conditions match,
     * the default action is executed.
     *
     * @param input the input to evaluate
     */
    public void execute(T input) {
        for (ConditionalAction<T> ca : conditionalActions) {
            if (ca.condition.test(input)) {
                ca.action.execute(input);
                return;
            }
        }
        defaultAction.execute(input);
    }

    /**
     * Internal class representing a pair of condition and action.
     *
     * @param <T> the type of input
     */
    private static class ConditionalAction<T> {
        final Predicate<T> condition;
        final Action<T> action;

        ConditionalAction(Predicate<T> condition, Action<T> action) {
            this.condition = condition;
            this.action = action;
        }
    }
}