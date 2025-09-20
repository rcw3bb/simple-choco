package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.trivial.handy.RegExMatcher

import javax.inject.Inject
import java.util.stream.Collectors

/**
 * A convenience task for removing a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoRemoveSourceTask extends AbstractChocoSourceTask {

    @Inject
    ChocoRemoveSourceTask(ObjectFactory objects) {
        super(objects)
        description = 'Removes a source to where chocolatey search for a package.'
        internalArgs.add('remove')
    }

    @Override
    String executeCommand() {
        def theSourceName = sourceName.get()

        if (getExtension().get().isNoop.get() || loadedSourceNames().contains(theSourceName)) {
            super.executeCommand()
        }
        else {
            println("The source [${theSourceName}] doesn't exist")
        }
    }

}
