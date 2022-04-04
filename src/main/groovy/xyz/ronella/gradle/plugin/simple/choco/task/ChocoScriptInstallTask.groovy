package xyz.ronella.gradle.plugin.simple.choco.task
/**
 * A convenience task for installing choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptInstallTask extends ChocoScriptAdminTask {

    ChocoScriptInstallTask() {
        description = "Install packages from chocolatey sources by script."
        internalCommand.convention('install')
        hasLogging.convention(true)
    }

    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultInstallArgs.getOrElse([]))
    }

    @Override
    String executeCommand() {
        initInternalZArgs()
        super.executeCommand()
    }

}
