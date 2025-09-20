package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for listing choco sources.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoSourceListTask extends ChocoTask {
    @Inject
    ChocoSourceListTask(ObjectFactory objects) {
        super(objects)
        description = 'Displays the sources that the chocolatey is using.'
        internalCommand.convention('source')
        internalArgs.add('list')
    }
}
