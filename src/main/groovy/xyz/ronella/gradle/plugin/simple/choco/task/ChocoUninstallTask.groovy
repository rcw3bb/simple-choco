package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for uninstalling a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUninstallTask extends ChocoInstallTask {

    public ChocoUninstallTask() {
        super()
        description = 'Uninstall a package installed by chocolatey'
        internalCommand = 'uninstall'
    }
}
