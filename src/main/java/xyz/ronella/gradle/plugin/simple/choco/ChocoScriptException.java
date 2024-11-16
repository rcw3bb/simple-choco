package xyz.ronella.gradle.plugin.simple.choco;

import java.io.Serial;

/**
 * The exception thrown when the there a problem creating the script file or writing into it.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
public class ChocoScriptException extends ChocoException {

    @Serial
    private static final long serialVersionUID = -7649551499086850984L;

    /**
     * Constructor that accepts a message.
     *
     * @param message The detail message.
     */
    public ChocoScriptException(final String message) {
        super(message);
    }

}
