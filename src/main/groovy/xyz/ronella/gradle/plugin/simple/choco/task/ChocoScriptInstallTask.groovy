package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for installing choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptInstallTask extends AbstractChocoScriptPackageTask {

    @Inject
    ChocoScriptInstallTask(ObjectFactory objects) {
        super(objects)
        description = "Install packages from chocolatey sources by script."
        internalCommand.convention('install')
        hasLogging.convention(true)
    }

    protected void initInternalZArgs() {
        internalZArgs.addAll(getSafeExtension().defaultInstallArgs.getOrElse([]))
    }

    @Override
    String executeCommand() {
        initInternalZArgs()
        super.executeCommand()
    }

}
