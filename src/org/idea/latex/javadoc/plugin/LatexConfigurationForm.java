package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The plugin configuration form.
 *
 * @author MikeSafonov
 */
public class LatexConfigurationForm implements Configurable {


    private JPanel root;
    private JTextField fontSizeTextField;
    private JCheckBox enableCheckBox;
    private JButton foregroundColorChoose;
    private JButton backgroundColorChoose;

    private int previousFontValue;
    private Color foregroundColor;
    private Color backgroundColor;


    public LatexConfigurationForm() {

        enableCheckBox.setSelected(LatexOptions.getInstance().getEnable());
        enableCheckBox.addItemListener(e -> {
            synchronized (LatexConfigurationForm.this) {

                setupEnableToComponents(enableCheckBox.isSelected());
            }
        });

        fontSizeTextField.setText(LatexOptions.getInstance().getIconSize() + "");
        fontSizeTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String textValue = ((JTextField) input).getText();
                try {
                    Integer intValue = Integer.parseInt(textValue);
                    previousFontValue = intValue;
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });


        foregroundColorChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = UIUtil.getTreeBackground();
                if (color != null) {
                    foregroundColor = color;
                }
            }
        });

        backgroundColorChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = UIUtil.getTreeBackground();
                if (color != null) {
                    backgroundColor = color;
                }
            }
        });


        setupData(LatexOptions.getInstance());
    }


    private void setupEnableToComponents(boolean enable) {
        foregroundColorChoose.setEnabled(enable);
        backgroundColorChoose.setEnabled(enable);
        fontSizeTextField.setEnabled(enable);
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return root;
    }

    @Override
    public synchronized boolean isModified() {
        return LatexOptions.getInstance().getEnable() != enableCheckBox.isSelected() ||
                !LatexOptions.getInstance().getBackgroundColor().equals(backgroundColor) ||
                !LatexOptions.getInstance().getForegroundColor().equals(foregroundColor) ||
                LatexOptions.getInstance().getIconSize() != previousFontValue;
    }

    @Override
    public synchronized void apply() {
        LatexOptions.getInstance().setIconSize(previousFontValue);
        LatexOptions.getInstance().setEnable(enableCheckBox.isSelected());
        LatexOptions.getInstance().setBackgroundColor(backgroundColor);
        LatexOptions.getInstance().setForegroundColor(foregroundColor);
    }

    @Override
    public synchronized void reset() {

        setupData(LatexOptions.getInstance());
    }

    private void setupData(LatexOptions latexOptions) {

        enableCheckBox.setSelected(latexOptions.getEnable());
        fontSizeTextField.setText(latexOptions.getIconSize() + "");

        setupEnableToComponents(latexOptions.getEnable());

        previousFontValue = LatexOptions.getInstance().getIconSize();
        foregroundColor = LatexOptions.getInstance().getForegroundColor();
        backgroundColor = LatexOptions.getInstance().getBackgroundColor();

    }

    @Nls
    @Override
    public String getDisplayName() {
        return "LaTeX Javadoc";
    }

}
