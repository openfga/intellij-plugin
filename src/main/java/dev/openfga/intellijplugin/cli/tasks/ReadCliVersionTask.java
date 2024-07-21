package dev.openfga.intellijplugin.cli.tasks;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import dev.openfga.intellijplugin.cli.Cli;
import dev.openfga.intellijplugin.cli.CliProcess;
import dev.openfga.intellijplugin.cli.CliProcessTask;
import dev.openfga.intellijplugin.cli.CliTaskException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.swing.*;
import org.jetbrains.annotations.NotNull;

public class ReadCliVersionTask extends Task.WithResult<Optional<String>, CliTaskException>
        implements CliProcessTask<String> {

    private static final Pattern versionRegex = Pattern.compile("fga version v`(?<version>.+?)`");
    private final CliProcess process;

    public ReadCliVersionTask(JComponent component, Cli cli) {
        super(null, component, "Detecting OpenFGA Cli Version", true);
        process = new CliProcess(cli, List.of("--version"));
    }

    @Override
    public void onCancel() {
        process.cancel();
    }

    @Override
    protected Optional<String> compute(@NotNull ProgressIndicator indicator) throws CliTaskException {
        return process.start(indicator, this);
    }

    @Override
    public String onSuccess(File stdOutFile, File stdErrFile) throws IOException {
        var output = Files.readString(stdOutFile.toPath());
        var matcher = versionRegex.matcher(output);
        if (matcher.find()) {
            return matcher.group("version");
        }
        return null;
    }

    @Override
    public CliTaskException onFailure(File stdOutFile, File stdErrFile) {
        String errorOutput;
        try {
            errorOutput = Files.readString(stdErrFile.toPath());
        } catch (IOException e) {
            errorOutput = "<no error output>";
        }
        return new CliTaskException("unexpected cli error:" + errorOutput);
    }
}
