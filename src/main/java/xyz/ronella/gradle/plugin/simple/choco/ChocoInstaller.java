package xyz.ronella.gradle.plugin.simple.choco;

import xyz.ronella.gradle.plugin.simple.choco.tools.CommandRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChocoInstaller {

    private ChocoInstaller() {}

    public static final String EXECUTABLE = "choco.exe";

    public static final String BIN_DIR = "bin";

    public static final Path DEFAULT_INSTALL_LOCATION = Paths.get(System.getenv("ProgramData"), "chocolatey");

    public List<String> getCommand() {
        final String POWER_SHELL = "PowerShell.Exe";
        List<String> command = new ArrayList<>();
        command.add(POWER_SHELL);
        command.add("-NoProfile");
        command.add("-InputFormat");
        command.add("None");
        command.add("-ExecutionPolicy");
        command.add("Bypass");
        command.add("-Command");
        command.add("\"[System.Net.ServicePointManager]::SecurityProtocol = 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))\"");

        return command;
    }

    public static void install() throws ChocoInstallException {
        System.out.println("Installing Chocolatey");

        ChocoInstaller chocoInstaller = new ChocoInstaller();
        String[] command = chocoInstaller.getCommand().toArray(new String[] {});

        CommandRunner.runCommand((___output, ___error) -> {
            if (___error.length() > 0) {
                System.out.println("Error: " + ___error);
                System.out.println("Your gradle must be run on an elevated command terminal to install chocolatey.");
            }
            else {
                System.out.println("Output: " + ___output);
            }
        }, command);
    }
}
