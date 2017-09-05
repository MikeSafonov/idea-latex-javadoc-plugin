package org.idea.latex.doclet.plugin;

import com.intellij.codeInsight.javadoc.JavaDocExternalFilter;
import com.intellij.lang.ASTNode;
import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.JavaDocElementType;

import java.util.List;

/**
 * @author MikeSafonov
 */
public class LatexDocumentationProvider extends JavaDocumentationProvider {


    public LatexDocumentationProvider() {
    }


    @Override
    public String generateDoc(PsiElement element, PsiElement originalElement) {


        PsiFile file = getPsiFile(element);

        if (file != null) {
            DocCommentProcessor processor = new DocCommentProcessor(file);

            String docHtml;

            if (element instanceof PsiMethod || element instanceof PsiParameter) {

                docHtml = super.generateDoc(element, originalElement);
                DocCommentProcessor docCommentProcessor = new DocCommentProcessor();
                docHtml = docCommentProcessor.processDocComment(docHtml);
//                List<String> latexParts = docCommentProcessor.findLatexParts(docHtml);
//
//                for (String part : latexParts) {
//
//                    docHtml = docCommentProcessor.processLatexFormula(part, docHtml);
//                }
//                System.out.println(docHtml);

            } else {
                LatexJavaDocInfoGenerator javaDocInfoGenerator = new LatexJavaDocInfoGenerator(element.getProject(), element, processor);
                List<String> docURLs = getExternalJavaDocUrl(element);
                String text = javaDocInfoGenerator.generateDocInfo(docURLs);
                Plugin.print("Intermediate HTML output", text);
                docHtml = JavaDocExternalFilter.filterInternalDocInfo(text);
            }
//            if (element instanceof PsiMethod) {
//                docHtml = super.generateDoc(PsiProxy.forMethod((PsiMethod) element), originalElement);
//            } else if (element instanceof PsiParameter) {
//                docHtml = super.generateDoc(PsiProxy.forParameter((PsiParameter) element), originalElement);
//            } else {
//                LatexJavaDocInfoGenerator javaDocInfoGenerator = new LatexJavaDocInfoGenerator(element.getProject(), element, processor);
//                List<String> docURLs = getExternalJavaDocUrl(element);
//                String text = javaDocInfoGenerator.generateDocInfo(docURLs);
//                Plugin.print("Intermediate HTML output", text);
//                docHtml = JavaDocExternalFilter.filterInternalDocInfo(text);
//            }
//            docHtml = extendCss(docHtml);
            Plugin.print("Final HTML output", docHtml);
            System.out.println("final HTML:" + docHtml);
            return docHtml;

        } else {
            System.out.println("null return");
            return null;
        }
    }


    private PsiFile getPsiFile(PsiElement element){

        PsiFile file = null;
        if (element instanceof PsiDirectory) {
            // let's see whether we can map the directory to a package; if so, change the
            // element to the package and continue
            PsiPackage pkg = JavaDirectoryService.getInstance().getPackage((PsiDirectory) element);
            if (pkg != null) {
                element = pkg;
            } else {
                return null;
            }
        }


        if (element instanceof PsiPackage) {
            for (PsiDirectory dir : ((PsiPackage) element).getDirectories()) {
                PsiFile info = dir.findFile(PsiPackage.PACKAGE_INFO_FILE);
                if (info != null) {
                    ASTNode node = info.getNode();
                    if (node != null) {
                        ASTNode docCommentNode = node.findChildByType(JavaDocElementType.DOC_COMMENT);
                        if (docCommentNode != null) {
                            // the default implementation will now use this file
                            // we're going to take over below, if Markdown is enabled in
                            // the corresponding module
                            // see JavaDocInfoGenerator.generatePackageJavaDoc()
                            file = info;
                            break;
                        }
                    }
                }
                if (dir.findFile("package.html") != null) {
                    // leave that to the default
                    return null;
                }
            }
        } else {
            if (JavaLanguage.INSTANCE.equals(element.getLanguage())) {
                element = element.getNavigationElement();
                if (element.getContainingFile() != null) {
                    file = element.getContainingFile();
                }
            }
        }


        return file;
    }

    private static String extendCss(String html) {
        String css = "\n"
                // I know, these tables aren't beautiful; however, Swing CSS is so
                // limited, this is about as good as it gets ...
                + "table { /*unsupported: border-collapse: collapse;*/ border: 0; border-spacing: 0; }\n"
                + "table td, table th { border: outset 1px black; padding-left: 5px; padding-right: 5px;}\n"
                + "\n";
        String upperHtml = html.toUpperCase();
        int bodyPos = upperHtml.indexOf("<BODY>");
        if (bodyPos < 0) {
            return html;
        }
        int stylePos = upperHtml.lastIndexOf("</STYLE>", bodyPos);
        if (stylePos < 0) {
            return html;
        }
        return html.substring(0, stylePos) + css + html.substring(stylePos);
    }

}
