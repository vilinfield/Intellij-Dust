package com.github.vilinfield.dust;

import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper;
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock;
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Based on the intellij mustache plugin
 */
public class DustFormattingModelBuilder extends TemplateLanguageFormattingModelBuilder
{
    @Override
    public TemplateLanguageBlock createTemplateLanguageBlock(@NotNull ASTNode node,
                                                             @Nullable Wrap wrap,
                                                             @Nullable Alignment alignment,
                                                             @Nullable List<DataLanguageBlockWrapper> foreignChildren,
                                                             @NotNull CodeStyleSettings codeStyleSettings)
    {
        return new DustFormatterBlock(this, codeStyleSettings, node, foreignChildren);
    }

    /**
     * We have to override {@link com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder#createModel}
     * since after we delegate to some templated languages, those languages (xml/html for sure, potentially others)
     * delegate right back to us to format the DustTypes.OUTER_TYPE token we tell them to ignore,
     * causing an stack-overflowing loop.
     */
    @NotNull
    public FormattingModel createModel(FormattingContext formattingContext)
    {
        final PsiFile file = formattingContext.getPsiElement().getContainingFile();
        Block rootBlock;

        ASTNode node = formattingContext.getPsiElement().getNode();

        if (node.getElementType() == DustFileViewProvider.OUTER_TYPE)
        {
            // If we're looking at a DustTypes.HTML element, then we've been invoked by our templated
            // language.  Make a dummy block to allow that formatter to continue.
            return new SimpleTemplateLanguageFormattingModelBuilder().createModel(formattingContext);
        }
        else
        {
            rootBlock = getRootBlock(file, file.getViewProvider(), formattingContext.getCodeStyleSettings());
        }

        return new DocumentBasedFormattingModel(rootBlock, formattingContext.getPsiElement().getProject(),
                formattingContext.getCodeStyleSettings(), file.getFileType(), file);
    }

    @Override
    public boolean dontFormatMyModel()
    {
        return false;
    }
}
