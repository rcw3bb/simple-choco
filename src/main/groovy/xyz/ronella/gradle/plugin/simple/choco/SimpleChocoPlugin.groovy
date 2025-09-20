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
        def extension = project.extensions.create('simple_choco', SimpleChocoPluginExtension)
        
        project.tasks.register('chocoAddSource', ChocoAddSourceTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoAdminTask', ChocoAdminTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoInstall', ChocoInstallTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoListInstalled', ChocoListInstalledTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoRemoveSource', ChocoRemoveSourceTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoScript', ChocoScriptTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoScriptAdmin', ChocoScriptAdminTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoScriptInstall', ChocoScriptInstallTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoScriptUninstall', ChocoScriptUninstallTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoScriptUpgrade', ChocoScriptUpgradeTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoSourceList', ChocoSourceListTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoTask', ChocoTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoUninstall', ChocoUninstallTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoUpgradeChoco', ChocoUpgradeChocoTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoUpgrade', ChocoUpgradeTask) { task ->
            task.extension.set(extension)
        }
        project.tasks.register('chocoVersion', ChocoVersionTask) { task ->
            task.extension.set(extension)
        }
    }
}