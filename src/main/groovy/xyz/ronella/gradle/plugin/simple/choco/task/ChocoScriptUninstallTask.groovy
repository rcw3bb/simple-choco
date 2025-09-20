package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for uninstalling choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptUninstallTask extends ChocoScriptInstallTask {

    @Inject
    ChocoScriptUninstallTask(ObjectFactory objects) {
        super(objects)
        description = 'Uninstall packages installed by chocolatey by script.'
        internalCommand.convention('uninstall')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(getExtension().get().defaultUninstallArgs.getOrElse([]))
    }
}
