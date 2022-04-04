package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

/**
 * A convenience task for adding a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoAddSourceTask extends ChocoRemoveSourceTask {

    @Input
    abstract Property<String> getUrl()

    ChocoAddSourceTask() {
        super()
        description = 'Adds a source to where chocolatey search for a package.'
        internalCommand.convention('source')
        internalArgs.set(['add'])
    }

    @Override
    String executeCommand() {
        internalArgs.add('-s')
        internalArgs.add(url.get())
        super.executeCommand()
    }
}
