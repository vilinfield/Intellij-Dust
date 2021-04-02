package com.github.vilinfield.dust.psi;

import com.intellij.psi.tree.IElementType;
import com.github.vilinfield.dust.DustLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DustTokenType extends IElementType
{
    public DustTokenType(@NotNull @NonNls String debugName)
    {
        super(debugName, DustLanguage.INSTANCE);
    }

    @Override
    public String toString()
    {
        return "DustTokenType." + super.toString();
    }
}
