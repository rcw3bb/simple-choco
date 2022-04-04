package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * This is the admin mode of ChocoScriptTask
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptAdminTask extends ChocoScriptTask {

    ChocoScriptAdminTask() {
        description = 'Creates a script that contains the packages before executing in administration mode'
        isAdminMode.convention(true)
    }

}
