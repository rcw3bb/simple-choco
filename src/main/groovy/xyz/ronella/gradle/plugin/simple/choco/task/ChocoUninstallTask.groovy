package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for uninstalling a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoUninstallTask extends ChocoInstallTask {

    @Inject
    ChocoUninstallTask(ObjectFactory objects) {
        super(objects)
        description = 'Uninstall packages installed by chocolatey.'
        internalCommand.convention('uninstall')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(getExtension().get().defaultUninstallArgs.getOrElse([]))
    }
}
