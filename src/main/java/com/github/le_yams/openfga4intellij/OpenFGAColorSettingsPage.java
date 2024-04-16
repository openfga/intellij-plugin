package com.github.le_yams.openfga4intellij;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class OpenFGAColorSettingsPage  implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keyword", OpenFGASyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Comment", OpenFGASyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Type identifier", OpenFGASyntaxHighlighter.TYPE_IDENTIFIER),
            new AttributesDescriptor("Relation name", OpenFGASyntaxHighlighter.RELATION_NAME),
            new AttributesDescriptor("Schema version", OpenFGASyntaxHighlighter.SCHEMA_VERSIONS),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return OpenFGAIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new OpenFGASyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return  "# Administrator model\n" +
                "model\n" +
                "  schema 1.1\n" +
                "\n" +
                "type user\n" +
                "type organization\n" +
                "  relations\n" +
                "    define administrator : [user]\n" +
                "    define entity_of: [entity]\n" +
                "    define member: administrator or member from entity_of";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "OpenFGA";
    }

}