package dev.openfga.intellijplugin.settings;

import com.intellij.openapi.options.Configurable;
import java.util.Objects;
import javax.swing.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class OpenFGASettingsConfigurable implements Configurable {

    private OpenFGASettingsComponent openFGASettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "OpenFGA";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return openFGASettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        openFGASettingsComponent = new OpenFGASettingsComponent();
        return openFGASettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        OpenFGASettingsState settings = OpenFGASettingsState.getInstance();
        if (!openFGASettingsComponent.isValid()) {
            return false;
        }

        return !Objects.equals(openFGASettingsComponent.getCliPathText(), settings.cliPath);
    }

    @Override
    public void apply() {
        OpenFGASettingsState settings = OpenFGASettingsState.getInstance();
        settings.cliPath = openFGASettingsComponent.getCliPathText();
    }

    @Override
    public void reset() {
        OpenFGASettingsState settings = OpenFGASettingsState.getInstance();
        openFGASettingsComponent.setCliPathText(settings.cliPath);
    }

    @Override
    public void disposeUIResources() {
        openFGASettingsComponent = null;
    }
}
