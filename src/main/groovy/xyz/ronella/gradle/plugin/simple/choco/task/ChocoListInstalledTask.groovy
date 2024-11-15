package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for listing choco installed packages.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class ChocoListInstalledTask extends ChocoTask {

    ChocoListInstalledTask() {
        super()
        description = 'Lists locally installed packages by chocolatey.'
        internalCommand.convention('list')
    }
}
