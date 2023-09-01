package com.github.le_yams.openfga4intellij.servers.service;

import com.github.le_yams.openfga4intellij.servers.model.Server;
import com.github.le_yams.openfga4intellij.servers.model.ServersState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.IntUnaryOperator;

@Service(Service.Level.APP)
@State(
        name = "OpenFGAServers.State",
        storages = {
                @Storage(value = "openfga-servers.xml", roamingType = RoamingType.DISABLED)
        }
)
public final class OpenFGAServers implements PersistentStateComponent<ServersState> {

    private ServersState state = new ServersState();

    public static OpenFGAServers getInstance() {
        return ApplicationManager.getApplication().getService(OpenFGAServers.class);
    }

    public List<Server> getServers() {
        return state.getServers();
    }

    public void add(Server server) {
        state.getServers().add(server);
    }

    public void remove(Server server) {
        state.getServers().remove(server);
    }

    public int moveDown(Server server) {
        return move(server, index -> index + 1);
    }

    public int moveUp(Server server) {
        return move(server, index -> index - 1);
    }

    private int move(Server server, IntUnaryOperator f) {
        var index = state.getServers().indexOf(server);
        var targetIndex = f.applyAsInt(index);
        if (targetIndex < 0 || targetIndex >= state.getServers().size()) {
            return index;
        }
        Collections.swap(state.getServers(), index, targetIndex);
        return targetIndex;
    }

    @Override
    public @Nullable ServersState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull ServersState state) {
        this.state = state;
    }
}
