package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

/**
 * The base class of all the task in script mode.
 *
 * @author Ron Webb
 * @since 1.1.0
 */
class ChocoScriptTask extends ChocoTask {

    protected List<List<String>> packages = []

    @Input
    List<List<String>> getPackages() {
        return packages
    }

    void setPackages(List<List<String>> packages) {
        this.packages = packages
    }

    public ChocoScriptTask() {
        description = 'Creates a script that contains the packages before executing'
    }

    @Override
    protected List<List<String>> packagesToScript() {
        return packages
    }

    @Override
    protected boolean scriptMode() {
        return true
    }

    @Override
    public String executeCommand() {
        if (command==null || command.size()<1) {
            println "The command parameter is required"
        }
        else if (packages.size()<1) {
            println "At least one package is required"
        }
        else {
            return super.executeCommand()
        }
        return null
    }

}
