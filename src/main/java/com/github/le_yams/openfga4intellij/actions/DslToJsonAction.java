package com.github.le_yams.openfga4intellij.actions;

import com.github.le_yams.openfga4intellij.OpenFGALanguage;
import com.github.le_yams.openfga4intellij.cli.tasks.DslToJsonTask;
import com.github.le_yams.openfga4intellij.settings.OpenFGASettingsState;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;

public class DslToJsonAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        super.update(event);
        var psiFile = event.getData(CommonDataKeys.PSI_FILE);
        var openfgaFile = psiFile != null && psiFile.getLanguage().isKindOf(OpenFGALanguage.INSTANCE);
        event.getPresentation().setVisible(openfgaFile);

        if (openfgaFile) {
            var settings = OpenFGASettingsState.getInstance();
            var cliIsConfigured = settings.cliPath != null && !settings.cliPath.isEmpty();
            event.getPresentation().setEnabled(cliIsConfigured);
            var description = cliIsConfigured
                    ? "generate " + DslToJsonTask.computeJsonGeneratedFileName(psiFile) + " file"
                    : "Please configure openfga cli path";
            event.getPresentation().setDescription(description);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        var dslFile = event.getData(CommonDataKeys.PSI_FILE);
        if (dslFile == null) {
            return;
        }

        var virtualFile = dslFile.getVirtualFile();
        if (virtualFile == null) {
            return;
        }
        DslToJsonTask.create(dslFile, virtualFile.toNioPath())
                .ifPresent(ProgressManager.getInstance()::run);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}