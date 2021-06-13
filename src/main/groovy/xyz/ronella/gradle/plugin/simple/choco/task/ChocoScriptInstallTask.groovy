package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for installing choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
class ChocoScriptInstallTask extends ChocoScriptAdminTask {

    public ChocoScriptInstallTask() {
        description = "Install packages from chocolatey sources by script."
        internalCommand = "install"
        hasLogging = true
    }

    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultInstallArgs
    }

    @Override
    public String executeCommand() {
        setInternalZArgs()
        super.executeCommand()
    }

}
