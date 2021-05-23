package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoListInstalledTask extends ChocoTask {

    public ChocoListInstalledTask() {
        super()
        description = 'List locally installed packages by chocolatey'
        internalCommand = 'list'
        internalArgs += "--local-only"
    }
}
