package org.idea.latex.doclet.plugin;

import com.intellij.util.ui.UIUtil;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author MikeSafonov
 */
public class LaTeXGenerator {

    public LaTeXGenerator() {
    }

    /**
     * Generate a PNG with the given path and LaTeX formula
     *
     * @param formula the formula to compile
     * @param path    the image path
     */
    public URL generate(String formula) throws IOException {
        TeXFormula tf = new TeXFormula(formula);
        TeXIcon ti = tf.createTeXIcon(TeXConstants.STYLE_DISPLAY, 40);
        BufferedImage bimg = UIUtil.createImage(ti.getIconWidth(), ti.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2d = bimg.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, ti.getIconWidth(), ti.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        ti.paintIcon(jl, g2d, 0, 0);

        return Plugin.tempFileManager().saveTempFile(formula.getBytes(), bimg, "png");
    }
}
