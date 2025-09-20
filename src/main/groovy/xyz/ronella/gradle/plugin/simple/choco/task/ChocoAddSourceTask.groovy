package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import javax.inject.Inject

/**
 * A convenience task for adding a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoAddSourceTask extends AbstractChocoSourceTask {

    @Input
    abstract Property<String> getUrl()

    /**
     * The priority of the source.
     * @since 3.0.0
     */
    @Optional @Input
    abstract Property<Integer> getPriority()

    @Inject
    ChocoAddSourceTask(ObjectFactory objects) {
        super(objects)
        description = 'Adds a source to where chocolatey search for a package.'
        internalArgs.set(['add'])
    }

    @Override
    String executeCommand() {
        def sourceNameToAdd = sourceName.get()

        if (!getExtension().get().isNoop.get() && loadedSourceNames().contains(sourceNameToAdd)) {
            println("The source [${sourceNameToAdd}] is already existing")
        }
        else {
            internalArgs.add('-s')
            internalArgs.add(url.get())

            if (priority.isPresent()) {
                internalArgs.add('--priority')
                internalArgs.add(priority.get().toString())
            }

            super.executeCommand()
        }
    }
}
