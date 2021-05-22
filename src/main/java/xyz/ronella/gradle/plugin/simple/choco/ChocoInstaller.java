package xyz.ronella.gradle.plugin.simple.choco;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String information = "Your gradle must be run on an elevated command terminal to use choco.";

        try {
            System.out.println("Installing Chocolatey");

            ChocoInstaller chocoInstaller = new ChocoInstaller();
            String[] command = chocoInstaller.getCommand().toArray(new String[] {});

            Process process = new ProcessBuilder(command).start();

            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String outputMessage = output.lines().collect(Collectors.joining());
            String errorMessage = error.lines().collect(Collectors.joining());

            if (errorMessage.length() > 0) {
                System.out.println("Error: " + errorMessage);
                System.out.println(information);
                throw new ChocoInstallException();
            }
            else {
                System.out.println("Output: " + outputMessage);
            }
        }
        catch(IOException ioe) {
            System.out.println("Chocolatey installation failed.");
            System.out.println(ioe.getMessage());
            System.out.println(information);
            throw new ChocoInstallException(ioe);
        }
    }
}
