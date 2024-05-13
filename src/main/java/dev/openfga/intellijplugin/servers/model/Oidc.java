package dev.openfga.intellijplugin.servers.model;

public record Oidc(
        String tokenEndpoint,
        String clientId,
        String clientSecret,
        String scope
) {
    public static final Oidc EMPTY = new Oidc("", "", "", "");
}
