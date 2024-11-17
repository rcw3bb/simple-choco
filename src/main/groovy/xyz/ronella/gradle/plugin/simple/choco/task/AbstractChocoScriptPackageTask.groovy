package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.ChocoScriptException

/**
 * The base class of all the task in package mode.
 *
 * @author Ron Webb
 * @since 3.0.0
 */
abstract class AbstractChocoScriptPackageTask extends AbstractChocoScriptAdminTask {

    @Input
    abstract ListProperty<List<String>> getPackages()

    AbstractChocoScriptPackageTask() {
        super()
        description = 'Creates a script that contains the packages before executing in package mode'
        packages.convention([])
    }

    @Override
    protected ListProperty<List<String>> commandsToScript() {
        return packages
    }

    @Override
    String executeCommand() {
        if (!internalCommand.isPresent() && (!command.isPresent() || command.get().size()<1)) {
            throw new ChocoScriptException("The command parameter is required")
        }
        else if (packages.get().isEmpty()) {
            throw new ChocoScriptException("At least one package is required")
        }
        else {
            return super.executeCommand()
        }
    }

}
