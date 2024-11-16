package xyz.ronella.gradle.plugin.simple.choco.tools;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * An administration utility.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
final public class Administration {

    private Administration() {}

    /**
     * Detect if the plugin is running on elevated terminal.
     *
     * @return Returns true if it on elevated mode.
     * @since 1.1.0
     */
    public static boolean isElevatedMode() {
        final String pid = ManagementFactory.getRuntimeMXBean().getName().replace("@", "-");
        final String fileName = String.format("simple-choco-%s.dummy", pid);
        final File file = Paths.get(System.getenv("SystemRoot"), fileName).toFile();
        boolean output = false;
        try {
            if (file.createNewFile()) {
                file.delete();
                output = true;
            }
        } catch (IOException e) {
            final var deniedMessage = "Access is denied";
            if (!deniedMessage.equalsIgnoreCase(e.getMessage())) {
                e.printStackTrace(System.err);
            }
        }
        return output;
    }

}
