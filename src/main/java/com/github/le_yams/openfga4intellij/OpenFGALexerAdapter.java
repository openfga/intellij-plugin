package com.github.le_yams.openfga4intellij;

import com.github.le_yams.openfga4intellij.parsing.OpenFGALexer;
import com.intellij.lexer.FlexAdapter;

public class OpenFGALexerAdapter extends FlexAdapter {

    public OpenFGALexerAdapter() {
        super(new OpenFGALexer(null));
    }

}