package dev.openfga.intellijplugin.servers.model;

public enum AuthenticationMethod {
    NONE,
    API_TOKEN,
    OIDC;
    ;

    @Override
    public String toString() {
        return switch (this) {
            case NONE -> "none";
            case API_TOKEN -> "api token";
            case OIDC -> "oidc";
        };
    }
}
