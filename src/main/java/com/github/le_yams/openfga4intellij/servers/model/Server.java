package com.github.le_yams.openfga4intellij.servers.model;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Server {
    private String id;
    private String name;
    private AuthenticationMethod authenticationMethod = AuthenticationMethod.NONE;

    public Server() {
        this("new server");
    }

    public Server(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String loadUrl() {
        var credentials = getCredentials(CredentialKey.URL);
        if (credentials == null) {
            return "";
        }
        return credentials.getPasswordAsString();
    }

    public void storeUrl(String url) {
        var attributes = getCredentialAttributes(CredentialKey.URL);
        PasswordSafe.getInstance().set(attributes, new Credentials(id, url));
    }

    public String loadApiToken() {
        var credentials = getCredentials(CredentialKey.API_TOKEN);
        if (credentials == null) {
            return "";
        }
        return credentials.getPasswordAsString();
    }

    public void storeApiToken(String token) {
        var attributes = getCredentialAttributes(CredentialKey.API_TOKEN);
        PasswordSafe.getInstance().set(attributes, new Credentials(id, token));
    }

    public Credentials getCredentials(String keySuffix) {
        CredentialAttributes attributes = getCredentialAttributes(keySuffix);
        return PasswordSafe.getInstance().get(attributes);
    }

    @NotNull
    private CredentialAttributes getCredentialAttributes(String keySuffix) {
        var key = id + "_" + keySuffix;
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("OpenFGAServer", key));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthenticationMethod getAuthenticationMethod() {
        return authenticationMethod;
    }

    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public Oidc loadOidc() {
        var credentials = getCredentials(CredentialKey.OIDC_CLIENT);
        var clientId = credentials != null ? credentials.getUserName() : "";
        var clientSecret = credentials != null ? credentials.getPasswordAsString() : "";

        credentials = getCredentials(CredentialKey.OIDC_TOKEN_ENDPOINT);
        var tokenEndpoint = credentials != null ? credentials.getPasswordAsString() : "";

        credentials = getCredentials(CredentialKey.OIDC_SCOPE);
        var audience = credentials != null ? credentials.getPasswordAsString() : "";

        return new Oidc(tokenEndpoint, clientId, clientSecret, audience);
    }

    public void storeOidc(Oidc oidc) {
        var attributes = getCredentialAttributes(CredentialKey.OIDC_CLIENT);
        PasswordSafe.getInstance().set(attributes, new Credentials(oidc.clientId(), oidc.clientSecret()));
        attributes = getCredentialAttributes(CredentialKey.OIDC_TOKEN_ENDPOINT);
        PasswordSafe.getInstance().set(attributes, new Credentials(id, oidc.tokenEndpoint()));
        attributes = getCredentialAttributes(CredentialKey.OIDC_SCOPE);
        PasswordSafe.getInstance().set(attributes, new Credentials(id, oidc.scope()));
    }

    @Override
    public String toString() {
        return name;
    }

    private interface CredentialKey {

        String URL = "url";
        String API_TOKEN = "apiToken";
        String OIDC_CLIENT = "oidc_client";
        String OIDC_TOKEN_ENDPOINT = "oidc_token_endpoint";
        String OIDC_SCOPE = "oidc_scope";
    }
}
