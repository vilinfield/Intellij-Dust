package com.github.vilinfield.dust.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.github.vilinfield.dust.DustFileType;
import com.github.vilinfield.dust.DustLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DustFile extends PsiFileBase
{
    public DustFile(@NotNull FileViewProvider viewProvider)
    {
        super(viewProvider, DustLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType()
    {
        return DustFileType.INSTANCE;
    }

    @Override
    public String toString()
    {
        return "Dust File";
    }

    @Override
    public Icon getIcon(int flags)
    {
        return super.getIcon(flags);
    }
}
