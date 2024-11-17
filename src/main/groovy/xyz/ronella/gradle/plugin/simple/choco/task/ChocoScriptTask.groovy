package xyz.ronella.gradle.plugin.simple.choco.task;

import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.ChocoScriptException;

/**
 * The base class of all the script task in command mode.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
abstract class ChocoScriptTask extends AbstractChocoScriptTask {

    @Input
    abstract ListProperty<List<String>> getCommands()

    ChocoScriptTask() {
        super()
        description = 'Creates a script that contains the commands before execution.'
        commands.convention([])
    }

    @Override
    protected ListProperty<List<String>> commandsToScript() {
        return commands
    }

    @Override
    String executeCommand() {
        if (internalCommand.isPresent() || (command.isPresent() && command.get().size()>0)) {
            throw new ChocoScriptException("The command parameter is not allowed")
        }
        else if (commands.get().isEmpty()) {
            throw new ChocoScriptException("At least one command is required")
        }
        else {
            return super.executeCommand()
        }
    }
}
