package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * This is the admin mode of ChocoTask
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoAdminTask extends ChocoTask {
    @Inject
    ChocoAdminTask(ObjectFactory objects) {
        super(objects)
        description = 'Executes any valid chocolatey commands in administration mode.'
        isAdminMode.convention(true)
    }
}
