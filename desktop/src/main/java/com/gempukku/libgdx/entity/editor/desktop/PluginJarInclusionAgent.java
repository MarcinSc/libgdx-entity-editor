package com.gempukku.libgdx.entity.editor.desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class PluginJarInclusionAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        appendJars(instrumentation);
    }

    private static void appendJars(Instrumentation instrumentation) {
        System.out.println("Including Plugin JARs...");
        File jarList = new File(".prefs", "com.gempukku.libgdx.entity.editor.plugins.jars");
        System.out.println("Jar list file: " + jarList.getAbsolutePath());
        if (jarList.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(jarList));
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.equals("")) {
                            File jarFile = new File(line);
                            if (jarFile.exists()) {
                                System.out.println("Loading JAR: " + jarFile.getName());
                                instrumentation.appendToSystemClassLoaderSearch(new JarFile(jarFile));
                            }
                        }
                    }
                } finally {
                    bufferedReader.close();
                }
            } catch (Exception exp) {
                System.out.println(exp.getMessage());
                throw new RuntimeException(exp);
            }
        }
    }
}
