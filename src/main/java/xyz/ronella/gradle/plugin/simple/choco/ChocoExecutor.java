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
        prepareExecutables();
    }

    private final void prepareExecutables() {
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

    public String execute() {
        StringBuilder sbCommand = new StringBuilder();
        executable().ifPresent(___executable -> {
            String executable = ___executable.getAbsolutePath();
            List<String> allArgs = new ArrayList<>();
            List<String> fullCommand = new ArrayList<>();

            Optional.ofNullable(command).ifPresent(allArgs::add);
            allArgs.addAll(args);
            allArgs.addAll(zArgs);

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

    public static ChocoExecutorBuilder getBuilder() {
        return new ChocoExecutorBuilder();
    }

    public static class ChocoExecutorBuilder {
        private OSType osType;
        private boolean isAutoInstall;
        private File chocoHome;
        private boolean isNoop;
        private String command;
        private boolean isAdminMode;
        private List<String> args;
        private List<String> zArgs;

        private ChocoExecutorBuilder() {
            args = new ArrayList<>();
            zArgs = new ArrayList<>();
        }

        public ChocoExecutorBuilder addOSType(OSType osType) {
            this.osType = osType;
            return this;
        }

        public ChocoExecutorBuilder addAutoInstall(boolean isAutoInstall) {
            this.isAutoInstall = isAutoInstall;
            return this;
        }

        public ChocoExecutorBuilder addChocoHome(File chocoHome) {
            this.chocoHome = chocoHome;
            return this;
        }

        public ChocoExecutorBuilder addCommand(String command) {
            this.command = command;
            return this;
        }

        public ChocoExecutorBuilder addArgs(String ... args) {
            if (args.length>0) {
                this.args.addAll(Arrays.asList(args));
            }
            return this;
        }

        public ChocoExecutorBuilder addZArgs(String ... args) {
            if (args.length>0) {
                this.zArgs.addAll(Arrays.asList(args));
            }
            return this;
        }

        public ChocoExecutorBuilder addNoop(boolean noop) {
            this.isNoop = noop;
            return this;
        }

        public ChocoExecutorBuilder addAdminMode(boolean isAdminMode) {
            this.isAdminMode = isAdminMode;
            return this;
        }

        public ChocoExecutor build() {
            return new ChocoExecutor(this);
        }
    }
}
