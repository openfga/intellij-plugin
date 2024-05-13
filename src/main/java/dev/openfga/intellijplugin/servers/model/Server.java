package dev.openfga.intellijplugin.servers.model;

import java.util.Objects;
import java.util.UUID;

public final class Server {

    private String id;
    private String name;
    private String url;
    private AuthenticationMethod authenticationMethod = AuthenticationMethod.NONE;
    private String apiToken;
    private Oidc oidc = Oidc.EMPTY;

    public Server() {
        this.id = UUID.randomUUID().toString();
    }

    public Server(String id, String name, String url, AuthenticationMethod authenticationMethod, String apiToken, Oidc oidc) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.authenticationMethod = authenticationMethod;
        this.apiToken = apiToken;
        this.oidc = oidc;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AuthenticationMethod getAuthenticationMethod() {
        return authenticationMethod;
    }

    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
        if (authenticationMethod == null) {
            authenticationMethod = AuthenticationMethod.NONE;
        }
        this.authenticationMethod = authenticationMethod;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public Oidc getOidc() {
        return oidc;
    }

    public void setOidc(Oidc oidc) {
        this.oidc = oidc;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Server that = (Server) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
