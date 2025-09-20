package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPluginExtension
import xyz.ronella.gradle.plugin.simple.choco.tools.Administration

import javax.inject.Inject

/**
 * The simple-choco base task.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoTask extends DefaultTask {

    private final ObjectFactory objects

    @Internal
    abstract Property<SimpleChocoPluginExtension> getExtension()

    protected Property<Boolean> isAdminMode

    protected Property<Boolean> hasLogging

    protected Property<String> internalCommand

    protected ListProperty<String> internalArgs

    protected ListProperty<String> internalZArgs

    @Optional @Input
    abstract Property<String> getCommand()

    @Optional @Input
    abstract ListProperty<String> getArgs()

    @Optional @Input
    abstract ListProperty<String> getZArgs()

    @Inject
    ChocoTask(ObjectFactory objects) {
        this.objects = objects
        group = 'Simple Chocolatey'
        description = 'Executes any valid chocolatey commands.'
        command.convention('')
        args.convention([])
        getZArgs().convention([])
        isAdminMode = objects.property(Boolean.class)
        hasLogging = objects.property(Boolean.class)
        internalArgs = objects.listProperty(String.class)
        internalZArgs = objects.listProperty(String.class)
        internalCommand = objects.property(String.class)

        isAdminMode.convention(false)
        hasLogging.convention(false)
    }

    protected boolean scriptMode() {
        return false
    }

    protected ListProperty<List<String>> commandsToScript() {
        return objects.<List<String>>listProperty(List<String>.class)
    }

    @TaskAction
    String executeCommand() {
        def extension = getExtension().get()

        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addAutoInstall(extension.isAutoInstall.get())
                .addNoop(extension.isNoop.get())
                .addChocoHome(extension.chocoHome.getOrNull())
                .addAdminMode(isAdminMode.get())
                .addCommand(internalCommand.isPresent() ? internalCommand.get() : command.get())
                .addArgs(internalArgs.get().toArray((String[])[]))
                .addArgs(args.get().toArray((String[])[]))
                .addArgs(internalZArgs.get().toArray((String[])[]))
                .addZArgs(getZArgs().get().toArray((String[])[]))
                .addLogging(hasLogging.get())
                .addRunningOnAdmin(Administration.isElevatedMode())
                .addForceAdminMode(extension.forceAdminMode.get())
                .addScriptMode(scriptMode())
                .addCommands(commandsToScript().get())
                .addTaskName(name)
                .addNoScriptDeletion(extension.noScriptDeletion.get())
                .addChocoDownloadURL(extension.chocoDownloadURL.get())
                .build()

        executor.execute()
    }
}
