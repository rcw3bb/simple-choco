package xyz.ronella.gradle.plugin.simple.choco;

import org.gradle.api.GradleException;

import java.io.Serial;

/**
 * The base exception to simple-choco plugin.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoException extends GradleException {

    @Serial
    private static final long serialVersionUID = -4487371119491264610L;

    /**
     * Constructor
     */
    public ChocoException() {
        super();
    }

    /**
     * Constructor
     * @param message The exception message.
     */
    public ChocoException(final String message) {
        super(message);
    }

}
