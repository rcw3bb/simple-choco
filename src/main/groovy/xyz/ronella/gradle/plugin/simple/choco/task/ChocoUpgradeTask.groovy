package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading chocolatey application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUpgradeTask extends ChocoTask {
    public ChocoUpgradeTask() {
        super()
        isAdminMode = true
        description = 'Upgrade current chocolatey'
        internalCommand = 'upgrade'
        internalArgs += "chocolatey"
    }
}
