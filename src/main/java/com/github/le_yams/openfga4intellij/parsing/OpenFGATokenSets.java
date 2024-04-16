package com.github.le_yams.openfga4intellij.parsing;

import com.github.le_yams.openfga4intellij.psi.OpenFGATypes;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.rd.generator.nova.Class;

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
            OpenFGATypes.WITH
    );

    TokenSet COMMENTS = TokenSet.create(
            OpenFGATypes.HEADER_MULTI_LINE_COMMENT,
            OpenFGATypes.MULTI_LINE_COMMENT
    );

    TokenSet SINGLE_LINE_COMMENT = TokenSet.create(OpenFGATypes.COMMENT);

    TokenSet SCHEMA_VERSIONS = TokenSet.create(
            OpenFGATypes.SCHEMA_VERSION
    );
}
