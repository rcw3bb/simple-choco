package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for upgrading the chocolatey application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUpgradeChocoTask extends ChocoAdminTask {
    ChocoUpgradeChocoTask() {
        super()
        description = 'Upgrades the current chocolatey.'
        internalCommand.convention('upgrade')
        internalArgs.addAll(['chocolatey', '-y'])
    }
}
