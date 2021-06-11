package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for upgrading choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
class ChocoScriptUpgradeTask extends ChocoScriptInstallTask {

    public ChocoScriptUpgradeTask() {
        super()
        description = 'Upgrade installed chocolatey packages by script.'
        internalCommand = 'upgrade'
    }

    @Override
    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultUpgradeArgs
    }
}
