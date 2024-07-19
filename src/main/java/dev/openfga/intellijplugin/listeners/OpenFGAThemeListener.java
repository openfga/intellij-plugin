package dev.openfga.intellijplugin.listeners;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import org.jetbrains.annotations.NotNull;

public class OpenFGAThemeListener implements LafManagerListener {
    private final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();

    private String previousUI =
            LafManager.getInstance().getCurrentUIThemeLookAndFeel().getName();

    @Override
    public void lookAndFeelChanged(@NotNull LafManager source) {
        String currentUI =
                LafManager.getInstance().getCurrentUIThemeLookAndFeel().getName();
        if (!previousUI.equals(currentUI)) {
            if (currentUI.equals("OpenFGA Dark")) {
                editorColorsManager.setGlobalScheme(editorColorsManager.getScheme(currentUI));
            }
        }
        previousUI = currentUI;
    }
}
