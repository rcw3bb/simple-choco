package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for upgrading a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUpgradeTask extends ChocoInstallTask {

    public ChocoUpgradeTask() {
        super()
        description = 'Upgrades some installed chocolatey packages'
        internalCommand = 'upgrade'
    }

    @Override
    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultUpgradeArgs
    }

    @Override
    public String executeCommand() {
        setInternalZArgs()
        super.executeCommand()
    }
}
