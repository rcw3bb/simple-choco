package xyz.ronella.gradle.plugin.simple.choco;

import xyz.ronella.gradle.plugin.simple.choco.tools.CommandRunner;
import xyz.ronella.gradle.plugin.simple.choco.tools.OSType;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    /**
     * Creates an instance of ChocoExecutor
     *
     * @param builder An instance of ChocoExecutorBuilder
     */
    private ChocoExecutor(ChocoExecutorBuilder builder) {
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

        prepareExecutables();
    }

    private void prepareExecutables() {
        Consumer<Supplier<File>> addExecLogic = ___execLogic -> {
            executables.add(___execLogic);
            isAutoInstall = false;
        };

        Optional.ofNullable(chocoHome).ifPresent(___chocoHome ->
                addExecLogic.accept(() -> Paths.get(chocoHome.getAbsolutePath(),ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile()));

        Optional.ofNullable(System.getenv("CHOCOLATEY_HOME")).ifPresent(___chocoHome ->
                addExecLogic.accept(() -> Paths.get(___chocoHome,ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile()));

        if (executables.size()==0) {
            executables.add(() -> Paths.get(ChocoInstaller.DEFAULT_INSTALL_LOCATION.toString(), ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toFile());
            Optional.ofNullable(getExecutableAuto()).ifPresent(___chocoHome -> executables.add(() -> ___chocoHome));
        }
    }

    private File getExecutableAuto() {
        StringBuilder sbFqfn = new StringBuilder();
        CommandRunner.runCommand((___output, ___error) -> sbFqfn.append(___output),"where", ChocoInstaller.EXECUTABLE);

        String fqfn = sbFqfn.toString();

        if (fqfn.length() > 0) {
            fqfn = fqfn.split("\\r\\n")[0]; //Just the first valid entry of where.
            File fileExec = new File(fqfn);
            if (fileExec.exists()) {
                return fileExec;
            }
        }

        return null;
    }

    private Optional<File> executable() {
        if (OSType.Windows.equals(osType)) {
            Optional<Supplier<File>> executable = executables.stream().filter(___execLogic -> ___execLogic.get().exists()).findFirst();
            if (executable.isPresent()) {
                Supplier<File> exec = executable.get();
                return Optional.of(exec.get());
            }
            else {
                try {
                    if (isAutoInstall) {
                        if (!isNoop) {
                            File installLocation = ChocoInstaller.DEFAULT_INSTALL_LOCATION.toFile();
                            if (!installLocation.exists()) {
                                ChocoInstaller.install();
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
                        String missingChocoExecutable = String.format("Cannot find %s and automatic installation is disabled, chocoHome property was set or CHOCOLATEY_HOME environment variable exists.", ChocoInstaller.EXECUTABLE);
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
        List<String> shellCommand = new ArrayList<>();
        shellCommand.add("powershell.exe");
        shellCommand.add("-NoProfile");
        shellCommand.add("-InputFormat");
        shellCommand.add("None");
        shellCommand.add("-ExecutionPolicy") ;
        shellCommand.add("Bypass");
        shellCommand.add("-Command");

        return new ArrayList<>(shellCommand);
    }

    private String quadQuote(String text) {
        return String.format("\"\"\"\"%s\"\"\"\"", text);
    }

    private String doubleQuote(String text) {
        return String.format("\"\"%s\"\"", text);
    }

    private String singleQuote(String text) {
        return String.format("\"%s\"", text);
    }

    private List<String> adminModeCommand(String executable, List<String> allArgs) {
        List<String> fullCommand = getPowershellCommand();

        StringBuilder sbArgs = new StringBuilder();
        allArgs.forEach(___arg -> sbArgs.append(sbArgs.length()>0 ? ",": "").append(quadQuote(___arg)));

        StringBuilder sbActualCommand = new StringBuilder("\"Start-Process ");
        sbActualCommand.append(quadQuote(executable));
        sbActualCommand.append(" -Wait -Verb runas");
        sbActualCommand.append(sbArgs.length()==0 ? "": " -argumentlist ").append(sbArgs.toString());
        sbActualCommand.append("\"");

        fullCommand.add(sbActualCommand.toString());
        return fullCommand;
    }

    private File getDataDirectory() {
        File dataDirectory = Paths.get(System.getenv("LOCALAPPDATA"), "simple-choco").toFile();
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        return dataDirectory;
    }

    private String getLogFile() {
        File dataDir = getDataDirectory();
        File chocoLogFile = Paths.get(dataDir.getAbsolutePath(), "chocolatey.log").toFile();
        return chocoLogFile.getAbsolutePath();
    }

    private List<String> generateLoggingArg(String logFile) {
        List<String> allArgs = new ArrayList<>();
        allArgs.add("--log-file");
        allArgs.add(logFile);
        return allArgs;
    }

    private List<String> prepareCommand(File chocoExecutable) {
        return prepareCommand(chocoExecutable, new ArrayList<>());
    }

    private List<String> prepareCommand(File chocoExecutable, List<String> packageInfo) {
        String executable = chocoExecutable.getAbsolutePath();
        List<String> allArgs = new ArrayList<>();
        List<String> fullCommand = new ArrayList<>();

        Optional.ofNullable(command).ifPresent(allArgs::add);
        allArgs.addAll(packageInfo);
        allArgs.addAll(args);
        allArgs.addAll(zArgs);

        List<String> commandToRun = new ArrayList<>();
        commandToRun.add(executable);
        commandToRun.addAll(allArgs);

        String logFile = getLogFile();

        System.out.println(String.join(" ", commandToRun));

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

    private String executeSingleCommand() {
        StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            List<String> fullCommand = prepareCommand(___executable);
            sbCommand.append(String.join(" ", fullCommand).trim());

            if (!isNoop) {
                CommandRunner.runCommand((___output, ___error)-> {
                    if (___error.length()>0) {
                        System.err.println(___error);
                    }
                    else {
                        System.out.println(___output);
                    }
                }, fullCommand.toArray(new String[]{}));
            }
        });
        return sbCommand.toString();
    }

    private List<String> buildScriptCommand(String scriptFullPath) {
        List<String> fullCommand = getPowershellCommand();
        String executable = "powershell.exe";

        if (isAdminMode) {
            StringBuilder sbActualCommand = new StringBuilder("\"Start-Process ");
            sbActualCommand.append(executable);
            sbActualCommand.append(" -Wait -Verb runas");
            sbActualCommand.append(" -argumentlist \"\"\"\"-NoProfile\"\"\"\",\"\"\"\"-InputFormat\"\"\"\",\"\"\"\"None\"\"\"\",\"\"\"\"-ExecutionPolicy\"\"\"\",\"\"\"\"Bypass\"\"\"\",\"\"\"\"-Command\"\"\"\",");
            sbActualCommand.append(quadQuote(String.format("%s %s", executable, doubleQuote(quadQuote(scriptFullPath)))));
            sbActualCommand.append("\"");

            fullCommand.add(sbActualCommand.toString());
        }
        else {
            fullCommand.add(singleQuote(scriptFullPath));
        }
        return fullCommand;
    }

    private void saveAndExecuteScriptFile(String script, Consumer<String> executeLogic) throws ChocoScriptException {
        File dataDir = getDataDirectory();
        File scriptFile = Paths.get(dataDir.getAbsolutePath(), String.format("%s.ps1", taskName)).toFile();

        try {
            String scriptFullPath = scriptFile.getAbsolutePath();

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
        StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            packages.forEach(___package -> {
                List<String> fullCommand = prepareCommand( ___executable, ___package);
                sbCommand.append(String.join(" ", fullCommand).trim());
                sbCommand.append("\n");
            });
            try {
                saveAndExecuteScriptFile(sbCommand.toString(), ___scriptFullPath -> {
                    List<String> fullCommand = buildScriptCommand(___scriptFullPath);

                    if (isNoop) {
                        sbCommand.append(String.join(" ", fullCommand));
                        System.out.println(String.format("Scripted on %s", ___scriptFullPath));
                    }
                    else {
                        CommandRunner.runCommand((___output, ___error)-> {
                            if (___error.length()>0) {
                                System.err.println(___error);
                            }
                            else {
                                System.out.println(___output);
                            }
                        }, fullCommand.toArray(new String[]{}));
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
    public static class ChocoExecutorBuilder {
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

        private ChocoExecutorBuilder() {
            args = new ArrayList<>();
            zArgs = new ArrayList<>();
        }

        /**
         * Add an OSType in the builder.
         * @param osType An instance of the OSType enum.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addOSType(OSType osType) {
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
        public ChocoExecutorBuilder addAutoInstall(boolean isAutoInstall) {
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
        public ChocoExecutorBuilder addChocoHome(File chocoHome) {
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
        public ChocoExecutorBuilder addCommand(String command) {
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
        public ChocoExecutorBuilder addArgs(String ... args) {
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
        public ChocoExecutorBuilder addZArgs(String ... args) {
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
        public ChocoExecutorBuilder addNoop(boolean noop) {
            this.isNoop = noop;
            return this;
        }

        /**
         * Adds an indication assembled choco command will be executed in admin mode.
         * @param isAdminMode Set to true to run the command in admin mode.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addAdminMode(boolean isAdminMode) {
            this.isAdminMode = isAdminMode;
            return this;
        }

        /**
         * Adds an indication that the choco must log its activities to a file.
         * @param hasLogging Set to true to enable choco logging.
         *
         * @return An instance of the builder.
         */
        public ChocoExecutorBuilder addLogging(boolean hasLogging) {
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
        public ChocoExecutorBuilder addRunningOnAdmin(boolean runningOnAdmin) {
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
        public ChocoExecutorBuilder addForceAdminMode(boolean forceAdminMode) {
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
        public ChocoExecutorBuilder addScriptMode(boolean scriptMode) {
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
        public ChocoExecutorBuilder addTaskName(String taskName) {
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
        public ChocoExecutorBuilder addPackages(List<List<String>> packages) {
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
        public ChocoExecutorBuilder addNoScriptDeletion(boolean noScriptDeletion) {
            this.noScriptDeletion = noScriptDeletion;
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
