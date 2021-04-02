package com.github.vilinfield.dust.psi;

import com.intellij.psi.tree.IElementType;
import com.github.vilinfield.dust.DustLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DustElementType extends IElementType
{
    public DustElementType(@NotNull @NonNls String debugName)
    {
        super(debugName, DustLanguage.INSTANCE);
    }
}
