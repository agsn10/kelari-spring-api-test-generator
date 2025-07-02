package io.github.kelari.atg.report;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.springframework.test.context.TestContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListenerEvidenceRecorder {

    private static final ListenerEvidenceRecorder INSTANCE = new ListenerEvidenceRecorder();

    private final List<StatusEvidenceJUnitTest> junitEvidences = Collections.synchronizedList(new ArrayList<>());
    private final List<StatusEvidenceSpringTest> springEvidences = Collections.synchronizedList(new ArrayList<>());

    private ListenerEvidenceRecorder() {
    }

    public static ListenerEvidenceRecorder getInstance() {
        return INSTANCE;
    }

    //start junit
    public void recordTestPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("📗 Iniciando: " + testPlan);
    }
    public void recordTestPlanExecutionFinished(TestPlan testPlan) {
        System.out.println("📘 Finalizado: " + testPlan);
    }
    public void recordExecutionSkipped(TestIdentifier testIdentifier, String reason) {
        System.out.println("📒 Ignorado: " + testIdentifier + " - Motivo: " + reason);
    }
    public void recordDynamicTestRegistered(TestIdentifier testIdentifier) {

    }
    public void recordExecutionStarted(TestIdentifier testIdentifier) {

    }
    public void recordExecutionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

    }
    public void recordReportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {

    }
    //end junit

    //start spring
    public void recordBeforeTestClass(TestContext testContext) {
    }
    public void recordPrepareTestInstance(TestContext testContext){
    }
    public void recordBeforeTestMethod(TestContext testContext){

    }
    public void recordBeforeTestExecution(TestContext testContext){

    }
    public void recordAfterTestExecution(TestContext testContext){

    }
    public void recordAfterTestMethod(TestContext testContext){

    }
    public void recordAfterTestClass(TestContext testContext){

    }
    //end spring
}