package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading the chocolatey application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUpgradeChocoTask extends ChocoTask {
    public ChocoUpgradeChocoTask() {
        super()
        isAdminMode = true
        description = 'Upgrade current chocolatey.'
        internalCommand = 'upgrade'
        internalArgs += "chocolatey"
    }
}
