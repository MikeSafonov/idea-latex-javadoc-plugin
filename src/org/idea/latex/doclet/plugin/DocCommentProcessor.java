/*
 * Copyright 2013 Raffael Herzog
 *
 * This file is part of markdown-doclet.
 *
 * markdown-doclet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * markdown-doclet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with markdown-doclet.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.idea.latex.doclet.plugin;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The work-horse, renders the JavaDoc comments using Markdown and creates a new
 * PsiDocComment that can be passed to IDEA's default QuickDoc implementation.
 *
 * @author <a href="mailto:herzog@raffael.ch">Raffael Herzog</a>
 */
public class DocCommentProcessor {

    private static final Logger LOG = Logger.getInstance(DocCommentProcessor.class);

    private final Project project;
    private final PsiElementFactory psiElementFactory;
    private final LaTeXGenerator latexGenerator;

    private final String LATEX_TAG_START = "<latex>";
    private final String LATEX_TAG_END = "</latex>";
    private final String LATEX_REGXP = "(?<=<latex>)(.*)(?=</latex>)";

    public DocCommentProcessor() {
        project = null;
        psiElementFactory = null;
        latexGenerator = new LaTeXGenerator();
    }

    public DocCommentProcessor(PsiFile file) {
        this.latexGenerator = new LaTeXGenerator();
        if (file != null) {
            this.project = file.getProject();
            psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        } else {
            project = null;
            psiElementFactory = null;
        }
    }

    public String processDocComment(String docComment) {

        if (isNotContainLatexTags(docComment)) {
            System.out.println("NotContainLatexTags");
            return docComment;
        }

        List<String> latexParts = findLatexParts(docComment);

        System.out.println("find " + latexParts.size() + " formulas");
        for (String part : latexParts) {

            docComment = processLatexFormula(part, docComment);
        }

        return docComment;
    }

    public PsiDocComment processDocComment(PsiDocComment docComment) {

        if (!isNotContainLatexTags(docComment)) {
            return docComment;
        }

        PsiDocCommentOwner context = PsiTreeUtil.getParentOfType(docComment, PsiDocCommentOwner.class);

        String latexText = docComment.getText();

        List<String> latexParts = findLatexParts(latexText);

        for (String part : latexParts) {

            latexText = processLatexFormula(part, latexText);
        }

        return docCommentFromText(context, latexText);


    }

    public List<String> findLatexParts(String text) {
        List<String> allMatches = new ArrayList<String>();

        try {
            Matcher matcher = Pattern.compile(LATEX_REGXP).matcher(text);

            while (matcher.find()) {
                allMatches.add(matcher.group());
            }

        } catch (Exception ignore) {
        }
        return allMatches;
    }

    public boolean isNotContainLatexTags(PsiDocComment psiDocComment) {

        return isNotContainLatexTags(psiDocComment.getText());
    }

    public boolean isNotContainLatexTags(String text) {
        boolean contain = false;

        if (text != null) {
            contain = text.contains(LATEX_TAG_START);
        }

        return !contain;
    }

    public String processLatexFormula(final String formula,final String originalText) {

        String newText = originalText;
        try {
            URL generatedImage = latexGenerator.generate(formula);

            if (generatedImage == null) {
                System.out.println("generatedImage == null");
                return originalText;
            }

            String partToReplace = LATEX_TAG_START + formula + LATEX_TAG_END;
            String replacer = "<img src=\"" + generatedImage.getFile() + "\"></img>";

            newText = originalText.replace(partToReplace, replacer);

        } catch (Exception ignore) {
        }

        return newText;
    }

    private PsiDocComment docCommentFromText(PsiElement context, CharSequence text) {
        return psiElementFactory.createDocCommentFromText(text.toString(), context);
    }


}
