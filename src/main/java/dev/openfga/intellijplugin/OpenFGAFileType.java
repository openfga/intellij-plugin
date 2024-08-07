package dev.openfga.intellijplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenFGAFileType extends LanguageFileType {

    public static final OpenFGAFileType INSTANCE = new OpenFGAFileType();

    private OpenFGAFileType() {
        super(OpenFGALanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "OpenFGA Model File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OpenFGA Model file";
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
