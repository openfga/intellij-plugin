package dev.openfga.intellijplugin;

import com.intellij.lexer.FlexAdapter;
import dev.openfga.intellijplugin.parsing.OpenFGALexer;

public class OpenFGALexerAdapter extends FlexAdapter {

    public OpenFGALexerAdapter() {
        super(new OpenFGALexer(null));
    }
}
