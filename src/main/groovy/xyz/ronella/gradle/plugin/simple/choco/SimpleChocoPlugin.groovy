package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Plugin
import org.gradle.api.Project
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoTask

class SimpleChocoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('simple_choco', SimpleChocoPluginExtension)
        project.task('chocoTask', type: ChocoTask)
    }
}