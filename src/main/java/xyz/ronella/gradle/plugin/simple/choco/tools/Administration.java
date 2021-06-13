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
public class Administration {

    private Administration() {}

    /**
     * Detect if the plugin is running on elevated terminal.
     *
     * @return Returns true if it on elevated mode.
     * @since 1.1.0
     */
    public static boolean isElevatedMode() {
        String pid = ManagementFactory.getRuntimeMXBean().getName().replace("@", "-");
        String fileName = String.format("simple-choco-%s.dummy", pid);
        File file = Paths.get(System.getenv("SystemRoot"), fileName).toFile();
        try {
            if (file.createNewFile()) {
                file.delete();
                return true;
            }
        } catch (IOException e) {
            if ("Access is denied".equalsIgnoreCase(e.getMessage())) {
                return false;
            }
            else {
                e.printStackTrace(System.err);
            }
        }
        return false;
    }

}
