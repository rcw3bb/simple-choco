package xyz.ronella.gradle.plugin.simple.choco;

import java.io.Serial;

/**
 * The exception thrown when the automatic installation fail.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoInstallException extends ChocoException {

    @Serial
    private static final long serialVersionUID = 964486709520991944L;

    /**
     * Constructor
     */
    public ChocoInstallException() {
        super();
    }
}
