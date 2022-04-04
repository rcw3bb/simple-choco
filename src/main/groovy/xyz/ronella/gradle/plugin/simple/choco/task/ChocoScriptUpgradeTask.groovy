package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptUpgradeTask extends ChocoScriptInstallTask {

    ChocoScriptUpgradeTask() {
        super()
        description = 'Upgrade installed chocolatey packages by script.'
        internalCommand.convention('upgrade')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultUpgradeArgs.getOrElse([]))
    }
}
