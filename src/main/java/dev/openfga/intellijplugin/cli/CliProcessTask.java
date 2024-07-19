package dev.openfga.intellijplugin.cli;

import java.io.File;
import java.io.IOException;

public interface CliProcessTask<T> {
    T onSuccess(File stdOutFile, File stdErrFile) throws IOException, CliTaskException;

    CliTaskException onFailure(File stdOutFile, File stdErrFile);
}
