package xyz.ronella.gradle.plugin.simple.choco;

/**
 * The exception thrown when the there a problem creating the script file or writing into it.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
public class ChocoScriptException extends ChocoException {

    /**
     * Constructor that accepts a message.
     *
     * @param message The detail message.
     */
    public ChocoScriptException(String message) {
        super(message);
    }

}
