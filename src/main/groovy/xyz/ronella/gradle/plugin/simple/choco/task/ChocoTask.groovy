package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

class ChocoTask extends DefaultTask {

    protected String[] internalArgs = []
    protected String internalCommand

    @Optional
    @Input
    public String command = ''

    @Optional
    @Input
    public String[] args = []

    @Optional
    @Input
    public String[] zArgs = []

    public ChocoTask() {
        group = 'Simple Chocolatey'
        description = 'Executes any valid choco commands.'
    }

    @TaskAction
    public String executeCommand() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco;

        if (null!=internalCommand) {
            command = internalCommand
        }

        ChocoExecutor executor = ChocoExecutor.getBuilder()
            .addAutoInstall(pluginExt.isAutoInstall)
            .addNoop(pluginExt.isNoop)
            .addChocoHome(pluginExt.chocoHome)
            .addCommand(command)
            .addArgs(internalArgs)
            .addArgs(args)
            .addZArgs(zArgs)
            .build();

        executor.execute();
    }
}
