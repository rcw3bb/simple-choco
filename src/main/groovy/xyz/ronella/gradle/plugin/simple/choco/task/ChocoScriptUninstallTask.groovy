package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for uninstalling choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
class ChocoScriptUninstallTask extends ChocoScriptInstallTask {

    public ChocoScriptUninstallTask() {
        super()
        description = 'Uninstall packages installed by chocolatey by script.'
        internalCommand = 'uninstall'
    }

    @Override
    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultUninstallArgs
    }
}
