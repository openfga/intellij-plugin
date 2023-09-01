package com.github.le_yams.openfga4intellij.servers.model;

public record Oidc(
        String tokenEndpoint,
        String clientId,
        String clientSecret,
        String scope
) {
}
