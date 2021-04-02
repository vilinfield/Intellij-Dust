package com.github.vilinfield.dust;

import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.lang.Language;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.github.vilinfield.dust.psi.DustTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DustLayeredSyntaxHighlighter extends LayeredLexerEditorHighlighter
{
    public DustLayeredSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile,
                                        @NotNull EditorColorsScheme colors)
    {
        // Create main highlighter.
        super(new DustSyntaxHighlighter(), colors);

        // Highlighter for outer lang.
        FileType type = null;
        if (project == null || virtualFile == null)
        {
            type = FileTypes.PLAIN_TEXT;
        }
        else
        {
            Language language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile);
            if (language != null)
            {
                type = language.getAssociatedFileType();
            }
            if (type == null)
            {
                type = HtmlFileType.INSTANCE;
            }
        }
        SyntaxHighlighter outerHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(type, project, virtualFile);

        if (outerHighlighter != null)
        {
            registerLayer(DustTypes.HTML, new LayerDescriptor(outerHighlighter, ""));
        }
    }
}
