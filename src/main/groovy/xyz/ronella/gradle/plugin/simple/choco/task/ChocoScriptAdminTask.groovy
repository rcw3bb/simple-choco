package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * This is the admin mode of ChocoScriptTask
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptAdminTask extends ChocoScriptTask {

    @Inject
    ChocoScriptAdminTask(ObjectFactory objects) {
        super(objects)
        description = 'Creates a script that contains the commands before execution in administration mode.'
        isAdminMode.convention(true)
    }

}
