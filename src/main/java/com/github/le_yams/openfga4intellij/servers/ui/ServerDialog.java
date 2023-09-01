package com.github.le_yams.openfga4intellij.servers.ui;

import com.github.le_yams.openfga4intellij.servers.util.ServersUtil;
import com.github.le_yams.openfga4intellij.servers.model.AuthenticationMethod;
import com.github.le_yams.openfga4intellij.servers.model.Oidc;
import com.github.le_yams.openfga4intellij.servers.model.Server;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.AnimatedIcon;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class ServerDialog extends DialogWrapper {
    private static final Logger logger = Logger.getInstance(com.github.le_yams.openfga4intellij.servers.ui.ServerDialog.class);
    private final ToolWindow toolWindow;
    private final Server server;

    private DialogPanel dialogPanel;
    private final JBTextField nameField = new JBTextField();
    private final JBTextField urlField = new JBTextField();
    private final ComboBox<AuthenticationMethod> authenticationMethodField = new ComboBox<>(AuthenticationMethod.values());
    private final JBPasswordField apiTokenField = new JBPasswordField();
    private final JBTextField oidcClientIdField = new JBTextField();
    private final JBPasswordField oidcClientSecretField = new JBPasswordField();
    private final JBTextField oidcTokenEndpointField = new JBTextField();
    private final JBTextField oidcScopeField = new JBTextField();
    private final ActionLink connectionTestButton = new ActionLink("Test connexion");
    private final JBLabel connectionTestLabel = new JBLabel();

    protected ServerDialog(ToolWindow toolWindow) {
        this(toolWindow, null);
    }

    public ServerDialog(ToolWindow toolWindow, @Nullable Server server) {
        super(true);
        this.toolWindow = toolWindow;
        this.server = server != null ? server : new Server();
        setTitle(server != null ? "Edit Server" : "Add Server");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        dialogPanel = new DialogPanel(new MigLayout("fillx,wrap 2", "[left]rel[grow,fill]"));

        dialogPanel.add(new JBLabel("Name"));
        dialogPanel.add(nameField);

        dialogPanel.add(new JBLabel("Url"));
        dialogPanel.add(urlField);

        dialogPanel.add(new JBLabel("Authentication method"));
        dialogPanel.add(authenticationMethodField, "grow 0");

        dialogPanel.add(createAuthenticationPanel(), "span, grow");

        var connectionTestPanel = new JPanel(new BorderLayout(3, 0));

        connectionTestPanel.add(connectionTestButton, BorderLayout.WEST);
        connectionTestPanel.add(connectionTestLabel, BorderLayout.CENTER);
        dialogPanel.add(connectionTestPanel, "span, grow");

        connectionTestButton.addActionListener(evt -> {
            var testServer = writeToModel(new Server());
            connectionTestLabel.setText("testing connection with " + testServer.loadUrl());
            connectionTestLabel.setIcon(new AnimatedIcon.Default());
            ProgressManager.getInstance().run(new com.github.le_yams.openfga4intellij.servers.ui.ServerDialog.ConnectionTestTask(testServer));
        });

        loadModel();

        return dialogPanel;
    }

    @NotNull
    private JPanel createAuthenticationPanel() {
        var authPanel = new JPanel(new BorderLayout());
        var noAuthPanel = new JPanel();
        var apiTokenPanel = createApiTokenPanel();
        var oidcPanel = createOidcPanel();
        authenticationMethodField.addItemListener(e -> {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            var newInnerPanel = switch ((AuthenticationMethod) e.getItem()) {
                case NONE -> noAuthPanel;
                case API_TOKEN -> apiTokenPanel;
                case OIDC -> oidcPanel;
            };
            SwingUtilities.invokeLater(() -> {
                authPanel.removeAll();
                authPanel.add(newInnerPanel, BorderLayout.CENTER);
                pack();
            });
        });
        return authPanel;
    }

    private JPanel createApiTokenPanel() {
        var apiTokenPanel = new JPanel(new MigLayout("fillx,wrap 2", "[left]rel[grow,fill]"));

        apiTokenPanel.add(new JBLabel("Api token"));
        apiTokenPanel.add(apiTokenField);

        return apiTokenPanel;
    }

    private JPanel createOidcPanel() {
        var oidcPanel = new JPanel(new MigLayout("fillx,wrap 2", "[left]rel[grow,fill]"));

        oidcPanel.add(new JBLabel("Token endpoint"));
        oidcPanel.add(oidcTokenEndpointField);

        oidcPanel.add(new JBLabel("Client id"));
        oidcPanel.add(oidcClientIdField);

        oidcPanel.add(new JBLabel("Client secret"));
        oidcPanel.add(oidcClientSecretField);

        oidcPanel.add(new JBLabel("Scope"));
        oidcPanel.add(oidcScopeField);

        return oidcPanel;
    }

    @Override
    public @Nullable JComponent getPreferredFocusedComponent() {
        return nameField;
    }

    private void loadModel() {
        nameField.setText(server.getName());
        urlField.setText(server.loadUrl());
        authenticationMethodField.setSelectedItem(server.getAuthenticationMethod());
        apiTokenField.setText(server.loadApiToken());
        var oidc = server.loadOidc();
        oidcClientIdField.setText(oidc.clientId());
        oidcClientSecretField.setText(oidc.clientSecret());
        oidcTokenEndpointField.setText(oidc.tokenEndpoint());
        oidcScopeField.setText(oidc.scope());

    }

    private Server updateModel() {
        return writeToModel(server);
    }

    private Server writeToModel(Server server) {
        server.setName(nameField.getText());
        server.storeUrl(urlField.getText());
        var authenticationMethod = authenticationMethodField.getItem();
        server.setAuthenticationMethod(authenticationMethod);
        switch (authenticationMethod) {
            case NONE -> {
            }
            case API_TOKEN -> server.storeApiToken(new String(apiTokenField.getPassword()));
            case OIDC -> server.storeOidc(new Oidc(
                    oidcTokenEndpointField.getText(), oidcClientIdField.getText(),
                    new String(oidcClientSecretField.getPassword()),
                    oidcScopeField.getText()
            ));
        }
        return server;
    }

    public static Server showAddServerDialog(ToolWindow toolWindow) {
        var dialog = new ServerDialog(toolWindow);
        return dialog.showAndGet() ? dialog.updateModel() : null;
    }

    public static Server showEditServerDialog(ToolWindow toolWindow, Server server) {
        var dialog = new ServerDialog(toolWindow, server);
        return dialog.showAndGet() ? dialog.updateModel() : null;
    }


    private class ConnectionTestTask extends Task.Backgroundable {
        private final Server testServer;

        public ConnectionTestTask(Server testServer) {
            super(toolWindow.getProject(), "Connection test", false);
            this.testServer = testServer;
        }

        public void run(@NotNull ProgressIndicator progressIndicator) {
            progressIndicator.setIndeterminate(true);
            progressIndicator.setText(connectionTestLabel.getText());
            try {
                var httpStatus = ServersUtil.testConnection(testServer);
                httpStatus.whenCompleteAsync((httpStatusValue, throwable) -> {
                    if (throwable != null) {
                        while (throwable.getCause() != null) {
                            throwable = throwable.getCause();
                        }
                        taskFailed(throwable);
                    } else if (httpStatusValue < 300) {
                        taskSucceeded();
                    } else {
                        taskFailed("OpenFGA server connection failed with HTTP status " + httpStatusValue);
                    }
                });
            } catch (Exception exception) {
                taskFailed(exception);
            } finally {
                progressIndicator.setFraction(1);
                progressIndicator.setIndeterminate(false);
            }
        }

        private void taskFailed(String errorMessage) {
            taskFailed(errorMessage, null);
        }
        private void taskFailed(Throwable throwable) {
            taskFailed(String.format("%s: %s", throwable.getClass().getName(), throwable.getMessage()), throwable);
        }
        private void taskFailed(String errorMessage, Throwable throwable) {
            SwingUtilities.invokeLater(() -> {
                connectionTestLabel.setText(testServer + " connection failed");
                connectionTestLabel.setIcon(AllIcons.RunConfigurations.TestError);
            });
            if (throwable != null) {
                logger.warn(errorMessage, throwable);
            }
            ToolWindowManager
                    .getInstance(toolWindow.getProject())
                    .notifyByBalloon(toolWindow.getId(), MessageType.ERROR, errorMessage);
        }

        private void taskSucceeded() {
            SwingUtilities.invokeLater(() -> {
                connectionTestLabel.setText(testServer + " connection test succeeded");
                connectionTestLabel.setIcon(AllIcons.RunConfigurations.TestPassed);
            });
        }

    }
}

