package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoVersionTask extends ChocoTask {

    public ChocoVersionTask() {
        super()
        description = 'Displays the chocolatey version.'
        internalArgs += "--version"
    }

}
