package dev.openfga.intellijplugin;

import dev.openfga.intellijplugin.parsing.OpenFGALexer;
import com.intellij.lexer.FlexAdapter;

public class OpenFGALexerAdapter extends FlexAdapter {

    public OpenFGALexerAdapter() {
        super(new OpenFGALexer(null));
    }

}