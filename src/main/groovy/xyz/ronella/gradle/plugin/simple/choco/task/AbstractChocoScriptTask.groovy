package xyz.ronella.gradle.plugin.simple.choco.task
/**
 * The base class of all the task in script mode.
 *
 * @author Ron Webb
 * @since 2.2.0
 */
abstract class AbstractChocoScriptTask extends ChocoTask {

    AbstractChocoScriptTask() {
        super()
        description = 'Creates a script that contains the packages before executing'
    }

    @Override
    protected boolean scriptMode() {
        return true
    }

}
