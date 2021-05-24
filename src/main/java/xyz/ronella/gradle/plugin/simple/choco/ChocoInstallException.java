package xyz.ronella.gradle.plugin.simple.choco;

/**
 * The exception thrown when the automatic installation fail.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
public class ChocoInstallException extends ChocoException {
    public ChocoInstallException() {
        super();
    }

    public ChocoInstallException(Throwable cause) {
        super(cause);
    }
}
