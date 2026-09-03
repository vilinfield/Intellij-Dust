package com.github.vilinfield.dust;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class DustColorSettingsPage implements ColorSettingsPage
{
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Tag", DustSyntaxHighlighter.TAG),
            new AttributesDescriptor("Number", DustSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Identifier", DustSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("String", DustSyntaxHighlighter.STRING),
            new AttributesDescriptor("Comments", DustSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Todo", DustSyntaxHighlighter.TODO)
    };

    private static final Map<String, TextAttributesKey> additionalHighlightingMap = new HashMap<>();

    static
    {
        additionalHighlightingMap.put("todocomment", DustSyntaxHighlighter.TODO);
    }

    private final String demo;

    public DustColorSettingsPage()
    {
        demo = """
                {!
                    You are reading the DUST template example
                    <todocomment>TODO comment</todocomment>
                    more comments {somepath}
                !}
                {#person test=something keya=valuea keyb="linked{expression}in"}
                    {>"path/to/template"/}
                    {<someInlinePartial}
                        {?name}
                            {! Dust Comment !}
                            {@some.helper key="some key" text="some value"/}
                            {key}{key|s}{key|h}{key|s|h|u} {! filters !}
                            {?first}
                                {.first}
                                {subscript[0]}
                                {subscript[0].content}
                                {subscript[index.key].content}
                            {:else}
                                {#selfClosingSection/}
                            {/first}
                        {/name}
                    {/someInlinePartial}
                
                    {#items}
                        {>"partials-view" item=. /}
                        {@eq key=idx value=125}{/eq}
                        {0}
                    {/items}
                    {+selfClosingBlock/}
                    {+block}Default Value{/block}
                    {@helper-tag_test/}
                {/person}
                """;
    }

    @Nullable
    @Override
    public Icon getIcon()
    {
        return DustIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter()
    {
        return new DustSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText()
    {
        return demo;
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
    {
        return additionalHighlightingMap;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors()
    {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors()
    {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName()
    {
        return "Dust";
    }
}
