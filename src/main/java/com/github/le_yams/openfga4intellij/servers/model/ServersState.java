package com.github.le_yams.openfga4intellij.servers.model;

import java.util.ArrayList;
import java.util.List;

public class ServersState {

    private List<Server> servers = new ArrayList<>();

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
