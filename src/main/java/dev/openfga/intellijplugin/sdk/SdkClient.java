package dev.openfga.intellijplugin.sdk;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientListStoresResponse;
import dev.openfga.sdk.api.configuration.ClientListStoresOptions;
import dev.openfga.sdk.api.model.Store;
import dev.openfga.sdk.errors.FgaInvalidParameterException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

class SdkClient implements OpenFgaApiClient {

    private static final int STORES_PAGE_SIZE = 50;

    private final OpenFgaClient fgaClient;

    public SdkClient(OpenFgaClient fgaClient) {
        this.fgaClient = fgaClient;
    }

    @Override
    public CompletableFuture<List<Store>> listStores() {
        return listStores(new ClientListStoresOptions().pageSize(STORES_PAGE_SIZE));
    }

    private CompletableFuture<List<Store>> listStores(ClientListStoresOptions options) {
        try {
            return fgaClient.listStores(options).thenCompose(this::listNextStores);
        } catch (FgaInvalidParameterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private CompletableFuture<List<Store>> listNextStores(ClientListStoresResponse response) {
        var continuationToken = response.getContinuationToken();
        if (continuationToken == null || continuationToken.isBlank()) {
            return CompletableFuture.completedFuture(response.getStores());
        }

        try {
            return fgaClient
                    .listStores(new ClientListStoresOptions().pageSize(STORES_PAGE_SIZE).continuationToken(continuationToken))
                    .thenCompose((ClientListStoresResponse nextResponse) ->
                            listNextStores(nextResponse).thenApply(nextStores -> Stream.concat(response.getStores().stream(), nextStores.stream()).toList()));
        } catch (FgaInvalidParameterException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
