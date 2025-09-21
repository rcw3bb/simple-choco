package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.DefaultTask
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
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

    private Provider<SimpleChocoPluginExtension> getExtensionProviderSafe() { //codenarc-disable MethodName
        if (!this.extension.isPresent()) {
            // Fallback: create provider directly from project (this will show deprecation warning)
            // This should only happen if task is used outside of the plugin registration
            logger.warn('Task was created outside of SimpleChocoPlugin.')
            this.extension.set(project.provider { project.extensions.simple_choco })
        }
        this.extension
    }

    /**
     * Gets the extension instance safely using a provider pattern.
     * This ensures that getExtension() always returns a value during task creation.
     */
    @Internal
    protected SimpleChocoPluginExtension getSafeExtension() {
        return getExtensionProviderSafe().get()
    }

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
        ChocoExecutor executor = ChocoExecutorHelper.createExecutor(this)
        executor.execute()
    }
}
