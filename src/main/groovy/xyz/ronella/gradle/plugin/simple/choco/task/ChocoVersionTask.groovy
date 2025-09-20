package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * A convenience task for showing the current choco version.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoVersionTask extends ChocoTask {

    @Inject
    ChocoVersionTask(ObjectFactory objects) {
        super(objects)
        description = 'Displays the chocolatey version.'
        internalArgs.add('--version')
    }

}
