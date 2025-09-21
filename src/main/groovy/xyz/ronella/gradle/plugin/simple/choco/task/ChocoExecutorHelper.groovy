package xyz.ronella.gradle.plugin.simple.choco.task

import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.gradle.plugin.simple.choco.tools.Administration

/**
 * Helper class for configuring and building ChocoExecutor instances.
 * This class encapsulates the complex builder pattern logic used in ChocoTask
 * to create properly configured ChocoExecutor instances.
 *
 * @author Ron Webb
 * @since v3.1.0
 */
final class ChocoExecutorHelper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ChocoExecutorHelper() {
        throw new UnsupportedOperationException("ChocoExecutorHelper is a utility class and cannot be instantiated")
    }

    /**
     * Creates and configures a ChocoExecutor instance based on the provided ChocoTask.
     * This method encapsulates all the builder configuration logic that was previously
     * inline in the ChocoTask.executeCommand() method.
     *
     * @param task The ChocoTask instance containing all necessary configuration properties
     * @return A fully configured ChocoExecutor ready for execution
     */
    static ChocoExecutor createExecutor(ChocoTask task) {
        def extension = task.getSafeExtension()

        return ChocoExecutor.getBuilder()
                .addAutoInstall(extension.isAutoInstall.get())
                .addNoop(extension.isNoop.get())
                .addChocoHome(extension.chocoHome.getOrNull())
                .addAdminMode(task.isAdminMode.get())
                .addCommand(task.internalCommand.isPresent() ? task.internalCommand.get() : task.command.get())
                .addArgs(task.internalArgs.get().toArray((String[])[]))
                .addArgs(task.args.get().toArray((String[])[]))
                .addArgs(task.internalZArgs.get().toArray((String[])[]))
                .addZArgs(task.getZArgs().get().toArray((String[])[]))
                .addLogging(task.hasLogging.get())
                .addRunningOnAdmin(Administration.isElevatedMode())
                .addForceAdminMode(extension.forceAdminMode.get())
                .addScriptMode(task.scriptMode())
                .addCommands(task.commandsToScript().get())
                .addTaskName(task.name)
                .addNoScriptDeletion(extension.noScriptDeletion.get())
                .addChocoDownloadURL(extension.chocoDownloadURL.get())
                .build()
    }
}