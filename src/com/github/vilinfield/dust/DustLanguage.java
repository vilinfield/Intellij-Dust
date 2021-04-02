package com.github.vilinfield.dust;

import com.intellij.lang.Language;

public class DustLanguage extends Language
{
    public static final DustLanguage INSTANCE = new DustLanguage();

    private DustLanguage()
    {
        super("Dust");
    }
}
