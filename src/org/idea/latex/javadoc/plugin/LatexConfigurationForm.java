package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.ui.ColorChooserService;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

/**
 * The plugin configuration form.
 *
 * @author MikeSafonov
 */
public class LatexConfigurationForm implements Configurable {


    private JPanel root;
    private JTextField fontSizeTextField;
    private JCheckBox enableCheckBox;
    private JLabel foregroundColorChoose;
    private JLabel backgroundColorChoose;

    private int previousFontValue;
    private Color foregroundColor;
    private Color backgroundColor;


    public LatexConfigurationForm() {

        enableCheckBox.addItemListener(e -> {
            synchronized (LatexConfigurationForm.this) {

                setupEnableToComponents(enableCheckBox.isSelected());
            }
        });

        fontSizeTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String textValue = ((JTextField) input).getText();
                try {
                    Integer intValue = Integer.parseInt(textValue);
                    if(intValue <= 0){
                        return false;
                    }
                    previousFontValue = intValue;
                    return true;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });


        foregroundColorChoose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enableCheckBox.isSelected()) {
                    Color color = ColorChooserService.getInstance()
                            .showDialog(foregroundColorChoose, "Choose foreground color", foregroundColor,
                                    false, Collections.emptyList(), false);
                    if (color != null) {
                        foregroundColor = color;
                        setupChooser(foregroundColorChoose, color);
                    }
                }
            }
        });

        backgroundColorChoose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (enableCheckBox.isSelected()) {
                    Color color = ColorChooserService.getInstance().
                            showDialog(backgroundColorChoose, "Choose background color", backgroundColor,
                                    false, Collections.emptyList(), false);
                    if (color != null) {
                        backgroundColor = color;
                        setupChooser(backgroundColorChoose, color);
                    }
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
        Plugin.tempFileManager().cleanup();
    }

    @Override
    public synchronized void reset() {

        setupData(LatexOptions.getInstance());
    }

    private void setupChooser(JLabel chooser, Color color) {

        chooser.setBackground(color);
        chooser.setText(colorToHex(color));
    }


    private void setupData(LatexOptions latexOptions) {

        enableCheckBox.setSelected(latexOptions.getEnable());
        fontSizeTextField.setText(latexOptions.getIconSize() + "");

        setupEnableToComponents(latexOptions.getEnable());

        previousFontValue = LatexOptions.getInstance().getIconSize();
        foregroundColor = LatexOptions.getInstance().getForegroundColor();
        backgroundColor = LatexOptions.getInstance().getBackgroundColor();

        setupChooser(foregroundColorChoose, foregroundColor);
        setupChooser(backgroundColorChoose, backgroundColor);

    }


    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()).toUpperCase();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "LaTeX Javadoc";
    }

}
