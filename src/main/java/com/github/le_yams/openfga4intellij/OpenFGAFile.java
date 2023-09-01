package com.github.le_yams.openfga4intellij;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class OpenFGAFile extends PsiFileBase {

    public OpenFGAFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, OpenFGALanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return OpenFGAFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return getFileType().getName();
    }

}
