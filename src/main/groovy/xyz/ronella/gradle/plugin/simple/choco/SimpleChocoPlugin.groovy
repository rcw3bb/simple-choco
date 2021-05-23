package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Plugin
import org.gradle.api.Project
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAddSourceTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoInstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoListInstalledTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoSourceListTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoUninstallTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoUpgradeTask
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoVersionTask

class SimpleChocoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('simple_choco', SimpleChocoPluginExtension)
        project.task('chocoTask', type: ChocoTask)
        project.task('chocoVersion', type: ChocoVersionTask)
        project.task('chocoSourceList', type: ChocoSourceListTask)
        project.task('chocoAddSource', type: ChocoAddSourceTask)
        project.task('chocoListInstalled', type: ChocoListInstalledTask)
        project.task('chocoInstall', type: ChocoInstallTask)
        project.task('chocoUninstall', type: ChocoUninstallTask)
        project.task('chocoUpgrade', type: ChocoUpgradeTask)
    }
}