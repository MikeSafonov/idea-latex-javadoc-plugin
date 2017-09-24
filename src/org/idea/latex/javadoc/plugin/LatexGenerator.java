package org.idea.latex.javadoc.plugin;

import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * LaTeX image generator.
 *
 * @author MikeSafonov
 */
public class LatexGenerator {

    public LatexGenerator() {
    }

    /**
     * Generate a PNG with the given path and LaTeX formula and store image in plugin temp folder
     *
     * @param formula the formula to compile
     * @see TempFileManager#saveTempFile(byte[], BufferedImage, String)
     */
    public URL generate(@NotNull String formula, @NotNull LatexOptions latexOptions) throws IOException {
        TeXFormula tf = new TeXFormula(formula);
        TeXIcon ti = tf.createTeXIcon(TeXConstants.STYLE_DISPLAY, latexOptions.getIconSize());
        BufferedImage bimg = UIUtil.createImage(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2d = bimg.createGraphics();
        g2d.setColor(latexOptions.getBackgroundColor());
        g2d.fillRect(0, 0, ti.getIconWidth(), ti.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(latexOptions.getForegroundColor());
        ti.paintIcon(jl, g2d, 0, 0);

        return Plugin.tempFileManager().saveTempFile(formula.getBytes(), bimg, "png");
    }
}
