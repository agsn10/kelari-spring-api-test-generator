package io.github.kelari.atg.report.listener;

import com.google.auto.service.AutoService;
import io.github.kelari.atg.report.ListenerEvidenceRecorder;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

@AutoService(org.junit.platform.launcher.TestExecutionListener.class)
public class TestJUnitListener implements TestExecutionListener {

    private final ListenerEvidenceRecorder recorder = ListenerEvidenceRecorder.getInstance();

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        recorder.recordTestPlanExecutionStarted(testPlan);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        recorder.recordTestPlanExecutionFinished(testPlan);
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        recorder.recordDynamicTestRegistered(testIdentifier);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        recorder.recordExecutionSkipped(testIdentifier, reason);
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        recorder.recordExecutionStarted(testIdentifier);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        recorder.recordExecutionFinished(testIdentifier, testExecutionResult);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        recorder.recordReportingEntryPublished(testIdentifier, entry);
    }

}
