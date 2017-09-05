package org.idea.latex.javadoc.plugin;

import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.psi.*;

/**
 * A documentation provider that renders the JavaDoc comments including LaTeX formulas.
 *
 * @author MikeSafonov
 */
public class LatexDocumentationProvider extends JavaDocumentationProvider {


    public LatexDocumentationProvider() {
    }


    @Override
    public String generateDoc(PsiElement element, PsiElement originalElement) {

        LatexJavadocPostProcessor latexJavadocPostProcessor =
                new LatexJavadocPostProcessor(element);

        String originalHtml = super.generateDoc(element, originalElement);

        return latexJavadocPostProcessor.process(originalHtml);
    }


}
