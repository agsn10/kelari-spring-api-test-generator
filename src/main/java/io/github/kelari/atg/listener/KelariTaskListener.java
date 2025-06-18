package io.github.kelari.atg.listener;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.*;
import io.github.kelari.atg.model.ClassTest;
import io.github.kelari.atg.process.ClassGeneration;
import io.github.kelari.atg.process.KelariTreeScanner;
import io.github.kelari.atg.util.Predicates;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code KelariTaskListener} is a {@link TaskListener} that intercepts the Java compiler's
 * task lifecycle to analyze and collect metadata from source classes in order to
 * generate corresponding test classes.
 *
 * <p>During the {@code ANALYZE} phase, this listener delegates scanning to the {@link KelariTreeScanner},
 * which processes the compilation unit and populates a shared list of {@link ClassTest} entries.
 * Once compilation is finished, this data is used by {@link ClassGeneration}
 * to generate the corresponding test source files.
 *
 * <p>The use of {@code Collections.synchronizedList} ensures thread-safety in concurrent environments.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @copyright 2025 Kelari. All rights reserved.
 *
 */
public class KelariTaskListener implements TaskListener {

    private final KelariTreeScanner kelariTreeScanner;
    private final List<ClassTest> classTestList;
    private final JavacTask task;
    private Trees trees;
    private TreePath path;
    /**
     * Constructs a new {@code KelariTaskListener} with the provided tree scanner.
     *
     * @param kelariTreeScanner the scanner responsible for traversing class declarations
     *                          and extracting metadata during the compilation process.
     */
    public KelariTaskListener(JavacTask task, KelariTreeScanner kelariTreeScanner) {
        this.kelariTreeScanner = kelariTreeScanner;
        this.classTestList = Collections.synchronizedList(new ArrayList<>());
        this.task = task;
    }

    /**
     * Called at the beginning of a compiler task event. When the event is of kind {@code ANALYZE},
     * it sets up the tree scanner with the current compilation unit and starts scanning
     * top-level class declarations.
     *
     * @param e the current task event
     */
    @Override
    public void started(TaskEvent e) {
        if (e.getKind().equals(TaskEvent.Kind.ANALYZE)) {
            CompilationUnitTree compilationUnit = e.getCompilationUnit();
            path = new TreePath(compilationUnit);
            trees = Trees.instance(task);
            // Defining the build tree and test list
            this.kelariTreeScanner.setCompilationUnitTree(e.getCompilationUnit());
            this.kelariTreeScanner.setClassTestList(this.classTestList);
            // Configuring the logger to record messages
            kelariTreeScanner.setCompilerLogger((kind, msg) ->
                    trees.printMessage(kind, msg, path.getLeaf(), path.getCompilationUnit())
            );
            // Processing type (class) declarations within the unit
            for (Tree typeDecl : e.getCompilationUnit().getTypeDecls())
                if (typeDecl instanceof ClassTree) {
                    Element element = trees.getElement(TreePath.getPath(path, typeDecl));
                    // Scanning classes within the compilation unit
                    if (Predicates.HAS_KELARI_ANNOTATION.test(element)) {
                        this.kelariTreeScanner.scan(typeDecl, null);
                        ClassGeneration kelariClassGeneration = new ClassGeneration(this.classTestList);
                        // Configuring the logger to record messages
                        kelariClassGeneration.setCompilerLogger((kind, msg) ->
                                trees.printMessage(kind, msg, path.getLeaf(), path.getCompilationUnit())
                        );
                        // ✨ Generate the test class file(s)
                        kelariClassGeneration.generateTestClass();
                    }
                }



        }
    }

    /**
     * Called at the end of a compiler task event. This method triggers test class generation
     * using the accumulated list of {@link ClassTest} instances.
     *
     * @param e the finished task event
     */
    @Override
    public void finished(TaskEvent e) {
    }
}