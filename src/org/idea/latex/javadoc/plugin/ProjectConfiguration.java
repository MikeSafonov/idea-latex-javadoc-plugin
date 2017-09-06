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

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A project component holding the project-wide configuration.
 *
 */
@State(name = Plugin.PLUGIN_NAME + ".ProjectConfiguration",
        storages = @Storage("latex-javadoc.xml"))
public class ProjectConfiguration implements ProjectComponent, PersistentStateComponent<LatexOptions> {

    private LatexOptions configuration;

    public ProjectConfiguration() {
        configuration = new LatexOptions();
    }

    public synchronized void setConfiguration(LatexOptions configuration) {
        System.out.println("configuration changed");
        this.configuration = new LatexOptions(configuration);
        Plugin.tempFileManager().cleanup();
    }

    public synchronized LatexOptions getConfiguration() {
        return configuration;
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return Plugin.PROJECT_CONFIG_NAME;
    }

    public void projectOpened() {
        // called when project is opened
    }

    public void projectClosed() {
        // called when project is being closed
    }

    @Nullable
    public synchronized LatexOptions getState() {
        return new LatexOptions(configuration);
    }

    public synchronized void loadState(LatexOptions state) {
        configuration = new LatexOptions(state);
    }

    public synchronized boolean isLatexJavadocEnabled() {
        return configuration.getEnable();
    }

}
