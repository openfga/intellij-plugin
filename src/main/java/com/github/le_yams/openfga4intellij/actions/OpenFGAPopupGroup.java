package com.github.le_yams.openfga4intellij.actions;

import com.github.le_yams.openfga4intellij.OpenFGALanguage;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.jetbrains.annotations.NotNull;

public class OpenFGAPopupGroup extends DefaultActionGroup {
    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        var psiFile = event.getData(CommonDataKeys.PSI_FILE);
        var openfgaFile = psiFile != null && psiFile.getLanguage().isKindOf(OpenFGALanguage.INSTANCE);
        event.getPresentation().setVisible(openfgaFile);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}
