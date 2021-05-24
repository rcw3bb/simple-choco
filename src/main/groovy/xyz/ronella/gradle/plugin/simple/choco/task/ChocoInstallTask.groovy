package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

class ChocoInstallTask extends ChocoTask {

    @Input
    public String packageName

    public ChocoInstallTask() {
        super()
        description = 'Install a package from chocolatey sources'
        internalCommand = 'install'
        isAdminMode = true
    }

    @Override
    public String executeCommand() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalArgs += packageName
        internalArgs += pluginExt.defaultInstallArgs
        super.executeCommand()
    }
}
