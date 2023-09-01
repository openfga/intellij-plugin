package com.github.le_yams.openfga4intellij.servers.util;

public class ServerConnectionException extends Exception {

    public ServerConnectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
