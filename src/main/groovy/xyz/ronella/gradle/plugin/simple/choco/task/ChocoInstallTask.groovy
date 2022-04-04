package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input

/**
 * A convenience task for installing a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoInstallTask extends ChocoAdminTask {

    @Input
    abstract ListProperty<String> getPackages();

    ChocoInstallTask() {
        super()
        description = 'Install packages from the chocolatey sources.'
        internalCommand.convention('install')
        hasLogging.convention(true)
    }

    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultInstallArgs.getOrElse([]))
    }

    @Override
    String executeCommand() {
        initInternalZArgs()
        internalArgs.addAll(packages.get())
        super.executeCommand()
    }
}
