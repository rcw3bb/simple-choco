package xyz.ronella.gradle.plugin.simple.choco;

import java.io.*;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChocoExecutor {

    private static final String EXECUTABLE = "choco.exe";

    private OSType osType;

    private ChocoExecutor(ChocoExecutorBuilder builder) {
        osType = builder.osType;
    }

    public Optional<File> executable() {
        if (OSType.Windows.equals(osType)) {
            try {
                Process process = new ProcessBuilder("where", EXECUTABLE).start();
                BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String fqfn = output.lines().collect(Collectors.joining());

                if (fqfn.length() > 0) {
                    fqfn = fqfn.split("\\r\\n")[0]; //Just the first valid entry of where.
                    File fileExec = new File(fqfn);
                    if (fileExec.exists()) {
                        return Optional.of(fileExec);
                    }
                }

                return Optional.empty();
            }
            catch(IOException ioe) {
                System.out.println(String.format("%s not found.", EXECUTABLE));
            }
        }
        System.out.println(String.format("%s OS is required.", OSType.Windows));
        return Optional.empty();
    }



    public static ChocoExecutorBuilder getBuilder() {
        return new ChocoExecutorBuilder();
    }

    public static class ChocoExecutorBuilder {
        private OSType osType;

        private ChocoExecutorBuilder() {}

        public ChocoExecutorBuilder addOSType(OSType osType) {
            this.osType = osType;
            return this;
        }

        public ChocoExecutor build() {
            return new ChocoExecutor(this);
        }
    }

}
