package dev.openfga.intellijplugin.parsing;

import com.intellij.psi.tree.TokenSet;
import dev.openfga.intellijplugin.psi.OpenFGATypes;

public interface OpenFGATokenSets {

    TokenSet KEYWORDS = TokenSet.create(
            OpenFGATypes.MODULE,
            OpenFGATypes.MODEL,
            OpenFGATypes.SCHEMA,
            OpenFGATypes.TYPE,
            OpenFGATypes.EXTEND,
            OpenFGATypes.RELATIONS,
            OpenFGATypes.DEFINE,
            OpenFGATypes.AND,
            OpenFGATypes.OR,
            OpenFGATypes.BUT_NOT,
            OpenFGATypes.FROM,
            OpenFGATypes.CONDITION,
            OpenFGATypes.WITH);

    TokenSet COMMENTS = TokenSet.create(OpenFGATypes.HEADER_MULTI_LINE_COMMENT, OpenFGATypes.MULTI_LINE_COMMENT);

    TokenSet SINGLE_LINE_COMMENT = TokenSet.create(OpenFGATypes.COMMENT);

    TokenSet SCHEMA_VERSIONS = TokenSet.create(OpenFGATypes.SCHEMA_VERSION);
}
