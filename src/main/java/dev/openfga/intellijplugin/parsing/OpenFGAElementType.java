package dev.openfga.intellijplugin.parsing;

import dev.openfga.intellijplugin.OpenFGALanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;

public class OpenFGAElementType extends IElementType {
    public OpenFGAElementType(@NonNls String debugName) {
        super(debugName, OpenFGALanguage.INSTANCE);
    }
}
