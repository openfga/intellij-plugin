package com.github.le_yams.openfga4intellij;

import com.github.le_yams.openfga4intellij.parsing.OpenFGATokenSets;
import com.github.le_yams.openfga4intellij.psi.OpenFGATypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class OpenFGASyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("OPENFGA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey SCHEMA_VERSIONS =
            createTextAttributesKey("OPENFGA_SCHEMA_VERSIONS", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey MODULE_NAME =
            createTextAttributesKey("OPENFGA_MODULE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey RELATION_NAME =
            createTextAttributesKey("OPENFGA_RELATION_NAME", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey RELATION_REFERENCE =
            createTextAttributesKey("OPENFGA_RELATION_REFERENCE", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey TYPE_IDENTIFIER =
            createTextAttributesKey("OPENFGA_TYPE_IDENTIFIER", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey TYPE_REFERENCE =
            createTextAttributesKey("OPENFGA_TYPE_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey CONDITION_IDENTIFIER =
            createTextAttributesKey("OPENFGA_CONDITION_IDENTIFIER", DefaultLanguageHighlighterColors.CLASS_NAME);
    public static final TextAttributesKey CONDITION_REFERENCE =
            createTextAttributesKey("OPENFGA_CONDITION_REFERENCE", DefaultLanguageHighlighterColors.CLASS_REFERENCE);
    public static final TextAttributesKey CONDITION_PARAM_IDENTIFIER =
            createTextAttributesKey("OPENFGA_CONDITION_PARAM_IDENTIFIER", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
    public static final TextAttributesKey CONDITION_PARAM_TYPE =
            createTextAttributesKey("OPENFGA_CONDITION_PARAM_TYPE", DefaultLanguageHighlighterColors.PARAMETER);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("OPENFGA_COMMENTS", DefaultLanguageHighlighterColors.LINE_COMMENT);

    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] MODULE_NAME_KEY = new TextAttributesKey[]{MODULE_NAME};
    private static final TextAttributesKey[] SCHEMA_VERSIONS_KEYS = new TextAttributesKey[]{SCHEMA_VERSIONS};
    private static final TextAttributesKey[] RELATION_NAME_KEYS = new TextAttributesKey[]{RELATION_NAME};
    private static final TextAttributesKey[] RELATION_REFERENCE_KEYS = new TextAttributesKey[]{RELATION_REFERENCE};
    private static final TextAttributesKey[] TYPE_IDENTIFIER_KEYS = new TextAttributesKey[]{TYPE_IDENTIFIER};
    private static final TextAttributesKey[] TYPE_REFERENCE_KEYS = new TextAttributesKey[]{TYPE_REFERENCE};
    private static final TextAttributesKey[] CONDITION_IDENTIFIER_KEYS = new TextAttributesKey[]{CONDITION_IDENTIFIER};
    private static final TextAttributesKey[] CONDITION_REFERENCE_KEYS = new TextAttributesKey[]{CONDITION_REFERENCE};
    private static final TextAttributesKey[] CONDITION_PARAM_KEYS = new TextAttributesKey[]{CONDITION_PARAM_IDENTIFIER};
    private static final TextAttributesKey[] CONDITION_PARAM_TYPES = new TextAttributesKey[]{CONDITION_PARAM_TYPE};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new OpenFGALexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {

        if (OpenFGATokenSets.SCHEMA_VERSIONS.contains(tokenType)) {
            return SCHEMA_VERSIONS_KEYS;
        }

        if (tokenType.equals(OpenFGATypes.MODULE_NAME)) {
            return MODULE_NAME_KEY;
        }

        if (tokenType.equals(OpenFGATypes.TYPE_IDENTIFIER)) {
            return TYPE_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.RELATION_DEF_TYPE_RESTRICTION)) {
            return TYPE_REFERENCE_KEYS;
        }

        if (tokenType.equals(OpenFGATypes.RELATION_NAME)) {
            return RELATION_NAME_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.REWRITE_TUPLESET_NAME)) {
            return RELATION_REFERENCE_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.REWRITE_COMPUTEDUSERSET_NAME)) {
            return RELATION_REFERENCE_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.REWRITE_TUPLESET_COMPUTEDUSERSET_NAME)) {
            return RELATION_REFERENCE_KEYS;
        }

        if (tokenType.equals(OpenFGATypes.CONDITION_NAME_REF)) {
            return CONDITION_REFERENCE_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.CONDITION_NAME)) {
            return CONDITION_IDENTIFIER_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.PARAMETER_NAME)) {
            return CONDITION_PARAM_KEYS;
        }
        if (tokenType.equals(OpenFGATypes.PARAMETER_TYPE)) {
            return CONDITION_PARAM_TYPES;
        }

        if (OpenFGATokenSets.KEYWORDS.contains(tokenType)) {
            return KEYWORD_KEYS;
        }
        if (OpenFGATokenSets.COMMENTS.contains(tokenType)) {
            return COMMENT_KEYS;
        }

        return EMPTY_KEYS;
    }

}