package org.idea.latex.javadoc.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class post processing original HTML javadoc, detect {@code <latex></latex>} tags
 * and replace this tags by generated LaTeX image.
 */
public class LatexJavadocPostProcessor {

    private final LatexGenerator latexGenerator;
    private final Pattern regexpPatter;
    private final Project project;

    private final String LATEX_TAG_START = "<latex>";
    private final String LATEX_TAG_END = "</latex>";
    private final String LATEX_REGXP = "(?<=<latex>)(.|\\n)*?(?=<\\/latex>)";

    LatexJavadocPostProcessor(PsiElement element) {

        this.project = element.getProject();
        if (LatexOptions.getInstance().getEnable()) {
            latexGenerator = new LatexGenerator();
            regexpPatter = Pattern.compile(LATEX_REGXP);
        } else {
            latexGenerator = null;
            regexpPatter = null;
        }
    }

    /**
     * Check is plugin enable
     *
     * @return is enable
     */
    private boolean isEnable() {
        return latexGenerator != null;
    }

    /**
     * Replacing LaTeX formulas in original javadoc html
     * by {@code <img>...</img>} tags
     *
     * @param originalHtml original javadoc html
     * @return processed javadoc html
     */
    String process(String originalHtml) {

        if (!isEnable() || isNotContainLatexTags(originalHtml)) {
            return originalHtml;
        }

        List<String> latexParts = findLatexParts(originalHtml);

        for (String part : latexParts) {

            originalHtml = processLatexFormula(part, originalHtml);
        }

        return originalHtml;
    }

    /**
     * Trying to detect {@code <latex>} tag in {@code text}
     *
     * @param text original html
     * @return
     */
    private boolean isNotContainLatexTags(final String text) {
        return !((text != null) && text.contains(LATEX_TAG_START));
    }

    /**
     * This method find all <b>{@code <latex>formula</latex>}</b> in {@code text}.
     *
     * @param text original html
     * @return list of LaTeX formulas
     */
    private List<String> findLatexParts(final String text) {
        List<String> allMatches = new ArrayList<String>();

        try {
            Matcher matcher = regexpPatter.matcher(text);

            while (matcher.find()) {
                allMatches.add(matcher.group());
            }

        } catch (Exception ignore) {
        }
        return allMatches;
    }

    /**
     * This method trying to generate LaTeX image from {@code formula} and replace
     * <b>{@code <latex>formula</latex>}</b> in <b>{@code originalText}</b> by
     * <b>{@code <p><img>url to image</img></p>}</b>. In case when <b>{@code formula}</b> isn`t
     * valid LaTeX string return <b>{@code originalText}</b>
     *
     * @param formula      probably LaTeX formula
     * @param originalText original HTML
     * @return processed HTML
     */
    private String processLatexFormula(final String formula, final String originalText) {

        String newText = originalText;
        try {
            URL generatedImage = latexGenerator.generate(formula, LatexOptions.getInstance());

            if (generatedImage == null) {
                return originalText;
            }

            String partToReplace = LATEX_TAG_START + formula + LATEX_TAG_END;
            String replacer = "<p><img src=\"" + generatedImage.getFile() + "\"></img></p>";
            newText = originalText.replace(partToReplace, replacer);

        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return newText;
    }
}
