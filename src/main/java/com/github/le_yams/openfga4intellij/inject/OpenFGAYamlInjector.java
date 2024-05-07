package com.github.le_yams.openfga4intellij.inject;

import com.github.le_yams.openfga4intellij.OpenFGALanguage;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLScalarList;

import java.util.List;

public class OpenFGAYamlInjector implements MultiHostInjector {
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {

        if (!(context instanceof YAMLKeyValue && shouldInject(context))) {
            return;
        }

        PsiLanguageInjectionHost host = (PsiLanguageInjectionHost) ((YAMLKeyValue) context).getValue();
        registrar.startInjecting(OpenFGALanguage.INSTANCE);
        registrar.addPlace(null, null, host, ElementManipulators.getValueTextRange(host));
        registrar.doneInjecting();
    }

    private boolean shouldInject(PsiElement context) {
        return context instanceof YAMLKeyValue &&
                (context.getContainingFile().getName().endsWith("fga.yaml") ||
                context.getContainingFile().getName().endsWith("openfga.yaml")) &&
                ((YAMLKeyValue) context).getValue() instanceof YAMLScalarList &&
                ((YAMLKeyValue) context).getKeyText().equals("model");
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return List.of(YAMLKeyValue.class);
    }
}
