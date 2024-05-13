package dev.openfga.intellijplugin.servers.ui;

import dev.openfga.intellijplugin.servers.service.OpenFGAServers;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class OpenFGAToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var content = ContentFactory.getInstance().createContent(
                new ServersTreePanel(toolWindow, OpenFGAServers.getInstance()),
                "Servers",
                false);
        toolWindow.getContentManager().addContent(content);
    }
}
