package xyz.ronella.gradle.plugin.simple.choco.task

class ChocoUpgradeTask extends ChocoTask {
    public ChocoUpgradeTask() {
        super()
        description = 'Upgrade current chocolatey'
        internalCommand = 'upgrade'
        internalArgs += "chocolatey"
    }
}
