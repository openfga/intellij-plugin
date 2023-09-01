package com.github.le_yams.openfga4intellij.cli;

import com.intellij.openapi.progress.ProgressIndicator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CliProcess {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private final Cli cli;

    private final List<String> args;
    private final Duration timeout;

    private Runnable onCancel;

    public CliProcess(@NotNull Cli cli, @NotNull List<String> args) {
        this(cli, args, DEFAULT_TIMEOUT);
    }

    public CliProcess(@NotNull Cli cli, @NotNull List<String> args, @NotNull Duration timeout) {
        this.cli = cli;
        this.args = args;
        this.timeout = timeout;
    }

    public <T> Optional<T> start(@NotNull ProgressIndicator progressIndicator, CliProcessTask<T> task) throws CliTaskException {
        var command = new ArrayList<String>();
        command.add(cli.binaryPath().toString());
        command.addAll(args);

        var processBuilder = new ProcessBuilder(command);

        File outputFile = null;
        File errorFile = null;
        try {

            var prefix = getClass().getName();
            outputFile = File.createTempFile(prefix, ".txt");
            errorFile = File.createTempFile(prefix, ".log");


            processBuilder.redirectError(errorFile);
            processBuilder.redirectOutput(outputFile);
            Process process = processBuilder.start();
            onCancel = process::destroy;

            var processTimedOut = !process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
            if (processTimedOut) {
                throw new InterruptedException("command timed out, took more than " + timeout.toMillis() + " ms");
            }
            if (process.exitValue() == 0) {
                if (!progressIndicator.isCanceled()) {
                    return Optional.ofNullable(task.onSuccess(outputFile, errorFile));
                }
                return Optional.empty();
            } else {
                throw task.onFailure(outputFile, errorFile);
            }
        } catch (IOException | InterruptedException e) {
            throw new CliTaskException(e);
        } finally {
            tryDelete(outputFile);
            tryDelete(errorFile);
        }
    }

    public void cancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }

    private void tryDelete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        try {
            file.delete();
        } catch (Exception e) {
            // left intentionally blank
        }
    }

}
