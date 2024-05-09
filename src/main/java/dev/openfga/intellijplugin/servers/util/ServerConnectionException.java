package dev.openfga.intellijplugin.servers.util;

public class ServerConnectionException extends Exception {

    public ServerConnectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
