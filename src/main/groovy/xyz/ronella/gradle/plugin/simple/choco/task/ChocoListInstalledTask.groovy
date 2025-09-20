package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for listing choco installed packages.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoListInstalledTask extends ChocoTask {

    @Inject
    ChocoListInstalledTask(ObjectFactory objects) {
        super(objects)
        description = 'Lists locally installed packages by chocolatey.'
        internalCommand.convention('list')
    }
}
