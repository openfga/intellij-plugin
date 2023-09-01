package com.github.le_yams.openfga4intellij.settings;

import com.github.le_yams.openfga4intellij.cli.CliTaskException;
import com.github.le_yams.openfga4intellij.cli.Cli;
import com.github.le_yams.openfga4intellij.cli.tasks.ReadCliVersionTask;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.ComponentValidator;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class OpenFGASettingsComponent {
    private static final Logger logger = Logger.getInstance(OpenFGASettingsComponent.class);
    private final JPanel myMainPanel;
    private final TextFieldWithBrowseButton cliPathField = new TextFieldWithBrowseButton();
    private final JBLabel cliVersionLabel = new JBLabel();
    private final List<ComponentValidator> validators = new ArrayList<>();

    public OpenFGASettingsComponent() {
        var clearCliPathField = new JButton("Clear");
        var cliPathPanel = new JPanel(new BorderLayout());
        cliPathPanel.add(cliPathField, BorderLayout.CENTER);
        cliPathPanel.add(clearCliPathField, BorderLayout.EAST);

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("OpenFGA cli path: "), cliPathPanel, 1, false)
                .addComponent(cliVersionLabel)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        clearCliPathField.addActionListener(evt -> cliPathField.setText(""));
        cliPathField.setEditable(false);
        cliPathField.addBrowseFolderListener("Select", "Select OpenFGA cli path", null, new FileChooserDescriptor(
                true, false, false, false, false, false
        ));

        installValidator(cliPathField, this::validateCliPath);
        cliPathField.getTextField().getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent event) {
                ComponentValidator.getInstance(cliPathField).ifPresent(validator -> {
                    validator.revalidate();
                    var cliPathInput = cliPathField.getText();
                    cliVersionLabel.setIcon(null);
                    cliVersionLabel.setText("");
                    if (validator.getValidationInfo() == null && !cliPathInput.isEmpty()) {
                        updateDetectedVersion(cliPathInput);
                    }
                });
            }
        });
    }

    private void updateDetectedVersion(String cliPathInput) {
        var cliPath = Paths.get(cliPathInput);
        var version = detectCliVersion(cliPath);
        notifiedDetectedVersion(version);
    }

    private String detectCliVersion(Path cliPath) {
        try {
            return ProgressManager.getInstance()
                    .run(new ReadCliVersionTask(cliPathField, new Cli(cliPath)))
                    .orElse(null);
        } catch (CliTaskException ex) {
            logger.info("could not detect OpenFGA cli version for " + cliPath, ex);
            return null;
        }
    }

    private void notifiedDetectedVersion(String version) {
        if (version != null) {
            cliVersionLabel.setIcon(AllIcons.RunConfigurations.TestPassed);
            cliVersionLabel.setText("Detected version: " + version);
        } else {
            cliVersionLabel.setIcon(AllIcons.General.Warning);
            cliVersionLabel.setText("Could not detect cli version, make sure it actually is a valid OpenFGA cli binary");
        }
    }

    private <T extends JComponent & Disposable> void installValidator(T component, Supplier<ValidationInfo> validator) {
        var componentValidator = new ComponentValidator(component)
                .withValidator(validator)
                .installOn(component);
        validators.add(componentValidator);
    }

    private ValidationInfo validateCliPath() {
        return validateCliPath(cliPathField.getText());
    }

    private ValidationInfo validateCliPath(String cliPathInput) {
        if (cliPathInput.isEmpty()) {
            return null;
        }
        var cliPath = Paths.get(cliPathInput);

        if (!Files.exists(cliPath)) {
            return new ValidationInfo("The file does not exist", cliPathField);
        }
        if (Files.isDirectory(cliPath)) {
            return new ValidationInfo("The file is a directory", cliPathField);
        }
        return null;
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return cliPathField;
    }

    @NotNull
    public String getCliPathText() {
        return cliPathField.getText();
    }

    public void setCliPathText(@NotNull String newText) {
        cliPathField.setText(newText);
    }

    public boolean isValid() {
        return validators.stream()
                .map(ComponentValidator::getValidationInfo)
                .allMatch(Objects::isNull);
    }
}
