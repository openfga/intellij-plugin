package com.github.le_yams.openfga4intellij.parsing;

import com.github.le_yams.openfga4intellij.psi.OpenFGATypes;
import com.intellij.psi.tree.TokenSet;

public interface OpenFGATokenSets {

    TokenSet KEYWORDS = TokenSet.create(
            OpenFGATypes.MODEL,
            OpenFGATypes.SCHEMA,
            OpenFGATypes.TYPE,
            OpenFGATypes.RELATIONS,
            OpenFGATypes.DEFINE,
            OpenFGATypes.OR,
            OpenFGATypes.FROM
    );
    TokenSet COMMENTS = TokenSet.create(
            OpenFGATypes.SINGLE_LINE_COMMENT,
            OpenFGATypes.HEADER_MULTI_LINE_COMMENT,
            OpenFGATypes.MULTI_LINE_COMMENT
    );

    TokenSet SINGLE_LINE_COMMENT = TokenSet.create(OpenFGATypes.COMMENT);

    TokenSet SCHEMA_VERSIONS = TokenSet.create(
            OpenFGATypes.SCHEMA_VERSION_V1_1
    );
}
