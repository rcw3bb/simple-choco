package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input

import javax.inject.Inject

/**
 * A convenience task for installing a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoInstallTask extends ChocoAdminTask {

    @Input
    abstract ListProperty<String> getPackages();

    @Inject
    ChocoInstallTask(ObjectFactory objects) {
        super(objects)
        description = 'Install packages from the chocolatey sources.'
        internalCommand.convention('install')
        hasLogging.convention(true)
    }

    protected void initInternalZArgs() {
        internalZArgs.addAll(getExtension().get().defaultInstallArgs.getOrElse([]))
    }

    @Override
    String executeCommand() {
        initInternalZArgs()
        internalArgs.addAll(packages.get())
        super.executeCommand()
    }
}
