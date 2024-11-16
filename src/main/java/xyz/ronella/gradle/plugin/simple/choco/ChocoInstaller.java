package xyz.ronella.gradle.plugin.simple.choco;

import xyz.ronella.trivial.handy.CommandProcessor;
import xyz.ronella.trivial.handy.impl.CommandArray;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class that can install the chocolatey console application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public final class ChocoInstaller {

    /**
     * The choco executor filename.
     */
    public static final String EXECUTABLE = "choco.exe";

    /**
     * The directory that holds the EXECUTABLE.
     */
    public static final String BIN_DIR = "bin";

    /**
     * The default installation location of chocolatey application.
     */
    @SuppressWarnings("PMD.LongVariable")
    public static final Path DEFAULT_INSTALL_LOCATION = Paths.get(System.getenv("ProgramData"), "chocolatey");

    private ChocoInstaller() {}

    /**
     * The command and parameters that will install the chocolatey application.
     *
     * @return A list that contains the installation command.
     */
    private List<String> getInstallCommand(final String downloadURL) {
        final String powershellCommand = "PowerShell.Exe";

        final String installCommand = String.format("\"Start-Process powershell -Wait -Verb runas -argumentlist " +
                "\"\"\"\"-NoProfile\"\"\"\"," +
                "\"\"\"\"-InputFormat\"\"\"\"," +
                "\"\"\"\"None\"\"\"\"," +
                "\"\"\"\"-ExecutionPolicy\"\"\"\"," +
                "\"\"\"\"Bypass\"\"\"\"," +
                "\"\"\"\"-Command\"\"\"\"," +
                "\"\"\"\"[System.Net.ServicePointManager]::SecurityProtocol = 3072; " +
                "iex ((New-Object System.Net.WebClient).DownloadString('%s'))\"\"\"\"\"", downloadURL);

        final List<String> command = new ArrayList<>();
        command.add(powershellCommand);
        command.add("-NoProfile");
        command.add("-InputFormat");
        command.add("None");
        command.add("-ExecutionPolicy");
        command.add("Bypass");
        command.add("-Command");
        command.add(installCommand);

        return command;
    }

    /**
     * The method that will actually install the chocolatey application.
     *
     * @param downloadURL The URL of the chocolatey download script.
     *
     * @throws ChocoInstallException An instance thrown if the installation failed.
     */
    @SuppressWarnings({"PMD.SystemPrintln", "PMD.AvoidUncheckedExceptionsInSignatures"})
    public static void install(final String downloadURL) throws ChocoInstallException {
        System.out.println("Installing Chocolatey");

        final ChocoInstaller chocoInstaller = new ChocoInstaller();
        final var command = chocoInstaller.getInstallCommand(downloadURL);

        CommandProcessor.process(CommandProcessor.ProcessOutputHandler.captureOutputs((___output, ___error) -> {
            if (!___error.isEmpty()) {
                System.err.println("Error: " + ___error);
            }
        }), CommandArray.wrap(command));

    }
}
