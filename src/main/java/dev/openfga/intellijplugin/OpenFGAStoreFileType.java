package dev.openfga.intellijplugin;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.psi.PsiFile;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLLanguage;

public class OpenFGAStoreFileType extends LanguageFileType {

    public static final OpenFGAStoreFileType INSTANCE = new OpenFGAStoreFileType();

    /**
     * Returns whether the given file is an OpenFGA store file, i.e. a YAML file whose name ends
     * with {@code fga.yaml} or {@code openfga.yaml}.
     */
    public static boolean isStoreFile(@Nullable final PsiFile file) {
        if (file == null) {
            return false;
        }

        final String name = file.getName();

        return name.endsWith("fga.yaml") || name.endsWith("openfga.yaml");
    }

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
