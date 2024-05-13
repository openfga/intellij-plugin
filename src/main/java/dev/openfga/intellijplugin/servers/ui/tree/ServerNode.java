package dev.openfga.intellijplugin.servers.ui.tree;

import com.intellij.openapi.diagnostic.Logger;
import dev.openfga.intellijplugin.OpenFGAIcons;
import dev.openfga.intellijplugin.sdk.OpenFgaApiClient;
import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.util.notifications.ToolWindowNotifier;
import dev.openfga.sdk.api.model.Store;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.net.ConnectException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ServerNode extends DefaultMutableTreeNode implements TreeNode {

    private static final Logger logger = Logger.getInstance(ServerNode.class);

    private final Server server;
    private final ToolWindowNotifier notifier;
    private boolean storesLoaded = false;

    public ServerNode(Server server, ToolWindowNotifier notifier) {
        super(server, true);
        this.server = server;
        this.notifier = notifier;
    }

    public Server getServer() {
        return server;
    }

    public ToolWindowNotifier getNotifier() {
        return notifier;
    }

    public boolean isStoresLoaded() {
        return storesLoaded;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String getText() {
        return server.getName();
    }

    @Override
    public String getToolTipText() {
        return server.getUrl();
    }

    @Override
    public Icon getIcon() {
        return OpenFGAIcons.SERVER_NODE;
    }

    @Override
    public NodeType getType() {
        return NodeType.SERVER_NODE;
    }

    public synchronized void forceNextReload() {
        storesLoaded = false;
    }

    public CompletableFuture<Void> loadChildren(OpenFgaTreeModel model) {
        return loadStores().thenAccept(updateChildren(model));
    }

    private CompletableFuture<List<Store>> loadStores() {
        synchronized (this) {
            if (storesLoaded) {
                return CompletableFuture.completedFuture(null);
            }
            storesLoaded = true;
        }
        return OpenFgaApiClient.ForServer(server).listStores()
                .exceptionally(exception -> {
                    var message = exception.getCause() instanceof ConnectException
                            ? "failed to connect to " + server.getUrl()
                            : exception.getMessage();
                    logger.info(exception);
                    notifier.notifyError(message);
                    return null;
                });
    }

    private Consumer<List<Store>> updateChildren(OpenFgaTreeModel model) {
        return stores -> {
            if (stores == null) {
                return;
            }
            var children1 = Collections.list(this.children());
            if (!children1.isEmpty()) {
                var indices = IntStream.range(0, children1.size()).toArray();
                this.removeAllChildren();
                SwingUtilities.invokeLater(() -> model.nodesWereRemoved(this, indices, children1.toArray()));
            }

            for (var store : stores) {
                this.add(new StoreNode(store));
            }
            var indices = IntStream.range(0, stores.size()).toArray();
            SwingUtilities.invokeLater(() -> model.nodesWereInserted(this, indices));
        };
    }
}
