package com.github.vilinfield.dust;

import com.intellij.openapi.fileTypes.FileTypeEditorHighlighterProviders;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DustFileType extends LanguageFileType
{
    public static final DustFileType INSTANCE = new DustFileType();
    @NonNls
    public static final String DEFAULT_EXTENSION = "dust";

    private DustFileType()
    {
        super(DustLanguage.INSTANCE);
        // Register highlighter - lazy singleton factory.
        FileTypeEditorHighlighterProviders.INSTANCE.addExplicitExtension(this,
                (project, fileType, virtualFile, editorColorsScheme) -> new DustLayeredSyntaxHighlighter(project, virtualFile, editorColorsScheme));
    }

    @Override
    @NotNull
    public String getName()
    {
        return "Dust";
    }

    @Override
    @NotNull
    public String getDescription()
    {
        return "Dust template";
    }

    @Override
    @NotNull
    public String getDefaultExtension()
    {
        return DEFAULT_EXTENSION;
    }

    @Override
    public Icon getIcon()
    {
        return DustIcons.FILE;
    }
}
