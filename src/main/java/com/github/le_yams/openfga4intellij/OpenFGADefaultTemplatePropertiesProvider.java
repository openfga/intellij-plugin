package com.github.le_yams.openfga4intellij;

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class OpenFGADefaultTemplatePropertiesProvider implements DefaultTemplatePropertiesProvider {
    @Override
    public void fillProperties(@NotNull PsiDirectory directory, @NotNull Properties properties) {
        properties.put("OPENFGA_MODEL_VERSION", "1.1");
    }
}
