package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author MikeSafonov
 */
public class LatexConfigurationForm {


    private final Project project;
    private JPanel root;
    private JTextField fontSizeTextField;
    private JComboBox backgroundColorComboBox;
    private JComboBox foregroundColorComboBox;
    private JCheckBox enableCheckBox;
    private LatexOptions latexOptions;


    public LatexConfigurationForm(final Project project) {
        this.project = project;
        this.latexOptions = Plugin.projectConfiguration(project).getState();

        if (latexOptions == null) {
            latexOptions = new LatexOptions();
        }

        enableCheckBox.setSelected(latexOptions.getEnable());
        enableCheckBox.addItemListener(e -> {
            synchronized (LatexConfigurationForm.this) {
                latexOptions.setEnable(enableCheckBox.isSelected());

                setupEnableToComponents(latexOptions.getEnable());
            }
        });

        for (ColorSelectionElement element : ColorSelectionElement.values()) {
            backgroundColorComboBox.addItem(element);
        }
        backgroundColorComboBox.setSelectedItem(latexOptions.getBackgroundColor());
        backgroundColorComboBox.addItemListener(e -> {
            synchronized (LatexConfigurationForm.this) {
                latexOptions.setBackgroundColor((ColorSelectionElement) backgroundColorComboBox.getSelectedItem());
            }
        });

        for (ColorSelectionElement element : ColorSelectionElement.values()) {
            foregroundColorComboBox.addItem(element);
        }
        foregroundColorComboBox.setSelectedItem(latexOptions.getForegroundColor());
        foregroundColorComboBox.addItemListener(e -> {
            synchronized (LatexConfigurationForm.this) {
                latexOptions.setForegroundColor((ColorSelectionElement) foregroundColorComboBox.getSelectedItem());
            }
        });

        fontSizeTextField.setText(latexOptions.getIconSize() + "");
        fontSizeTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String textValue = ((JTextField) input).getText();
                try {
                    Integer intValue = Integer.parseInt(textValue);
                    synchronized (LatexConfigurationForm.this) {
                        latexOptions.setIconSize(intValue);
                    }
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });

        setupEnableToComponents(latexOptions.getEnable());

    }


    private void setupEnableToComponents(boolean enable) {
        backgroundColorComboBox.setEnabled(enable);
        foregroundColorComboBox.setEnabled(enable);
        fontSizeTextField.setEnabled(enable);
    }


    public JPanel getRoot() {
        return root;
    }


    public synchronized boolean isModified() {
        return !Plugin.projectConfiguration(project).getConfiguration().equals(latexOptions);
    }

    public synchronized void apply() {
        System.out.println("apply form");
        Plugin.projectConfiguration(project).setConfiguration(latexOptions);
    }

    public synchronized void reset() {
        latexOptions = Plugin.projectConfiguration(project).getConfiguration();
    }

}
