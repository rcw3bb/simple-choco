package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

class ChocoTask extends DefaultTask {

    @TaskAction
    def executeCommand() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco;
        pluginExt.writeln("Hello World")
        pluginExt.verbose = false
    }
}
