package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for uninstalling a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUninstallTask extends ChocoInstallTask {

    ChocoUninstallTask() {
        super()
        description = 'Uninstall packages installed by chocolatey.'
        internalCommand.convention('uninstall')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultUninstallArgs.getOrElse([]))
    }
}
