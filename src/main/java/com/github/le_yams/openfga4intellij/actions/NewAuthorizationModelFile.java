package com.github.le_yams.openfga4intellij.actions;

import com.github.le_yams.openfga4intellij.OpenFGAIcons;
import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.actions.AttributesDefaults;
import com.intellij.ide.fileTemplates.ui.CreateFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class NewAuthorizationModelFile extends CreateFileFromTemplateAction {
    public NewAuthorizationModelFile() {
        super("Authorization Model File", "Creates an OpenFGA authorization model DSL File", OpenFGAIcons.FILE);
    }

    @Override
    protected String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return "Authorization Model File";
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder
                .setTitle("Authorization Model File")
                .addKind("File", OpenFGAIcons.FILE, "OpenFGA Authorization Model");
    }

    @Override
    protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir) {
        var project = dir.getProject();
        return new CreateFromTemplateDialog(project, dir, template, new AttributesDefaults(name).withFixedName(true), null)
                .create()
                .getContainingFile();
    }
}
