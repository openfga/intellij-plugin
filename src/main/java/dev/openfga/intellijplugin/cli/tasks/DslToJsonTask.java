package dev.openfga.intellijplugin.cli.tasks;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import dev.openfga.intellijplugin.cli.CliProcess;
import dev.openfga.intellijplugin.cli.CliProcessTask;
import dev.openfga.intellijplugin.cli.CliTaskException;
import dev.openfga.intellijplugin.settings.OpenFGASettingsState;
import dev.openfga.intellijplugin.util.notifications.Notifier;
import dev.openfga.intellijplugin.util.notifications.ProjectNotifier;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class DslToJsonTask extends Task.Backgroundable implements CliProcessTask<Void> {

    private final PsiFile dslFile;
    private final Path targetPath;
    private final CliProcess process;
    private final Notifier notifier;

    public static Optional<DslToJsonTask> create(@NotNull PsiFile dslFile, @NotNull Path dslFilePath) {
        var targetName = computeJsonGeneratedFileName(dslFile);
        var targetPath = dslFilePath.getParent().resolve(targetName);

        if (Files.exists(targetPath)) {
            var title = "Replace Existing File?";
            var message = "The file " + targetPath + " already exists. Overrides it?";
            if (Messages.showYesNoDialog(dslFile.getProject(), message, title, null) == Messages.NO) {
                return Optional.empty();
            }
        }

        return Optional.of(new DslToJsonTask(dslFile, dslFilePath, targetPath));
    }

    private DslToJsonTask(@NotNull PsiFile dslFile, @NotNull Path dslFilePath, @NotNull Path targetPath) {
        super(dslFile.getProject(), "Generating json model for " + dslFile.getName(), true);
        this.dslFile = dslFile;
        this.targetPath = targetPath;
        notifier = new ProjectNotifier(dslFile.getProject());

        process = new CliProcess(
                OpenFGASettingsState.getInstance().requireCli(),
                List.of(
                        "model",
                        "transform",
                        "--file",
                        dslFilePath.toString(),
                        "--input-format",
                        "fga"
                ));
    }

    public static String computeJsonGeneratedFileName(PsiFile dslFile) {
        return dslFile.getName().replaceAll("(fga)|(openfga)$", "json");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        try {
            process.start(indicator, this);
        } catch (CliTaskException e) {
            notifier.notifyError("Error generating json authorization model", e);
        }
    }

    @Override
    public void onCancel() {
        process.cancel();
    }

    @Override
    public Void onSuccess(File stdOutFile, File stdErrFile) throws IOException {
        Files.copy(stdOutFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        ApplicationManager.getApplication().invokeLater(
                () -> show(new GeneratedFile(dslFile.getProject(), targetPath)),
                ModalityState.nonModal());
        return null;
    }

    @Override
    public CliTaskException onFailure(File stdOutFile, File stdErrFile) {
        try {
            var errors = Files.readString(stdErrFile.toPath());
            return new CliTaskException(errors);
        } catch (IOException e) {
            return new CliTaskException("unexpected error");
        }
    }

    private void show(GeneratedFile generatedFile) {
        generatedFile.refreshInTreeView();
        generatedFile.openInEditor();

    }

    private static final class GeneratedFile {
        private final Project project;
        private final VirtualFile virtualFile;

        private GeneratedFile(Project project, Path path) {
            this.project = project;
            virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(path);
        }

        private void openInEditor() {
            var editorManager = FileEditorManager.getInstance(project);
            var editors = editorManager.openFile(virtualFile, true);

            for (var editor : editors) {
                editor.getFile().refresh(false, false);
            }

            PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
            new ReformatCodeProcessor(project, psiFile, null, false).run();
        }

        private void refreshInTreeView() {
            virtualFile.getParent().refresh(false, false);
        }
    }
}
