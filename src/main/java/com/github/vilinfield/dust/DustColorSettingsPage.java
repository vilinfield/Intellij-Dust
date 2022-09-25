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

    private String demo = "";

    public DustColorSettingsPage()
    {
        demo = "{!\n" +
                "    You are reading the DUST template example\n" +
                "    <todocomment>TODO comment</todocomment> \n" +
                "    more comments {somepath}\n" +
                "!}\n" +
                "{#person test=something keya=valuea keyb=\"linked{expression}in\"}\n" +
                "    {>\"path/to/template\"/}\n" +
                "    {<someInlinePartial}\n" +
                "        {?name}\n" +
                "            {! Dust Comment !}\n" +
                "            {@some.helper key=\"some key\" text=\"some value\"/}\n" +
                "            {key}{key|s}{key|h}{key|s|h|u} {! filters !}\n" +
                "            {?first}\n" +
                "                {.first}\n" +
                "                {subscript[0]}\n" +
                "                {subscript[0].content}\n" +
                "                {subscript[index.key].content}\n" +
                "            {:else}\n" +
                "                {#selfClosingSection/}\n" +
                "            {/first}\n" +
                "        {/name}\n" +
                "    {/someInlinePartial}\n" +
                "\n" +
                "    {#items}\n" +
                "        {>\"partials-view\" item=. /}\n" +
                "        {@eq key=idx value=125}{/eq}\n" +
                "        {0}\n" +
                "    {/items}\n" +
                "    {+selfClosingBlock/}\n" +
                "    {+block}Default Value{/block}\n" +
                "    {@helper-tag_test/}\n" +
                "{/person}\n";
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

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors()
    {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors()
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
