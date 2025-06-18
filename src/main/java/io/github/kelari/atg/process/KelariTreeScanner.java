package io.github.kelari.atg.process;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import io.github.kelari.atg.annotation.ApiTestSpec;
import io.github.kelari.atg.annotation.KelariGenerateApiTest;
import io.github.kelari.atg.model.*;
import io.github.kelari.atg.process.helper.KelariTreeScannerHelper;
import io.github.kelari.atg.util.*;

import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * {@code KelariTreeScanner} is a custom implementation of {@link TreeScanner} that traverses 
 * the Java Abstract Syntax Tree (AST) to locate classes and methods annotated with
 * {@link KelariGenerateApiTest} and {@link ApiTestSpec}.
 *
 * <p>It extracts metadata from these annotations to build a list of {@link ClassTest}
 * objects containing test specifications based on the scanned code structure.
 *
 * <p>This class is primarily used to automate the generation of API test cases from annotated source code.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved
 *
 */
public class KelariTreeScanner extends TreeScanner<Void, Void> implements CompilerLogger {

    private final Trees trees;
    private CompilationUnitTree compilationUnitTree;
    private List<ClassTest> classTestList;
    private CompilerLogger compilerLogger;
    private final KelariTreeScannerHelper kelariTreeScannerHelper;

    public void setCompilerLogger(CompilerLogger logger) {
        this.compilerLogger = logger;
    }
    public void log(Diagnostic.Kind kind, String message) {
        if (Objects.nonNull(compilerLogger))
            compilerLogger.log(kind, message);
    }

    /**
     * Constructs a {@code KelariTreeScanner} with the provided {@link Trees} instance.
     *
     * @param trees the {@code Trees} utility used to obtain information about the AST
     */
    public KelariTreeScanner(Trees trees) {
        this.trees = trees;
        this.kelariTreeScannerHelper = KelariTreeScannerHelper.getInstance();
        this.kelariTreeScannerHelper.setCompilerLogger(this);
    }

    /**
     * Visits class declarations in the source code and processes them if they are annotated
     * with {@link KelariGenerateApiTest}. For each annotated method, it extracts the associated
     * annotations and builds test scenario definitions.
     *
     * @param node   the class node to visit
     * @param unused additional parameter not used in this context
     * @return always returns {@code null}
     */
    @Override
    public Void visitClass(ClassTree node, Void unused) {
        TreePath path = trees.getPath(getCompilationUnitTree(), node);
        Element element = trees.getElement(path);
        String className = node.getSimpleName().toString();
        String packageName = PackageUtils.sanitizePackageName(getCompilationUnitTree().getPackageName().toString());
        KelariGenerateApiTest annotation = element.getAnnotation(KelariGenerateApiTest.class);
        if(Objects.nonNull(annotation)) {
            log(Diagnostic.Kind.NOTE, "Captured package: " + packageName);
            log(Diagnostic.Kind.NOTE, "Checking class annotation: " + node.getSimpleName().toString());
            ClassTest classTest = kelariTreeScannerHelper.createClassTest(className, packageName, element);
            for (Tree member : node.getMembers()) {
                if (member instanceof MethodTree) {
                    MethodTree method = (MethodTree) member;
                    if (method.getName().contentEquals("<init>"))
                        continue;
                    TreePath methodPath = trees.getPath(getCompilationUnitTree(), method);
                    Element methodElement = trees.getElement(methodPath);
                    SpecScenariosTest specScenariosTest = new SpecScenariosTest();
                    kelariTreeScannerHelper.createSpecScenariosTest(specScenariosTest, methodElement);
                    kelariTreeScannerHelper.processApiTestSpecAndApiTestCase(specScenariosTest, methodElement);
                    classTest.put(specScenariosTest.getMethodName(), specScenariosTest);
                    for(CaseTest caseTest : specScenariosTest.getCaseTestList())
                        caseTest.setMethodParameters(kelariTreeScannerHelper.processMethodParameters(methodElement));
                }
            }
            classTestList.add(classTest);
        }
        return super.visitClass(node, unused);
    }

    /**
     * Returns the current {@link CompilationUnitTree} being scanned.
     *
     * @return the compilation unit tree
     */
    public CompilationUnitTree getCompilationUnitTree() {
        return compilationUnitTree;
    }
    /**
     * Sets the {@link CompilationUnitTree} for this scanner.
     *
     * @param compilationUnitTree the compilation unit tree to set
     */
    public void setCompilationUnitTree(CompilationUnitTree compilationUnitTree) {
        if (Objects.isNull(compilationUnitTree)) {
            log(Diagnostic.Kind.ERROR, "CompilationUnitTree is not initialized.");
            throw new IllegalStateException("CompilationUnitTree is not initialized.");
        }
        this.compilationUnitTree = compilationUnitTree;
    }

    /**
     * Returns the list of {@link ClassTest} instances collected during scanning.
     *
     * @return the list of class tests
     */
    public List<ClassTest> getClassTestList() {
        return classTestList;
    }
    /**
     * Sets the list of {@link ClassTest} instances. If the provided list is {@code null}, an empty list is initialized.
     *
     * @param classTestList the class test list to set
     */
    public void setClassTestList(List<ClassTest> classTestList) {
        if (Objects.isNull(classTestList))
            this.classTestList = new ArrayList<>(0);
        this.classTestList = classTestList;
    }
}