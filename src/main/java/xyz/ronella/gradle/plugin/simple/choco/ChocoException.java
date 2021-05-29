package xyz.ronella.gradle.plugin.simple.choco;

/**
 * The base exception of simple-choco plugin.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoException extends Exception {
    public ChocoException() {
        super();
    }

    public ChocoException(Throwable cause) {
        super(cause);
    }

}
