package io.github.kelari.atg.report.listener;

import com.google.auto.service.AutoService;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.lang.reflect.Method;

@AutoService(org.junit.platform.launcher.TestExecutionListener.class)
public class KelariUnifiedListener implements TestExecutionListener, org.junit.platform.launcher.TestExecutionListener {

    private void logRecursivamente(TestPlan plan, TestIdentifier id, int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.printf("%s- [%s] %s (%s)%n",
                indent,
                id.getType(),                               // CONTAINER ou TEST
                id.getDisplayName(),                        // nome legível
                id.getUniqueId()                            // identificador interno
        );
        for (TestIdentifier filho : plan.getChildren(id))
            logRecursivamente(plan, filho, nivel + 1);
    }

    // 🔹 Métodos da interface do JUnit Platform
    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
        System.out.println("✅ Iniciando execução do plano de teste...");
        System.out.println("➡️  Total de testes detectados: " + testPlan.countTestIdentifiers(TestIdentifier::isTest));

        for (TestIdentifier identifier : testPlan.getRoots())
            logRecursivamente(testPlan, identifier, 0);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        System.out.println("🔍 Testes descobertos no TestPlan:");

        testPlan.getRoots().forEach(root -> {
            System.out.println("📦 Raiz: " + root.getDisplayName());

        });

        System.out.println("\n📊 Resumo:");
        long totalTestes = testPlan.countTestIdentifiers(TestIdentifier::isTest);
        long totalContainers = testPlan.countTestIdentifiers(TestIdentifier::isContainer);

