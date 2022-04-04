package xyz.ronella.gradle.plugin.simple.choco;

/**
 * Indicates that the chocolatey executable cannot be found.
 *
 * @author Ron Webb
 * @since 2.0.0
 */
public class ChocoExecutableException extends ChocoException {

    public ChocoExecutableException() {
        super("Chocolatey executable not found.");
    }
}
