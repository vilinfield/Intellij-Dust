package com.github.vilinfield.dust;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import com.github.vilinfield.dust.psi.DustTypes;
import org.jetbrains.annotations.NotNull;

public class DustCompletionContributor extends CompletionContributor
{
    public DustCompletionContributor()
    {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement(DustTypes.IDENTIFIER).withLanguage(DustLanguage.INSTANCE),
                new CompletionProvider<>()
                {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet)
                    {
                        resultSet.addElement(LookupElementBuilder.create("if"));
                        resultSet.addElement(LookupElementBuilder.create("select"));
                        resultSet.addElement(LookupElementBuilder.create("log"));
                        resultSet.addElement(LookupElementBuilder.create("idx"));
                        resultSet.addElement(LookupElementBuilder.create("jsControl"));
                        resultSet.addElement(LookupElementBuilder.create("math"));
                        resultSet.addElement(LookupElementBuilder.create("lt"));
                        resultSet.addElement(LookupElementBuilder.create("gt"));
                        resultSet.addElement(LookupElementBuilder.create("ne"));
                        resultSet.addElement(LookupElementBuilder.create("eq"));
                        resultSet.addElement(LookupElementBuilder.create("pre.scss"));
                        resultSet.addElement(LookupElementBuilder.create("pre.layout"));
                        resultSet.addElement(LookupElementBuilder.create("pre.js"));
                        resultSet.addElement(LookupElementBuilder.create("pre.i18n"));
                        // TODO: add more keywords, think about writing reference based autocompletion.
                    }
                }
        );
    }
}
