package com.github.le_yams.openfga4intellij.parsing;

import com.github.le_yams.openfga4intellij.OpenFGALanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;

public class OpenFGAElementType extends IElementType {
    public OpenFGAElementType(@NonNls String debugName) {
        super(debugName, OpenFGALanguage.INSTANCE);
    }
}
