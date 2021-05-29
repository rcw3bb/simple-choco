package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading the chocolatey application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoUpgradeChocoTask extends ChocoAdminTask {
    public ChocoUpgradeChocoTask() {
        super()
        description = 'Upgrades the current chocolatey.'
        internalCommand = 'upgrade'
        internalArgs += ["chocolatey", "-y"]
    }
}
