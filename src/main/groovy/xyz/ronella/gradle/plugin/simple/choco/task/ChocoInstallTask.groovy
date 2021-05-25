package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * A convenience task for installing a choco package.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoInstallTask extends ChocoTask {

    protected String[] packageNames

    @Input
    public String[] getPackageNames() {
        return packageNames
    }

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames
    }

    public ChocoInstallTask() {
        super()
        description = 'Install packages from chocolatey sources.'
        internalCommand = 'install'
        isAdminMode = true
    }

    protected void setInternalZArgs() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalZArgs = pluginExt.defaultInstallArgs
    }

    @Override
    public String executeCommand() {
        setInternalZArgs()
        internalArgs = packageNames
        super.executeCommand()
    }
}
