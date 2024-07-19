package dev.openfga.intellijplugin.parsing;

import com.intellij.psi.tree.IElementType;
import dev.openfga.intellijplugin.OpenFGALanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OpenFGATokenType extends IElementType {

    public OpenFGATokenType(@NotNull @NonNls String debugName) {
        super(debugName, OpenFGALanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "OpenFGA." + super.toString();
    }
}
