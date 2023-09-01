package com.github.le_yams.openfga4intellij.servers.util;

import com.github.le_yams.openfga4intellij.servers.model.Server;
import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientListStoresResponse;
import dev.openfga.sdk.api.configuration.*;
import dev.openfga.sdk.errors.FgaInvalidParameterException;

import java.util.concurrent.CompletableFuture;


public class ServersUtil {

    public static CompletableFuture<Integer> testConnection(Server server) throws ServerConnectionException {
        try {
            var fgaClient = createClient(server);
            var options = new ClientListStoresOptions()
                    .pageSize(1);
            var stores = fgaClient.listStores(options);
            return stores.thenApply(ClientListStoresResponse::getStatusCode);
        } catch (Exception e) {
            throw new ServerConnectionException(e);
        }
    }

    private static OpenFgaClient createClient(Server server) throws FgaInvalidParameterException {
        var clientConfiguration = new ClientConfiguration()
                .apiUrl(server.loadUrl())
                .credentials(getCredentials(server));

        return new OpenFgaClient(clientConfiguration);
    }

    private static Credentials getCredentials(Server server) {
        return switch (server.getAuthenticationMethod()) {
            case NONE -> new Credentials();
            case API_TOKEN -> new Credentials(new ApiToken(server.loadApiToken()));
            case OIDC -> {
                var oidc = server.loadOidc();
                yield new Credentials(new ClientCredentials()
                        .apiTokenIssuer(oidc.tokenEndpoint())
                        .clientId(oidc.clientId())
                        .clientSecret(oidc.clientSecret())
                        .scopes(oidc.scope()));
            }
        };
    }
}
