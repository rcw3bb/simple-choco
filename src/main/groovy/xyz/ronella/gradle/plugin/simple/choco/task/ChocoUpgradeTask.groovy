package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUpgradeTask extends ChocoInstallTask {

    ChocoUpgradeTask() {
        super()
        description = 'Upgrade installed chocolatey packages.'
        internalCommand.convention('upgrade')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultUpgradeArgs.getOrElse([]))
    }

}
