package io.github.kelari.atg.annotation.defaults;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class DummyMatcher implements Matcher<Object> {
    @Override public boolean matches(Object item) { return false; }
    @Override public void describeMismatch(Object item, Description description) {}
    @Override public void describeTo(Description description) {}
    @Override public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
}