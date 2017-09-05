package org.idea.latex.javadoc.plugin;

import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * LaTeX javadoc plugin configuration options
 *
 * @author MikeSafonov
 */
public class LatexOptions {

    private Boolean enable;
    private int iconSize;
    private ColorSelectionElement backgroundColor;
    private ColorSelectionElement foregroundColor;

    public LatexOptions() {
        enable = false;
        iconSize = 20;
        backgroundColor = ColorSelectionElement.WHITE;
        foregroundColor = ColorSelectionElement.BLACK;
    }

    public LatexOptions(LatexOptions configuration) {
        this();
        if (configuration != null) {
            this.enable = configuration.enable;
            this.iconSize = configuration.iconSize;
            this.backgroundColor = configuration.backgroundColor;
            this.foregroundColor = configuration.foregroundColor;
        }
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public void setBackgroundColor(ColorSelectionElement backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setForegroundColor(ColorSelectionElement foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Boolean getEnable() {
        return enable;
    }

    public int getIconSize() {
        return iconSize;
    }

    public ColorSelectionElement getBackgroundColor() {
        return backgroundColor;
    }

    public ColorSelectionElement getForegroundColor() {
        return foregroundColor;
    }
}
