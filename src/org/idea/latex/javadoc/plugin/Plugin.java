package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;


/**
 * Some utilities for the plugin.
 *
 */
final class Plugin {

    public static final String PLUGIN_NAME = "org.idea.latex.javadoc.plugin.LatexJavadocPlugin";

    public static final String TEMP_FILE_MANAGER_NAME = PLUGIN_NAME + ".TempFileManager";


    public static TempFileManager tempFileManager() {
        return ApplicationManager.getApplication().getComponent(TempFileManager.class);
    }


}
