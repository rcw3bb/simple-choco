package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * This is the admin mode of ChocoScriptTask
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptAdminTask extends ChocoScriptTask {

    ChocoScriptAdminTask() {
        super()
        description = 'Creates a script that contains the commands before execution in administration mode.'
        isAdminMode.convention(true)
    }

}
