package com.github.vilinfield.dust;

import com.github.vilinfield.dust.psi.DustCloseTag;
import com.github.vilinfield.dust.psi.DustOpenTag;
import com.github.vilinfield.dust.psi.DustTypes;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DustAnnotator implements Annotator
{
    @Override
    public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder)
    {
        if (element instanceof DustOpenTag)
        {
            DustOpenTag openTag = (DustOpenTag) element;
            checkMatchingCloseTag(openTag, holder);
        }

        if (element.getNode().getElementType() == DustTypes.COMMENT)
        {
            String commentStr = element.getText();

            if (commentStr.length() >= 8)
            {
                commentStr = commentStr.substring(0, commentStr.length() - 2);
                Pattern p = Pattern.compile("TODO[^\n]*");
                Matcher m = p.matcher(commentStr);

                int startOffset = element.getTextRange().getStartOffset();
                while (m.find())
                {
                    MatchResult mr = m.toMatchResult();
                    TextRange tr = new TextRange(startOffset + mr.start(), startOffset + mr.end());
                    holder.createInfoAnnotation(tr, null).setTextAttributes(DustSyntaxHighlighter.TODO);
                }
            }
        }
    }

    private static void checkMatchingCloseTag(DustOpenTag openTag, AnnotationHolder holder)
    {
        if (openTag == null)
        {
            return;
        }

        String openTagName = getTagName(openTag);
        PsiElement sibling = openTag.getNextSibling();
        DustCloseTag closeTag = null;
        while (sibling != null)
        {
            if (sibling instanceof DustCloseTag)
            {
                closeTag = (DustCloseTag) sibling;
                if (getTagName(closeTag).equals(openTagName))
                {
                    return;
                }
            }
            sibling = sibling.getNextSibling();
        }

        holder.createErrorAnnotation(openTag.getTextRange(), "Could not find matching closing tag " + getTagName(openTag));

        if (closeTag != null)
        {
            holder.createErrorAnnotation(closeTag.getTextRange(), "Could not find matching opening tag " + getTagName(closeTag));
        }
    }

    private static String getTagName(PsiElement tag)
    {
        return tag.getChildren()[0].getText().trim();
    }
}
