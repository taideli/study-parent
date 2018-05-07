package com.tdl.study.java.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CompileManager {
    private static final Logger log = LoggerFactory.getLogger(CompileManager.class);

    private List<String> sources;

    public CompileManager() {
        sources = new LinkedList<>();
    }

    public void addSourceFile(String filename) {
        sources.add(filename);
    }

    public void compile() throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<>();
        if (null == compiler) {
            log.error("It seems as though your are running with a JRE");
            log.error("please install a JDK and set system variable JAVA_HOME to use it");
            throw new IOException("No Java compiler found");
        }
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        List<String> srcJavaFiles = new ArrayList<>(sources.size());
        for (String srcJavaFile : sources) {
            srcJavaFiles.add(srcJavaFile);
            log.debug("Adding source file: " + srcJavaFile);
        }
        if (log.isDebugEnabled()) {
            log.debug("Invoking javac with args: " + args.stream().collect(Collectors.joining(" ")));
        }
        Iterable<? extends JavaFileObject> srcFileObjs = fileManager.getJavaFileObjectsFromStrings(srcJavaFiles);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, args, null, srcFileObjs);
        boolean complete = task.call();
        if (!complete) {
            throw new IOException("error returned by javac");
        }
    }
}
