package com.github.le_yams.openfga4intellij.cli;

public class CliTaskException extends Exception {

    public CliTaskException(String message) {
        super(message);
    }

    public CliTaskException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public CliTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
