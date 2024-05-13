package dev.openfga.intellijplugin.servers.service;

import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.util.ListUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

@Service(Service.Level.APP)
@State(
        name = "OpenFGAServers.State",
        storages = {
                @Storage(value = "openfga-servers.xml")
        }
)
public final class OpenFGAServers implements PersistentStateComponent<ServersState> {

    private final List<Server> servers = new ArrayList<>();
    private ServersState state = new ServersState();

    public static OpenFGAServers getInstance() {
        return ApplicationManager.getApplication().getService(OpenFGAServers.class);
    }

    public List<Server> getServers() {
        return servers;
    }

    @Nullable
    @Override
    public ServersState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull ServersState state) {
        this.state = state;
        var loadedServers = this.state.getServers().stream()
                .map(ServerState::toModel)
                .toList();
        servers.clear();
        servers.addAll(loadedServers);
    }

    public void add(Server server) {
        var serverState = new ServerState();
        serverState.setId(server.getId());
        serverState.populateWith(server);
        state.getServers().add(serverState);
        servers.add(server);
    }

    public void update(Server server) {
        var serverState = ListUtil.get(state.getServers(), withId(server.getId()))
                .orElseThrow(() -> new NoSuchElementException("could not found server state with id " + server.getId()));
        serverState.populateWith(server);
    }

    public void remove(Server server) {
        ListUtil.remove(state.getServers(), withId(server.getId()));
        servers.remove(server);
    }

    public int moveDown(Server server) {
        return move(server, index -> index + 1);
    }

    public int moveUp(Server server) {
        return move(server, index -> index - 1);
    }

    private int move(Server server, IntUnaryOperator f) {
        var index = ListUtil.indexOf(state.getServers(), withId(server.getId())).orElse(-1);
        var targetIndex = f.applyAsInt(index);
        if (targetIndex < 0 || targetIndex >= state.getServers().size()) {
            return index;
        }
        Collections.swap(state.getServers(), index, targetIndex);
        loadState(state);
        return targetIndex;
    }

    private static Predicate<ServerState> withId(String id) {
        return serverState -> Objects.equals(serverState.getId(), id);
    }
}
