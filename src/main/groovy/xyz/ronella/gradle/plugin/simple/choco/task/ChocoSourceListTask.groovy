package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for listing choco sources.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoSourceListTask extends ChocoTask {
    public ChocoSourceListTask() {
        super()
        description = 'Displays the sources that the chocolatey is using.'
        internalCommand = 'source'
        internalArgs += "list"
    }
}
