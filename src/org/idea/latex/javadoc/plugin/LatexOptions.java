package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.components.*;
import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * LaTeX javadoc plugin configuration options
 *
 * @author MikeSafonov
 */
@State(name = "LatexOptions", storages = {@Storage("latex-javadoc.cfg")})
public class LatexOptions implements PersistentStateComponent<LatexOptions> {

    private boolean enable;
    private int iconSize;
    private Color backgroundColor;
    private Color foregroundColor;

    public static LatexOptions getInstance() {
        return ServiceManager.getService(LatexOptions.class);
    }

    private LatexOptions() {
        enable = false;
        iconSize = 20;
        backgroundColor = JBColor.WHITE;
        foregroundColor = JBColor.BLACK;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Boolean getEnable() {
        return enable;
    }

    public int getIconSize() {
        return iconSize;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    @Nullable
    @Override
    public LatexOptions getState() {
        return this;
    }

    @Override
    public void loadState(LatexOptions state) {

        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(o, this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
