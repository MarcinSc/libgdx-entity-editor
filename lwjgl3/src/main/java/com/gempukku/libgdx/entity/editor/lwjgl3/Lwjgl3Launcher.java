package com.gempukku.libgdx.entity.editor.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.gempukku.libgdx.entity.editor.GdxEntityEditor;
import com.gempukku.libgdx.entity.editor.plugin.PluginDefinition;
import com.gempukku.libgdx.entity.editor.plugin.PluginRegistry;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphEntityEditorPluginInitializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {
    public static void main(String[] arg) throws IOException, URISyntaxException {
        String executeArgument = "NoPlugins";
        if (arg.length == 0 || !arg[0].equals(executeArgument)) {
            String jarPath = Lwjgl3Launcher.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            System.out.println("Starting process...");

            String[] args = new String[]{"java", "-javaagent:" + jarPath, "-jar", jarPath, executeArgument};
            ProcessBuilder builder = new ProcessBuilder(args);

            Process process = builder.start();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                String line;
                while ((line = errorReader.readLine()) != null)
                    System.out.println(line);
            } finally {
                errorReader.close();
            }
        } else {
            System.out.println("Starting libGDX-entity Editor");

            PluginRegistry.addPluginDefinition(
                    new PluginDefinition("internal", AshleyGraphEntityEditorPluginInitializer.class,
                            "Graph + Ashley", "latest", false, false));

            createApplication();
        }
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new GdxEntityEditor(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("libGDX entity editor");
        configuration.setWindowedMode(1440, 810);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        return configuration;
    }
}