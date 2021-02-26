package com.gempukku.libgdx.entity.editor.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gempukku.libgdx.entity.editor.GdxEntityEditor;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentRegistry;
import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.PluginDefinition;
import com.gempukku.libgdx.entity.editor.plugin.PluginRegistry;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphEntityEditorPluginInitializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

/**
 * Launches the desktop (LWJGL) application.
 */
public class DesktopLauncher {
    public static void main(String[] arg) throws IOException, URISyntaxException {
        String executeArgument = "NoPlugins";
        if (arg.length == 0 || !arg[0].equals(executeArgument)) {
            String jarPath = DesktopLauncher.class
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

            CustomComponentRegistry.registerComponentFieldType(new FloatComponentFieldType());

            PluginRegistry.addPluginDefinition(
                    new PluginDefinition("internal", AshleyGraphEntityEditorPluginInitializer.class,
                            "Graph + Ashley", "latest", false, false));

            createApplication();
        }
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new GdxEntityEditor(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "libGDX Entity Editor";
        configuration.width = 1440;
        configuration.height = 810;
        //// This prevents a confusing error that would appear after exiting normally.
        configuration.forceExit = false;

        for (int size : new int[]{128, 64, 32, 16}) {
            configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
        }
        return configuration;
    }
}