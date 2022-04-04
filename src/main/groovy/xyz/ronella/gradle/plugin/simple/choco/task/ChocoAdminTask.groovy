package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * This is the admin mode of ChocoTask
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoAdminTask extends ChocoTask {
    ChocoAdminTask() {
        super()
        description = 'Executes any valid chocolatey commands in administration mode.'
        isAdminMode.convention(true)
    }
}
