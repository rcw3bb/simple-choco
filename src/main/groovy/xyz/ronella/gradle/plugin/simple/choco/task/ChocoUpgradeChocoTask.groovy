package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for upgrading the chocolatey application.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUpgradeChocoTask extends ChocoAdminTask {
    @Inject
    ChocoUpgradeChocoTask(ObjectFactory objects) {
        super(objects)
        description = 'Upgrades the current chocolatey.'
        internalCommand.convention('upgrade')
        internalArgs.addAll(['chocolatey', '-y'])
    }
}