        System.out.printf("🧪 Total de testes: %d%n", totalTestes);
        System.out.printf("📁 Total de containers: %d%n", totalContainers);
    }

    @Override
    public void dynamicTestRegistered(TestIdentifier testIdentifier) {
        System.out.println("🔵 [DYNAMIC TEST REGISTERED]");
        System.out.println("  Unique ID:      " + testIdentifier.getUniqueId());
        System.out.println("  Display Name:   " + testIdentifier.getDisplayName());
        System.out.println("  Source:         " + testIdentifier.getSource().orElse(null));
        System.out.println("  Tags:           " + testIdentifier.getTags());
        System.out.println("  Is Container:   " + testIdentifier.isContainer());
        System.out.println("  Is Test:        " + testIdentifier.isTest());
        System.out.println("  Parent ID:      " + testIdentifier.getParentId().orElse(null));
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        System.out.println("🚫 Teste ignorado:");
        System.out.println("  ID: " + testIdentifier.getUniqueId());
        System.out.println("  Display Name: " + testIdentifier.getDisplayName());
        System.out.println("  Tipo de Teste:");
        System.out.println("    - É container? " + testIdentifier.isContainer());
        System.out.println("    - É teste?     " + testIdentifier.isTest());
        System.out.println("  Tags: " + testIdentifier.getTags());
        System.out.println("  Razão: " + reason);

        // Se quiser ver o Optional do parent
        testIdentifier.getParentId().ifPresent(parent -> {
            System.out.println("  Parent ID: " + parent);
        });

        // Pode também obter Source (caso exista)
        testIdentifier.getSource().ifPresent(source -> {
            System.out.println("  Source: " + source.getClass().getSimpleName());
            System.out.println("    → " + source.toString());
        });
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        System.out.println("══════════════════════════════════");
        System.out.println("[JUnit] ▶ Início: Início da execução do teste:");
        System.out.println("• Display Name       : " + testIdentifier.getDisplayName());
        System.out.println("• Unique ID          : " + testIdentifier.getUniqueId());
        System.out.println("• Test Source        : " + testIdentifier.getSource().orElse(null));
        System.out.println("• Tags               : " + testIdentifier.getTags());
        System.out.println("• Type (isTest)      : " + testIdentifier.isTest());
        System.out.println("• Type (isContainer) : " + testIdentifier.isContainer());
        System.out.println("• Parent ID          : " + testIdentifier.getParentId().orElse(null));
        System.out.println("══════════════════════════════════");
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        // Logar informações completas do TestIdentifier
        System.out.println("[JUnit] ⏹ Fim do teste: ");
        System.out.println("[JUnit] Nome do Teste: " + testIdentifier.getDisplayName());
        System.out.println("[JUnit] Tipo de Teste: " + testIdentifier.getType());
        System.out.println("[JUnit] ID do Teste: " + testIdentifier.getUniqueId());

        // Logar o resultado do teste
        System.out.println("[JUnit] Status do Teste: " + testExecutionResult.getStatus());
        testExecutionResult.getThrowable().ifPresent(t -> {
            System.out.println("[JUnit] Exceção: " + t.getClass().getName());
            System.out.println("[JUnit] Mensagem: " + t.getMessage());
        });

        // Logar informações adicionais, caso existam
        if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED)
            System.out.println("[JUnit] Teste falhou!");
        else
            System.out.println("[JUnit] Teste concluído com sucesso.");
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        System.out.println("Test Identifier: " + testIdentifier.getUniqueId());
        System.out.println("Test Display Name: " + testIdentifier.getDisplayName());
        System.out.println("Test Type: " + testIdentifier.getType());

        // Logando o status do teste
        System.out.println("Test Execution Status: " + testIdentifier.toString());

    }

    // 🔹 Métodos da interface do Spring
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        System.out.println(">>> Iniciando execução da classe de teste: "+ testContext.getTestClass().getName());
        // Nome do método de teste que será executado
        if (testContext.getTestMethod() != null)
            System.out.println("Método de teste a ser executado: "+ testContext.getTestMethod().getName());
        // Logando a instância do teste
        System.out.println("Instância do teste: "+ testContext.getTestInstance());
        // Se a classe de teste estiver associada a um ApplicationContext, logue a informação
        if (testContext.getApplicationContext() != null) {
            System.out.println("ApplicationContext associado ao teste: "+ testContext.getApplicationContext().getId());
            System.out.println("ApplicationContext displayName ao teste: "+ testContext.getApplicationContext().getDisplayName());
            System.out.println("ApplicationContext applicationName ao teste: "+ testContext.getApplicationContext().getApplicationName());
        }
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        // Obtém o objeto de teste (instância da classe de teste)
        Object testInstance = testContext.getTestInstance();
        System.out.println("Test Instance: " + testInstance);
        // Obtém o método de teste que está sendo executado
        Method testMethod = testContext.getTestMethod();
        System.out.println("Test Method: " + testMethod.getName());
        // Obtém a classe de teste
        Class<?> testClass = testContext.getTestClass();
        System.out.println("Test Class: " + testClass.getName());
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        // Nome da classe de teste
        String className = testContext.getTestClass().getName();
        // Nome do método de teste
        String methodName = testContext.getTestMethod().getName();
        // Parâmetros do método de teste
        Object[] methodParams = testContext.getTestMethod().getParameters();
        // Contexto do objeto de teste
        Object testInstance = testContext.getTestInstance();
        // Informações gerais de TestContext
        String testContextDetails = testContext.toString();
        // Logando todas as informações
        System.out.println("[Spring] 🔧 Antes do teste:");
        System.out.println("    Classe de teste: " + className);
        System.out.println("    Método de teste: " + methodName);
        System.out.println("    Parâmetros do método:");
        for (Object param : methodParams)
            System.out.println("        " + param);
        System.out.println("    Instância de teste: " + testInstance);
        System.out.println("    Detalhes do contexto de teste: " + testContextDetails);
    }

    @Override
    public void beforeTestExecution(TestContext testContext) throws Exception {
        // Nome do método de teste
        String testMethodName = testContext.getTestMethod().getName();
        System.out.println("Método de Teste: " + testMethodName);
        // Nome da classe de teste
        String testClassName = testContext.getTestClass().getName();
        System.out.println("Classe de Teste: " + testClassName);
        // Test Instance (instância da classe de teste)
        Object testInstance = testContext.getTestInstance();
        System.out.println("Instância do Teste: " + testInstance);
        // TestContext está preparando o teste para execução
        System.out.println("Preparando para executar o método: " + testMethodName);
    }

    @Override
    public void afterTestExecution(TestContext testContext) throws Exception {
        Object testInstance = testContext.getTestInstance();
        Method testMethod = testContext.getTestMethod();
        Class<?> testClass = testContext.getTestClass();

        System.out.println("🔍 [Kelari] Pós-execução do teste:");
        System.out.println("➡ Classe de teste: " + testClass.getName());
        System.out.println("➡ Instância: " + testInstance);

        System.out.println("➡ Resultado do teste (sucesso ou exceção): " + testContext.getTestException());
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        Method metodo = testContext.getTestMethod();
        Object testInstance = testContext.getTestInstance();

        System.out.println("[Spring] ✅ Depois do teste: " + metodo.getName());

    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        System.out.println("==== [afterTestClass] Informações do TestContext ====");

        System.out.println("🧪 Test Class       : " + testContext.getTestClass().getName());
        System.out.println("📦 ApplicationContext: " + testContext.getApplicationContext().getClass().getName());
        System.out.println("🔧 Test Instance    : " + testContext.getTestInstance());
        System.out.println("🔍 Test Method      : " + testContext.getTestMethod()); // pode ser null aqui
        System.out.println("📂 Test Directory   : " + testContext.getTestClass().getProtectionDomain().getCodeSource().getLocation());

        System.out.println("====================================================");
    }
}
