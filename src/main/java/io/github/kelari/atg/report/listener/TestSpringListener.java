package io.github.kelari.atg.report.listener;

import io.github.kelari.atg.report.ListenerEvidenceRecorder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class TestSpringListener implements TestExecutionListener {

    private final ListenerEvidenceRecorder recorder = ListenerEvidenceRecorder.getInstance();

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        recorder.recordBeforeTestClass(testContext);
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        recorder.recordPrepareTestInstance(testContext);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        recorder.recordBeforeTestMethod(testContext);
    }

    @Override
    public void beforeTestExecution(TestContext testContext) throws Exception {
        recorder.recordBeforeTestExecution(testContext);
    }

    @Override
    public void afterTestExecution(TestContext testContext) throws Exception {
        recorder.recordAfterTestExecution(testContext);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        recorder.recordAfterTestMethod(testContext);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        recorder.recordAfterTestClass(testContext);
    }
}