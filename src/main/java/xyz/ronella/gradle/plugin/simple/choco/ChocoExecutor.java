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
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The class that assembles the appropriate choco command and execute if possible.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoExecutor {

    private final OSType osType;
    private boolean isAutoInstall;
    private final File chocoHome;
    private final boolean isNoop;
    private final boolean isAdminMode;
    private final List<Supplier<File>> executables;
    private final String command;
    private final List<String> args;
    private final List<String> zArgs;
    private boolean hasLogging;

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
        isAdminMode = builder.isAdminMode;
        command = builder.command;
        args = builder.args;
        zArgs = builder.zArgs;
        hasLogging = builder.hasLogging;
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
    };

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
                                return executable();
                            }
                        }
                    }
                    else {
                        System.out.println("Automatic installation is not possible or it is disabled.");
                    }
                } catch (ChocoInstallException cie) {
                    System.out.println("Chocolatey installation failed.");
                }
            }
        }
        else {
            System.out.println(String.format("%s OS is required.", OSType.Windows));
        }
        return Optional.empty();
    }

    /**
     * Actually execute the assembled choco command.
     *
     * @return The command that wil be exeecuted.
     */
    public String execute() {
        StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            String executable = ___executable.getAbsolutePath();
            List<String> allArgs = new ArrayList<>();
            List<String> fullCommand = new ArrayList<>();

            Optional.ofNullable(command).ifPresent(allArgs::add);
            allArgs.addAll(args);
            allArgs.addAll(zArgs);

            if (hasLogging) {
                File chocoLogDir = Paths.get(System.getenv("LOCALAPPDATA"), "simple-choco").toFile();
                if (!chocoLogDir.exists()) {
                    chocoLogDir.mkdirs();
                }
                File chocoLogFile = Paths.get(chocoLogDir.getAbsolutePath(), "chocolatey.log").toFile();

                allArgs.add("--log-file");
                allArgs.add(chocoLogFile.getAbsolutePath());
            }

            if (isAdminMode) {
                Function<String, String> quadQuote = (___text -> String.format("\"\"\"\"%s\"\"\"\"", ___text));

                StringBuilder sbArgs = new StringBuilder();
                allArgs.forEach(___arg -> sbArgs.append(sbArgs.length()>0 ? ",": "").append(quadQuote.apply(___arg)));

                StringBuilder sbActualCommand = new StringBuilder("\"Start-Process ");
                sbActualCommand.append(quadQuote.apply(executable));
                sbActualCommand.append(" -Wait -Verb runas");
                sbActualCommand.append(sbArgs.length()==0 ? "": " -argumentlist ").append(sbArgs.toString());
                sbActualCommand.append("\"");

                fullCommand.add("powershell.exe");
                fullCommand.add("-NoProfile");
                fullCommand.add("-InputFormat");
                fullCommand.add("None");
                fullCommand.add("-ExecutionPolicy") ;
                fullCommand.add("Bypass");
                fullCommand.add("-Command");
                fullCommand.add(sbActualCommand.toString());
            } else {
                fullCommand.add(executable);
                fullCommand.addAll(allArgs);
            }

            sbCommand.append(String.join(" ", fullCommand).trim());
            System.out.println(String.format("OS type: %s", osType));
            System.out.println(String.format("Command: %s", sbCommand.toString()));

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
         * The method that actually builds an instance of ChocoExecutor.
         *
         * @return An instance of the ChocoExecutor.
         */
        public ChocoExecutor build() {
            return new ChocoExecutor(this);
        }
    }
}
