package io.github.kelari.atg.plugin;

import com.google.auto.service.AutoService;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.Trees;
import io.github.kelari.atg.listener.TaskListener;
import io.github.kelari.atg.process.TreeScanner;
import io.github.kelari.atg.util.Constants;

/**
 * {@code KelariGenerateApiTestPlugin} is a custom annotation processing plugin for the Java compiler
 * that integrates with the {@link JavacTask}. This plugin is responsible for generating API test cases
 * from annotated Java source code during the compilation process.
 *
 * <p>The plugin listens for the compilation task events and processes the Java Abstract Syntax Tree (AST)
 * to identify classes and methods annotated with specific annotations. It then triggers the
 * necessary actions to generate test cases based on the extracted metadata.
 *
 * <p>It is registered using the {@link AutoService} annotation, which automatically registers this
 * plugin with the Java Compiler (Javac) so that it can be executed during the compilation process.
 *
 * <p>The {@code KelariGenerateApiTestPlugin} implements the {@link Plugin} interface and overrides
 * its methods to provide the necessary functionality for initializing and setting up the task listener.
 *
 * @author <a href="mailto:agsn10@hotmail.com">Antonio Neto</a> [<()>] – Initial implementation.
 * @since 1.0
 * @see TaskListener
 * @see TreeScanner
 * @see Constants
 */
@AutoService(Plugin.class)
public class KelariGenerateApiTestPlugin implements Plugin {

    /**
     * Returns the name of the plugin.
     *
     * @return the name of the plugin
     */
    @Override
    public String getName() {
        return Constants.PLUGIN_NAME;
    }

    /**
     * Initializes the plugin, setting up the task listener to process the compilation task.
     * This method is invoked when the Java compiler starts processing a source file.
     *
     * @param task the {@link JavacTask} instance that represents the current compilation task
     * @param args additional arguments passed to the plugin (currently unused)
     */
    @Override
    public void init(JavacTask task, String... args) {
        Trees trees = Trees.instance(task);
        task.setTaskListener(new TaskListener(task, new TreeScanner(trees)));
    }

}