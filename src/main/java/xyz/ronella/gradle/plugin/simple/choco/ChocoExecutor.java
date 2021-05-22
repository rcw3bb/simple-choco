package xyz.ronella.gradle.plugin.simple.choco;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ChocoExecutor {

    public static final String LOCAL_CHOCO_DIR = Paths.get("build", "simple", "choco").toString();

    private OSType osType;
    private boolean isAutoInstall;
    private File chocoHome;
    private boolean isNoop;
    private final List<Supplier<File>> executables = new ArrayList<>();

    private ChocoExecutor(ChocoExecutorBuilder builder) {
        osType = builder.osType;
        isAutoInstall = builder.isAutoInstall;
        chocoHome = builder.chocoHome;
        isNoop = builder.isNoop;
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
        try {
            Process process = new ProcessBuilder("where", ChocoInstaller.EXECUTABLE).start();
            BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String fqfn = output.lines().collect(Collectors.joining());

            if (fqfn.length() > 0) {
                fqfn = fqfn.split("\\r\\n")[0]; //Just the first valid entry of where.
                File fileExec = new File(fqfn);
                if (fileExec.exists()) {
                    return fileExec;
                }
            }
        }
        catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        return null;
    };

    public Optional<File> executable() {
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

    public static ChocoExecutorBuilder getBuilder() {
        return new ChocoExecutorBuilder();
    }

    public static class ChocoExecutorBuilder {
        private OSType osType;
        private boolean isAutoInstall;
        private File chocoHome;
        private boolean isNoop;

        private ChocoExecutorBuilder() {}

        public ChocoExecutorBuilder addOSType(OSType osType) {
            this.osType = osType;
            return this;
        }

        public ChocoExecutorBuilder addAutoInstall() {
            this.isAutoInstall = true;
            return this;
        }

        public ChocoExecutorBuilder addChocoHome(File chocoHome) {
            this.chocoHome = chocoHome;
            return this;
        }

        public ChocoExecutorBuilder addNoop() {
            this.isNoop = true;
            return this;
        }

        public ChocoExecutor build() {
            return new ChocoExecutor(this);
        }
    }
}
