package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for uninstalling choco packages by script.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptUninstallTask extends ChocoScriptInstallTask {

    ChocoScriptUninstallTask() {
        super()
        description = 'Uninstall packages installed by chocolatey by script.'
        internalCommand.convention('uninstall')
    }

    @Override
    protected void initInternalZArgs() {
        internalZArgs.addAll(EXTENSION.defaultUninstallArgs.getOrElse([]))
    }
}
