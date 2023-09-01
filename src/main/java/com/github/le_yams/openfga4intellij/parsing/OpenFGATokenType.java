package com.github.le_yams.openfga4intellij.parsing;

import com.github.le_yams.openfga4intellij.OpenFGALanguage;
import com.intellij.psi.tree.IElementType;
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
