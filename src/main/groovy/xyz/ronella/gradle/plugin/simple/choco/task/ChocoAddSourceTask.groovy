package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

class ChocoAddSourceTask extends ChocoTask {

    @Input
    public String name

    @Input
    public String url

    public ChocoAddSourceTask() {
        super()
        description = 'Add source to where chocolatey search for a package'
        internalCommand = 'source'
        internalArgs += "add"
    }

    @Override
    public String executeCommand() {
        internalArgs += String.format("-n=\"%s\"", name)
        internalArgs += String.format("-s \"%s\"", url)
        super.executeCommand()
    }
}
