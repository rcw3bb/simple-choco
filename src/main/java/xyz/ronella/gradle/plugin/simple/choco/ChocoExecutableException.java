package xyz.ronella.gradle.plugin.simple.choco;

import java.io.Serial;

/**
 * Indicates that the chocolatey executable cannot be found.
 *
 * @author Ron Webb
 * @since 2.0.0
 */
public class ChocoExecutableException extends ChocoException {

    @Serial
    private static final long serialVersionUID = 1488334312896743160L;

    /**
     * Constructor
     */
    public ChocoExecutableException() {
        super("Chocolatey executable not found.");
    }
}
