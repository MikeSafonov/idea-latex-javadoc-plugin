package org.idea.latex.javadoc.plugin;

import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * Object for combobox search
 *
 * @author MikeSafonov
 */
public enum ColorSelectionElement {


    WHITE("white", JBColor.WHITE),
    BLACK("black", JBColor.BLACK),
    RED("red", JBColor.RED),
    YELLOW("yellow", JBColor.YELLOW),
    BLUE("blue", JBColor.BLUE);

    private String displayableName;
    private Color color;

    ColorSelectionElement(String displayableName, Color color) {
        this.displayableName = displayableName;
        this.color = color;
    }

    public String getDisplayableName() {
        return displayableName;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getDisplayableName();
    }
}
