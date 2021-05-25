package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension

/**
 * The simple-choco base task.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoTask extends DefaultTask {

    protected String[] internalArgs = []
    protected String[] internalZArgs = []
    protected String internalCommand
    protected boolean isAdminMode;

    protected String command = ''
    protected String[] args = []
    protected String[] zArgs = []

    @Optional
    @Input
    public String getCommand() {
        return this.command
    }

    public void setCommand(String command) {
        this.command = command
    }

    @Optional
    @Input
    public String[] getArgs() {
        return this.args
    }

    @Optional
    @Input
    public String[] getZArgs() {
        return this.zArgs
    }

    public void setArgs(String[] args) {
        this.args = args
    }

    public void setZArgs(String[] args) {
        this.zArgs = args
    }

    public ChocoTask() {
        group = 'Simple Chocolatey'
        description = 'Executes any valid chocolatey commands.'
    }

    @TaskAction
    public String executeCommand() {
        SimpleChocoPluginExtension pluginExt = project.extensions.simple_choco

        if (null!=internalCommand) {
            command = internalCommand
        }

        ChocoExecutor executor = ChocoExecutor.getBuilder()
            .addAutoInstall(pluginExt.isAutoInstall)
            .addNoop(pluginExt.isNoop)
            .addChocoHome(pluginExt.chocoHome)
            .addAdminMode(isAdminMode)
            .addCommand(command)
            .addArgs(internalArgs)
            .addArgs(args)
            .addArgs(internalZArgs)
            .addZArgs(zArgs)
            .build()

        executor.execute();
    }
}
