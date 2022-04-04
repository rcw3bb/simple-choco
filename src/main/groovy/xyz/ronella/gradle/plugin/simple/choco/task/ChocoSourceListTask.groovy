package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for listing choco sources.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoSourceListTask extends ChocoTask {
    ChocoSourceListTask() {
        super()
        description = 'Displays the sources that the chocolatey is using.'
        internalCommand.convention('source')
        internalArgs.add('list')
    }
}
