package io.github.kelari.atg.process.processor;

import io.github.kelari.atg.model.CaseTest;

public interface AnnotationProcessor {
    void process(CaseTest caseTest, String name, Object fieldValue);
}