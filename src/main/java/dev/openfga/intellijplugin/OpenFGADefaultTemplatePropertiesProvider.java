package dev.openfga.intellijplugin;

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider;
import com.intellij.psi.PsiDirectory;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;

public class OpenFGADefaultTemplatePropertiesProvider implements DefaultTemplatePropertiesProvider {
    @Override
    public void fillProperties(@NotNull PsiDirectory directory, @NotNull Properties properties) {
        properties.put("OPENFGA_MODEL_VERSION", "1.1");
    }
}
