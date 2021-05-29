package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for installing a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoInstallTask extends ChocoAdminTask {

    protected String[] packages

    @Input
    public String[] getPackages() {
        return packages
    }

    public void setPackages(String[] packages) {
        this.packages = packages
    }

    public ChocoInstallTask() {
        super()
        description = 'Installs packages from the chocolatey sources.'
        internalCommand = 'install'
        hasLogging = true
    }

    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultInstallArgs
    }

    @Override
    public String executeCommand() {
        setInternalZArgs()
        internalArgs = packages
        super.executeCommand()
    }
}
