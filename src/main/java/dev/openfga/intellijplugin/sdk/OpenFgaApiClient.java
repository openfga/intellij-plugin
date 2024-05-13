package dev.openfga.intellijplugin.sdk;

import dev.openfga.intellijplugin.servers.model.Server;
import dev.openfga.intellijplugin.servers.util.ServersUtil;
import dev.openfga.sdk.api.model.Store;
import dev.openfga.sdk.errors.FgaInvalidParameterException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OpenFgaApiClient {

    CompletableFuture<List<Store>> listStores();

    static OpenFgaApiClient ForServer(Server server) {
        try {
            var openFgaClient = ServersUtil.createClient(server);
            return new SdkClient(openFgaClient);
        } catch (FgaInvalidParameterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
