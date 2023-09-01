package com.github.le_yams.openfga4intellij;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class OpenFGATemplateContext extends TemplateContextType {
    protected OpenFGATemplateContext() {
        super("OPENFGA_CONTEXT", "OpenFGA authorization model DSL");
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getLanguage().isKindOf(OpenFGALanguage.INSTANCE);
    }
}
