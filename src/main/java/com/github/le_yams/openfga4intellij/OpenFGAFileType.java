package com.github.le_yams.openfga4intellij;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class OpenFGAFileType extends LanguageFileType {

    public static final OpenFGAFileType INSTANCE = new OpenFGAFileType();

  private OpenFGAFileType() {
        super(OpenFGALanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "OpenFGA File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OpenFGA DSL file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "fga";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return OpenFGAIcons.FILE;
    }

}

