package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

class ChocoInstallTask extends ChocoTask {

    @Input
    public String packageName

    public ChocoInstallTask() {
        super()
        description = 'Install a package from chocolatey sources'
        internalCommand = 'install'
    }

    @Override
    public String executeCommand() {
        internalArgs += String.format("\"%s\"", packageName)
        super.executeCommand()
    }
}
