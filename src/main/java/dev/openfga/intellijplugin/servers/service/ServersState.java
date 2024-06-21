package dev.openfga.intellijplugin.servers.service;


import java.util.ArrayList;
import java.util.List;

public class ServersState {

    private List<ServerState> servers = new ArrayList<>();

    public List<ServerState> getServers() {
        return servers;
    }

    public void setServers(List<ServerState> servers) {
        this.servers = servers;
    }
}
