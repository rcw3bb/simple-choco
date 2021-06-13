package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * This is the admin mode of ChocoTask
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoAdminTask extends ChocoTask {
    public ChocoAdminTask() {
        super()
        description = 'Executes any valid chocolatey commands in administration mode.'
        isAdminMode = true
    }
}
