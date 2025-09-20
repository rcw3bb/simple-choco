package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * This is the admin mode of ChocoScriptTask
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class AbstractChocoScriptAdminTask extends AbstractChocoScriptTask {

    @Inject
    AbstractChocoScriptAdminTask(ObjectFactory objects) {
        super(objects)
        description = 'Creates a script that contains the packages before executing in administration mode'
        isAdminMode.convention(true)
    }

}
