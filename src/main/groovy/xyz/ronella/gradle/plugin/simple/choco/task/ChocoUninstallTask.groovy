package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for uninstalling a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUninstallTask extends ChocoInstallTask {

    public ChocoUninstallTask() {
        super()
        description = 'Uninstall some packages installed by chocolatey.'
        internalCommand = 'uninstall'
    }

    @Override
    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultUninstallArgs
    }
}
