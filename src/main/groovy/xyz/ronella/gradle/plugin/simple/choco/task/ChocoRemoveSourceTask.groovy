package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

/**
 * A convenience task for removing a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoRemoveSourceTask extends ChocoAdminTask {

    @Input
    abstract Property<String> getSourceName()

    ChocoRemoveSourceTask() {
        super()
        description = 'Removes a source to where chocolatey search for a package.'
        internalCommand.convention('source')
        internalArgs.add('remove')
    }

    @Override
    String executeCommand() {
        internalArgs.add(String.format("-n=%s", sourceName.get()))
        super.executeCommand()
    }

}
