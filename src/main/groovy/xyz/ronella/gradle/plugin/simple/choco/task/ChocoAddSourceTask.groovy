package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

/**
 * A convenience task for adding a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoAddSourceTask extends AbstractChocoSourceTask {

    @Input
    abstract Property<String> getUrl()

    ChocoAddSourceTask() {
        super()
        description = 'Adds a source to where chocolatey search for a package.'
        internalArgs.set(['add'])
    }

    @Override
    String executeCommand() {
        def sourceNameToAdd = sourceName.get()

        if (!EXTENSION.isNoop.get() && loadedSourceNames().contains(sourceNameToAdd)) {
            println("The source [${sourceNameToAdd}] is already existing")
        }
        else {
            internalArgs.add('-s')
            internalArgs.add(url.get())
            super.executeCommand()
        }
    }
}
