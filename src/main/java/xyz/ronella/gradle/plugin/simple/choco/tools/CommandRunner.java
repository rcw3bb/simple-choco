package xyz.ronella.gradle.plugin.simple.choco.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * A utility class for running command.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class CommandRunner {

    private CommandRunner() {}

    /**
     * The method that actually runs the command.
     *
     * @param outputSet A BiConsumer implementation that receives the output and error texts.
     * @param command An array of command and arguments.
     */
    public static void runCommand(BiConsumer<String, String> outputSet, String ... command) {
        try {
            Process process = new ProcessBuilder(command).start();
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String outputStr = output.lines().collect(Collectors.joining("\n"));
            String errorStr = error.lines().collect(Collectors.joining("\n"));
            Optional.ofNullable(outputSet).ifPresent(___outputSet -> ___outputSet.accept(outputStr, errorStr));
        }
        catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        }
    }

}
