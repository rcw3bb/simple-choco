package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

class ChocoRemoveSourceTask extends ChocoTask {

    public String sourceName

    @Input
    public String getSourceName() {
        return this.getSourceName()
    }

    public void setSourceName(String name) {
        this.sourceName = name
    }

    public ChocoRemoveSourceTask() {
        super()
        description = 'Remove a source to where chocolatey search for a package'
        isAdminMode = true
        internalCommand = 'source'
        internalArgs += "remove"
    }

    @Override
    public String executeCommand() {
        internalArgs += String.format("-n=%s", sourceName)
        super.executeCommand()
    }

}
