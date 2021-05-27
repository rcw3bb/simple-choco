package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for listing choco installed packages.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoListInstalledTask extends ChocoTask {

    public ChocoListInstalledTask() {
        super()
        description = 'Lists locally installed packages by chocolatey.'
        internalCommand = 'list'
        internalArgs += "--local-only"
    }
}
