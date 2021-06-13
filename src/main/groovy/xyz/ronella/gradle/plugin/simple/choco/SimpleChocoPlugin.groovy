package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Plugin
import org.gradle.api.Project
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAddSourceTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAdminTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoInstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoListInstalledTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoRemoveSourceTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptAdminTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptInstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptUninstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptUpgradeTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoSourceListTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoUninstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoUpgradeChocoTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoUpgradeTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoVersionTask

/**
 * The simple-choco plugin implementation.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class SimpleChocoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('simple_choco', SimpleChocoPluginExtension)
        project.task('chocoAddSource', type: ChocoAddSourceTask)
        project.task('chocoAdminTask', type: ChocoAdminTask)
        project.task('chocoInstall', type: ChocoInstallTask)
        project.task('chocoListInstalled', type: ChocoListInstalledTask)
        project.task('chocoRemoveSource', type: ChocoRemoveSourceTask)
        project.task('chocoScript', type: ChocoScriptTask)
        project.task('chocoScriptAdmin', type: ChocoScriptAdminTask)
        project.task('chocoScriptInstall', type: ChocoScriptInstallTask)
        project.task('chocoScriptUninstall', type: ChocoScriptUninstallTask)
        project.task('chocoScriptUpgrade', type: ChocoScriptUpgradeTask)
        project.task('chocoSourceList', type: ChocoSourceListTask)
        project.task('chocoTask', type: ChocoTask)
        project.task('chocoUninstall', type: ChocoUninstallTask)
        project.task('chocoUpgradeChoco', type: ChocoUpgradeChocoTask)
        project.task('chocoUpgrade', type: ChocoUpgradeTask)
        project.task('chocoVersion', type: ChocoVersionTask)
    }
}