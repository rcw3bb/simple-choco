package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input

/**
 * The base class of all the task in script mode.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptTask extends ChocoTask {

    @Input
    abstract ListProperty<List<String>> getPackages()

    ChocoScriptTask() {
        description = 'Creates a script that contains the packages before executing'
        packages.convention([])
    }

    @Override
    protected ListProperty<List<String>> packagesToScript() {
        return packages
    }

    @Override
    protected boolean scriptMode() {
        return true
    }

    @Override
    String executeCommand() {
        if (!internalCommand.isPresent() && (!command.isPresent() || command.get().size()<1)) {
            println "The command parameter is required"
        }
        else if (packages.get().size()<1) {
            println "At least one package is required"
        }
        else {
            return super.executeCommand()
        }
        return null
    }

}
