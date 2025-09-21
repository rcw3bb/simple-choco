package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for upgrading a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUpgradeTask extends ChocoInstallTask {

    @Inject
    ChocoUpgradeTask(ObjectFactory objects) {
        super(objects)
        description = 'Upgrade installed chocolatey packages.'
        internalCommand.convention('upgrade')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(getSafeExtension().defaultUpgradeArgs.getOrElse([]))
    }

}
