package dev.openfga.intellijplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLLanguage;

public class OpenFGAStoreFileType extends LanguageFileType {

    public static final OpenFGAStoreFileType INSTANCE = new OpenFGAStoreFileType();

    private OpenFGAStoreFileType() {
        super(YAMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "OpenFGA Store File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "OpenFGA Store file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "fga.yaml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return OpenFGAIcons.FILE;
    }
}
