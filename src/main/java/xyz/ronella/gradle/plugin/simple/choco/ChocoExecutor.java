package xyz.ronella.gradle.plugin.simple.choco;

import xyz.ronella.trivial.handy.CommandProcessor;
import xyz.ronella.trivial.handy.OSType;
import xyz.ronella.trivial.handy.impl.CommandArray;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * The class that assembles the appropriate choco command and execute if possible.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public final class ChocoExecutor {

    private final OSType osType;
    private boolean isAutoInstall;
    private final File chocoHome;
    private final boolean isNoop;
    private final boolean isAdminMode;
    private final List<Supplier<File>> executables;
    private final String command;
    private final List<String> args;
    private final List<String> zArgs;
    private final boolean hasLogging;
    private final String taskName;
    private final boolean isScriptMode;
    private final List<List<String>> packages;
    private final boolean noScriptDeletion;
    private final boolean showCommand;
    private final String chocoDownloadURL;

    /**
     * Creates an instance of ChocoExecutor
     *
     * @param builder An instance of ChocoExecutorBuilder
     */
    private ChocoExecutor(final ChocoExecutorBuilder builder) {
        executables = new ArrayList<>();
        osType = builder.osType==null ? OSType.identify() : builder.osType;
        isAutoInstall = builder.isAutoInstall;
        chocoHome = builder.chocoHome;
        isNoop = builder.isNoop;
        command = builder.command;
        args = builder.args;
        zArgs = builder.zArgs;
        hasLogging = builder.hasLogging;
        isAdminMode = (!builder.isRunningOnAdmin || builder.forceAdminMode) && builder.isAdminMode;
        taskName = builder.taskName;
        isScriptMode = builder.isScriptMode;
        packages = builder.packages;
        noScriptDeletion = builder.noScriptDeletion;
        showCommand = builder.showCommand;
        chocoDownloadURL = builder.chocoDownloadURL;

        prepareExecutables();
    }

    private void prepareExecutables() {
        final Consumer<Supplier<File>> addExecLogic = ___execLogic -> {
            executables.add(___execLogic);
            isAutoInstall = false;
        };

        Optional.ofNullable(chocoHome).ifPresent(___chocoHome ->
                addExecLogic.accept(() -> Paths.get(chocoHome.getAbsolutePath(),ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile()));

        Optional.ofNullable(System.getenv("CHOCOLATEY_HOME")).ifPresent(___chocoHome ->
                addExecLogic.accept(() -> Paths.get(___chocoHome,ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile()));

        if (executables.isEmpty()) {
            executables.add(() -> Paths.get(ChocoInstaller.DEFAULT_INSTALL_LOCATION.toString(), ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile());
            getExecutableAuto().ifPresent(___chocoHome -> executables.add(() -> ___chocoHome));
        }
    }

    private Optional<File> getExecutableAuto() {
        final StringBuilder sbFqfn = new StringBuilder();
        CommandProcessor.process(CommandProcessor.ProcessOutputHandler.captureOutputs((___output, ___error) ->
                        sbFqfn.append(___output)),
                CommandArray.wrap(List.of("where", ChocoInstaller.EXECUTABLE)));

        String fqfn = sbFqfn.toString();
        File output = null;

        if (!fqfn.isEmpty()) {
            fqfn = fqfn.split("\\r\\n")[0]; //Just the first valid entry of where.
            final File fileExec = new File(fqfn);
            if (fileExec.exists()) {
                output = fileExec;
            }
        }
        return Optional.ofNullable(output);
    }

    private Optional<File> executable() {
        if (OSType.Windows.equals(osType)) {
            final Optional<Supplier<File>> executable = executables.stream().filter(___execLogic -> ___execLogic.get().exists()).findFirst();
            if (executable.isPresent()) {
                final Supplier<File> exec = executable.get();
                return Optional.of(exec.get());
            }
            else {
                try {
                    if (isAutoInstall) {
                        if (!isNoop) {
                            final File installLocation = ChocoInstaller.DEFAULT_INSTALL_LOCATION.toFile();
                            if (!installLocation.exists()) {

                                System.out.printf("Downloading from %s%n", chocoDownloadURL);

                                ChocoInstaller.install(chocoDownloadURL);
                                if (installLocation.exists()) {
                                    return executable();
                                }
                                else {
                                    throw new ChocoInstallException();
                                }
                            }
                        }
                    }
                    else {
                        final String missingChocoExecutable = String.format("Cannot find %s and automatic installation is " +
                                "disabled, ensure that chocoHome property was set or CHOCOLATEY_HOME environment " +
                                "variable exists.", ChocoInstaller.EXECUTABLE);
                        System.err.println(missingChocoExecutable);
                    }
                } catch (ChocoInstallException cie) {
                    System.err.println("Chocolatey automatic installation failed. Install chocolatey manually.");
                }
            }
        }
        else {
            System.err.printf("%s OS is required.%n", OSType.Windows);
        }

        throw new ChocoExecutableException();
    }

    private List<String> getPowershellCommand() {
        final List<String> shellCommand = new ArrayList<>();
        shellCommand.add("powershell.exe");
        shellCommand.add("-NoProfile");
        shellCommand.add("-InputFormat");
        shellCommand.add("None");
        shellCommand.add("-ExecutionPolicy") ;
        shellCommand.add("Bypass");
        shellCommand.add("-Command");

        return new ArrayList<>(shellCommand);
    }

    private String quadQuote(final String text) {
        return String.format("\"\"\"\"%s\"\"\"\"", text);
    }

    private String doubleQuote(final String text) {
        return String.format("\"\"%s\"\"", text);
    }

    private String singleQuote(final String text) {
        return String.format("\"%s\"", text);
    }

    private List<String> adminModeCommand(final String executable, final List<String> allArgs) {
        final List<String> fullCommand = getPowershellCommand();

        final StringBuilder sbArgs = new StringBuilder();
        allArgs.forEach(___arg -> sbArgs.append(sbArgs.length()>0 ? ",": "").append(quadQuote(___arg)));

        final StringBuilder sbActualCommand = new StringBuilder("\"Start-Process ")
                .append(quadQuote(executable))
                .append(" -Wait -Verb runas")
                .append(sbArgs.isEmpty() ? "": " -argumentlist ")
                .append(sbArgs)
                .append("\"");

        fullCommand.add(sbActualCommand.toString());
        return fullCommand;
    }

    private File getDataDirectory() {
        final File dataDirectory = Paths.get(System.getenv("LOCALAPPDATA"), "simple-choco").toFile();
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        return dataDirectory;
    }

    private String getLogFile() {
        final File dataDir = getDataDirectory();
        final File chocoLogFile = Paths.get(dataDir.getAbsolutePath(), "chocolatey.log").toFile();
        return chocoLogFile.getAbsolutePath();
    }

    private List<String> generateLoggingArg(final String logFile) {
        final List<String> allArgs = new ArrayList<>();
        allArgs.add("--log-file");
        allArgs.add(logFile);
        return allArgs;
    }

    private List<String> prepareCommand(final File chocoExecutable) {
        return prepareCommand(chocoExecutable, new ArrayList<>());
    }

    private List<String> prepareCommand(final File chocoExecutable, final List<String> packageInfo) {
        final String executable = chocoExecutable.getAbsolutePath();
        final List<String> allArgs = new ArrayList<>();
        final List<String> fullCommand = new ArrayList<>();

        Optional.ofNullable(command).ifPresent(allArgs::add);
        allArgs.addAll(packageInfo);
        allArgs.addAll(args);
        allArgs.addAll(zArgs);

        final List<String> commandToRun = new ArrayList<>();
        commandToRun.add(executable);
        commandToRun.addAll(allArgs);

        final String logFile = getLogFile();

        if (showCommand) {
            System.out.println(String.join(" ", commandToRun));
        }

        if (!isNoop && hasLogging) {
            allArgs.addAll(generateLoggingArg(logFile));
            System.out.println(String.format("Log file: %s", logFile));
        }

        if (isAdminMode && !isScriptMode) {
            fullCommand.addAll(adminModeCommand(executable, allArgs));
        } else {
            fullCommand.add(executable);
            fullCommand.addAll(allArgs);
        }
        return fullCommand;
    }

    private void executeCommand(final List<String> command) {
        CommandProcessor.process(CommandProcessor.ProcessOutputHandler.captureStreams(
                (___output, ___error) -> {
                    final BufferedReader output = new BufferedReader(new InputStreamReader(___output));
                    final BufferedReader error = new BufferedReader(new InputStreamReader(___error));
                    final String outputStr = output.lines().collect(Collectors.joining("\n"));
                    final String errorStr = error.lines().collect(Collectors.joining("\n"));

                    if (!errorStr.isEmpty()) {
                        System.err.println(errorStr);
                    }
                    else {
                        System.out.println(outputStr);
                    }
                }), CommandArray.wrap(command));
    }

    private String executeSingleCommand() {
        final StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            final List<String> fullCommand = prepareCommand(___executable);
            sbCommand.append(String.join(" ", fullCommand).trim());
            if (!isNoop) {
                executeCommand(fullCommand);
            }
        });
        return sbCommand.toString();
    }

    private List<String> buildScriptCommand(final String scriptFullPath) {
        final List<String> fullCommand = getPowershellCommand();
        final String executable = "powershell.exe";

        if (isAdminMode) {
            final StringBuilder sbActualCommand = new StringBuilder("\"Start-Process ")
                    .append(executable)
                    .append(" -Wait -Verb runas")
                    .append(" -argumentlist \"\"\"\"-NoProfile\"\"\"\",\"\"\"\"-InputFormat\"\"\"\",\"\"\"\"None\"\"\"\",\"\"\"\"-ExecutionPolicy\"\"\"\",\"\"\"\"Bypass\"\"\"\",\"\"\"\"-Command\"\"\"\",")
                    .append(quadQuote(String.format("%s %s", executable, doubleQuote(quadQuote(scriptFullPath)))))
                    .append("\"");

            fullCommand.add(sbActualCommand.toString());
        }
        else {
            fullCommand.add(singleQuote(scriptFullPath));
        }

        return fullCommand;
    }

    private void saveAndExecuteScriptFile(final String script, final Consumer<String> executeLogic)
            throws ChocoScriptException {

        final File dataDir = getDataDirectory();
        final File scriptFile = Paths.get(dataDir.getAbsolutePath(), String.format("%s.ps1", taskName)).toFile();

        try {
            final String scriptFullPath = scriptFile.getAbsolutePath();

            if (!scriptFile.exists()) {
                try {
                    scriptFile.createNewFile();
                } catch (IOException ioe) {
                    System.err.println(String.format("Cannot create %s", scriptFullPath));
                    throw new ChocoScriptException(ioe.getMessage());
                }
            }

            try (FileWriter writer = new FileWriter(scriptFile)) {
                writer.write(script);
                writer.flush();
            } catch (IOException ioe) {
                System.err.println(String.format("Cannot write to %s", scriptFullPath));
                throw new ChocoScriptException(ioe.getMessage());
            }

            executeLogic.accept(scriptFullPath);
        }
        finally {
            if (scriptFile.exists()) {
                if (noScriptDeletion) {
                    System.out.println(String.format("Script file: %s", scriptFile.getAbsolutePath()));
                }
                else {
                    scriptFile.delete();
                }
            }
        }
    }

    private String executeScriptCommands() {
        final StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            packages.forEach(___package -> {
                final List<String> fullCommand = prepareCommand( ___executable, ___package);
                sbCommand.append(String.join(" ", fullCommand).trim())
                        .append("\n");
            });
            try {
                saveAndExecuteScriptFile(sbCommand.toString(), ___scriptFullPath -> {
                    final List<String> fullCommand = buildScriptCommand(___scriptFullPath);

                    if (isNoop) {
                        sbCommand.append(String.join(" ", fullCommand));
                        System.out.println(String.format("Scripted on %s", ___scriptFullPath));
                    }
                    else {
                        executeCommand(fullCommand);
                    }

                });
            } catch (ChocoScriptException chocoScriptException) {
                chocoScriptException.printStackTrace(System.err);
            }
        });
        return sbCommand.toString();
    }

    /**
     * Actually execute the assembled choco command.
     *
     * @return The command that wil be exeecuted.
     */
    public String execute() {
        if (isScriptMode) {
            return executeScriptCommands();
        }
        else {
            return executeSingleCommand();
        }
    }

    /**
     * Executes a single choco command with custom logic for handling the command's output and error streams.
     *
     * @param logic A BiConsumer that processes the InputStream for the command's output and error streams.
     * @return The command that was executed as a String.
     * @throws RuntimeException if the command cannot be executed due to a MissingCommandException.
     */
    public String executeSingleCommand(final BiConsumer<InputStream, InputStream> logic) {
        final StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            final List<String> fullCommand = prepareCommand(___executable);
            sbCommand.append(String.join(" ", fullCommand).trim());

            if (!isNoop) {
                final var commandArray = CommandArray.wrap(fullCommand);
                CommandProcessor.process(CommandProcessor.ProcessOutputHandler.captureStreams(logic), commandArray);
            }
        });
        return sbCommand.toString();
    }

    /**
     * Creates an instance of ChocoExecutorBuilder.
     *
     * @return An instance of ChocoExecutorBuilder.
     */
    public static ChocoExecutorBuilder getBuilder() {
        return new ChocoExecutorBuilder();
    }

    /**
     * The only class that can create a ChocoExecutor instance.
     *
     * @author Ron Webb
     * @since v1.0.0
     */
    final public static class ChocoExecutorBuilder {
        private boolean showCommand;
        private OSType osType;
        private boolean isAutoInstall;
        private File chocoHome;
        private boolean isNoop;
        private String command;
        private boolean isAdminMode;
        private final List<String> args;
        private final List<String> zArgs;
        private boolean hasLogging;
        private boolean isRunningOnAdmin;
        private boolean forceAdminMode;
        private String taskName;
        private boolean isScriptMode;
        private List<List<String>> packages;
        private boolean noScriptDeletion;
        private String chocoDownloadURL;

        private ChocoExecutorBuilder() {
            args = new ArrayList<>();
            zArgs = new ArrayList<>();
            showCommand = true;
        }

        /**
         * Add an OSType in the builder.
         * @param osType An instance of the OSType enum.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addOSType(final OSType osType) {
            this.osType = osType;
            return this;
        }

        /**
         * Adds an indication if the ChocoExecutor will auto install chocolatey.
         *
         * @param isAutoInstall Set to true to indicate auto installation.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addAutoInstall(final boolean isAutoInstall) {
            this.isAutoInstall = isAutoInstall;
            return this;
        }

        /**
         * Adds the location of a non-standard location of chocolatey installation.
         *
         * @param chocoHome An instance of File holding the location of chocolatey.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addChocoHome(final File chocoHome) {
            this.chocoHome = chocoHome;
            return this;
        }

        /**
         * Add the choco command the be executed.
         *
         * @param command The command to be executed.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addCommand(final String command) {
            this.command = command;
            return this;
        }

        /**
         * Adds multiple arguments in one command.
         *
         * @param args An array of arguments.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addArgs(final String ... args) {
            if (args.length>0) {
                this.args.addAll(Arrays.asList(args));
            }
            return this;
        }

        /**
         * Adds multiple zArguments in one command.
         *
         * @param args An array of arguments.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addZArgs(final String ... args) {
            if (args.length>0) {
                this.zArgs.addAll(Arrays.asList(args));
            }
            return this;
        }

        /**
         * Adds an indication that no actual choco command will be executed
         * @param noop Set to true to stop prevent execution.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addNoop(final boolean noop) {
            this.isNoop = noop;
            return this;
        }

        /**
         * Adds an indication assembled choco command will be executed in admin mode.
         * @param isAdminMode Set to true to run the command in admin mode.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addAdminMode(final boolean isAdminMode) {
            this.isAdminMode = isAdminMode;
            return this;
        }

        /**
         * Adds an indication that the choco must log its activities to a file.
         * @param hasLogging Set to true to enable choco logging.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addLogging(final boolean hasLogging) {
            this.hasLogging = hasLogging;
            return this;
        }

        /**
         * Adds an indication that the choco is already running on administration mode.
         * @param runningOnAdmin True if running on administration mode.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addRunningOnAdmin(final boolean runningOnAdmin) {
            this.isRunningOnAdmin = runningOnAdmin;
            return this;
        }

        /**
         * Adds an indication that the choco is being force to run in admin mode.
         * @param forceAdminMode True if running on to force administration mode.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addForceAdminMode(final boolean forceAdminMode) {
            this.forceAdminMode = forceAdminMode;
            return this;
        }

        /**
         * Adds an indication that the choco commands will be scripted.
         * @param scriptMode True the commands will be scripted.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addScriptMode(final boolean scriptMode) {
            this.isScriptMode = scriptMode;
            return this;
        }

        /**
         * Adds the name of the task being executed.
         * @param taskName The name of the task.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addTaskName(final String taskName) {
            this.taskName = taskName;
            return this;
        }

        /**
         * The the packages to be scripted.
         * @param packages The name of the task.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addPackages(final List<List<String>> packages) {
            this.packages = packages;
            return this;
        }

        /**
         * Indicates that the script generated will not be deleted.
         * @param noScriptDeletion The name of the task.
         *
         * @return An instance of the builder.
         *
         * @since 1.1.0
         */
        public ChocoExecutorBuilder addNoScriptDeletion(final boolean noScriptDeletion) {
            this.noScriptDeletion = noScriptDeletion;
            return this;
        }

        /**
         * Suppress displaying the command to execute.
         * @return An instance of the builder.
         *
         * @since 2.1.0
         */
        public ChocoExecutorBuilder dontShowCommand() {
            this.showCommand = false;
            return this;
        }

        /**
         * Adds the choco download URL.
         * @param chocoDownloadURL The URL of the choco download.
         *
         * @return An instance of the builder.
         * @since 2.1.0
         */
        public ChocoExecutorBuilder addChocoDownloadURL(final String chocoDownloadURL) {
            this.chocoDownloadURL = chocoDownloadURL;
            return this;
        }

        /**
         * The method that actually builds an instance of ChocoExecutor.
         *
         * @return An instance of the ChocoExecutor.
         */
        public ChocoExecutor build() {
            return new ChocoExecutor(this);
        }
    }
}
