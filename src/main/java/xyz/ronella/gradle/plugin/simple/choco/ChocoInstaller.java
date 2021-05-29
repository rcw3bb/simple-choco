package xyz.ronella.gradle.plugin.simple.choco;

import xyz.ronella.gradle.plugin.simple.choco.tools.CommandRunner;

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
public class ChocoInstaller {

    private ChocoInstaller() {}

    /**
     * The choco executor file nanme.
     */
    public static final String EXECUTABLE = "choco.exe";

    /**
     * The directory that holds the EXECUTABLE.
     */
    public static final String BIN_DIR = "bin";

    /**
     * The default installation location of chocolatey application.
     */
    public static final Path DEFAULT_INSTALL_LOCATION = Paths.get(System.getenv("ProgramData"), "chocolatey");

    /**
     * The command the and parameters that will install the chocolatey application.
     *
     * @return A list that contains the installation command.
     */
    public List<String> getCommand() {
        final String POWER_SHELL = "PowerShell.Exe";

        String installCommand = "\"Start-Process powershell -Wait -Verb runas -argumentlist \"\"\"\"-NoProfile\"\"\"\",\"\"\"\"-InputFormat\"\"\"\",\"\"\"\"None\"\"\"\",\"\"\"\"-ExecutionPolicy\"\"\"\",\"\"\"\"Bypass\"\"\"\",\"\"\"\"-Command\"\"\"\",\"\"\"\"[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))\"\"\"\"\"";
        List<String> command = new ArrayList<>();
        command.add(POWER_SHELL);
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
     * @throws ChocoInstallException An instance thrown if the installation failed.
     */
    public static void install() throws ChocoInstallException {
        System.out.println("Installing Chocolatey");

        ChocoInstaller chocoInstaller = new ChocoInstaller();
        String[] command = chocoInstaller.getCommand().toArray(new String[] {});

        CommandRunner.runCommand((___output, ___error) -> {
            if (___error.length() > 0) {
                System.out.println("Error: " + ___error);
                System.out.println("Chocolatey automatic installation failed. Install chocolatey manually.");
            }
            else {
                System.out.println("Output: " + ___output);
            }
        }, command);
    }
}
