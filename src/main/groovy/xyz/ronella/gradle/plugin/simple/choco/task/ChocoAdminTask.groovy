package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoAdminTask extends ChocoTask {
    public ChocoAdminTask() {
        super()
        description = 'Executes any valid chocolatey commands in administration mode.'
        isAdminMode = true
    }
}
