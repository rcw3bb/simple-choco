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

    public String packageName

    @Input
    public String getPackageName() {
        return packageName
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName
    }

    public ChocoInstallTask() {
        super()
        description = 'Install a package from chocolatey sources'
        internalCommand = 'install'
        isAdminMode = true
    }

    @Override
    public String executeCommand() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco
        internalArgs += packageName
        internalArgs += pluginExt.defaultInstallArgs
        super.executeCommand()
    }
}
