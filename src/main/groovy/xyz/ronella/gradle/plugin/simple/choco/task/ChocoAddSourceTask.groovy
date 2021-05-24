package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

class ChocoAddSourceTask extends ChocoRemoveSourceTask {

    @Input
    public String url

    public ChocoAddSourceTask() {
        super()
        description = 'Add a source to where chocolatey search for a package'
        isAdminMode = true
        internalCommand = 'source'
        internalArgs += "add"
    }

    @Override
    public String executeCommand() {
        internalArgs += "-s"
        internalArgs += url
        super.executeCommand()
    }
}
