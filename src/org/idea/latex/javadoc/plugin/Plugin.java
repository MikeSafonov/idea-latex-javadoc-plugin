/*
 * Copyright 2013 Raffael Herzog
 *
 * This file is part of markdown-doclet.
 *
 * markdown-doclet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * markdown-doclet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with markdown-doclet.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;


/**
 * Some utilities for the plugin.
 *
 */
public final class Plugin {

    public static final boolean DEBUG = true;

    public static final String PLUGIN_NAME = "org.idea.latex.javadoc.plugin.LatexJavadocPlugin";

    public static final String TEMP_FILE_MANAGER_NAME = PLUGIN_NAME + ".TempFileManager";
    public static final String PROJECT_CONFIG_NAME = PLUGIN_NAME + ".ProjectConfig";

    private Plugin() {
    }

    public static TempFileManager tempFileManager() {
        return ApplicationManager.getApplication().getComponent(TempFileManager.class);
    }

    public static ProjectConfiguration projectConfiguration(Project project) {
        return project.getComponent(ProjectConfiguration.class);
    }

    public static void print(String description, String output) {
        if ( DEBUG ) {
            System.out.println("\n\n\n*** " + description + "\n");
            System.out.println(output);
        }
    }

}
