package dev.openfga.intellijplugin.util.notifications;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;

public class ToolWindowNotifier {

    private final ToolWindow toolWindow;

    public ToolWindowNotifier(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }

    public void notifyError(Throwable throwable) {
        notifyError(throwable.getMessage());
    }

    public void notifyError(String message) {
        notify(MessageType.ERROR, message);
    }

    public void notify(MessageType messageType, String message) {
        SwingUtilities.invokeLater(() -> ToolWindowManager
                .getInstance(toolWindow.getProject())
                .notifyByBalloon(toolWindow.getId(), messageType, message));
    }
}
