package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * The base class of all the task in script mode.
 *
 * @author Ron Webb
 * @since 3.0.0
 */
abstract class AbstractChocoScriptTask extends ChocoTask {

    @Inject
    AbstractChocoScriptTask(ObjectFactory objects) {
        super(objects)
        description = 'Creates a script that contains the packages before executing'
    }

    @Override
    protected boolean scriptMode() {
        return true
    }

}
