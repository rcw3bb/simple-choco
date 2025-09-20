package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for upgrading choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptUpgradeTask extends ChocoScriptInstallTask {

    @Inject
    ChocoScriptUpgradeTask(ObjectFactory objects) {
        super(objects)
        description = 'Upgrade installed chocolatey packages by script.'
        internalCommand.convention('upgrade')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(getExtension().get().defaultUpgradeArgs.getOrElse([]))
    }
}
