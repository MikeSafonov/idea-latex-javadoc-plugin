package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Adds the plugin's configuration panel to "LaTeX Javadoc".
 *
 * @author MikeSafonov
 */
public class LatexConfigurable {

    private final Project project;
    private LatexConfigurationForm configurationForm;

    public LatexConfigurable(Project project) {
        this.project = project;
    }

//    @Nls
//    @Override
//    public String getDisplayName() {
//        return "LaTeX Javadoc";
//    }
//
//    @Nullable
//    @Override
//    public JComponent createComponent() {
//        if (configurationForm == null) {
//            configurationForm = new LatexConfigurationForm(project);
//        }
//        return configurationForm.getRoot();
//    }
//
//    @Override
//    public boolean isModified() {
//        return configurationForm != null && configurationForm.isModified();
//    }
//
//    @Override
//    public void apply() throws ConfigurationException {
//        System.out.println("latconf apply");
//        if (configurationForm != null) {
//            configurationForm.apply();
//        }
//    }
//
//    @Override
//    public void reset() {
//        if (configurationForm != null) {
//            configurationForm.reset();
//        }
//    }
//
//    @Override
//    public void disposeUIResources() {
//        configurationForm = null;
//    }
}
