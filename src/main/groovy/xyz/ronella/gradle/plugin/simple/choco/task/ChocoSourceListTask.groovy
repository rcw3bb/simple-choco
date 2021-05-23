package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoSourceListTask extends ChocoTask {
    public ChocoSourceListTask() {
        super()
        description = 'Displays the sources that the chocolatey is using.'
        internalCommand = 'source'
        internalArgs += "list"
    }
}
