package xyz.ronella.gradle.plugin.simple.choco;

import org.gradle.api.GradleException;

/**
 * The base exception of simple-choco plugin.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoException extends GradleException {

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
