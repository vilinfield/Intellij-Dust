package com.github.vilinfield.dust.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

public class DustPsiUtil
{
    public static DustOpenTag findParentOpenTagElement(PsiElement element)
    {
        return (DustOpenTag) PsiTreeUtil.findFirstParent(element, true, e -> e instanceof DustOpenTag);
    }

    public static DustCloseTag findParentCloseTagElement(PsiElement element)
    {
        return (DustCloseTag) PsiTreeUtil.findFirstParent(element, true, e -> e instanceof DustCloseTag);
    }

    public static boolean isNonRootStatementsElement(PsiElement element)
    {
        PsiElement statementsParent = PsiTreeUtil.findFirstParent(element, true, e -> e != null
                && e.getNode() != null
                && e.getNode().getElementType() == DustTypes.STATEMENTS);

        // We are a non-root statements if we're of type statements, and we have a statements parent.
        return element.getNode().getElementType() == DustTypes.STATEMENTS
                && statementsParent != null;
    }

    public static DustSelfCloseTag findParentPartialTagElement(PsiElement element)
    {
        return (DustSelfCloseTag) PsiTreeUtil.findFirstParent(element, true, e -> e instanceof DustSelfCloseTag
                && e.getFirstChild() != null
                && e.getFirstChild().getNode() != null
                && e.getFirstChild().getNode().getElementType() == DustTypes.PARTIAL);
    }
}
