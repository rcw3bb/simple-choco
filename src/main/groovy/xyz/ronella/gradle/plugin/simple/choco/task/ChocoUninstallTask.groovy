package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoUninstallTask extends ChocoInstallTask {

    public ChocoUninstallTask() {
        super()
        description = 'Uninstall a package installed by chocolatey'
        internalCommand = 'uninstall'
    }
}
