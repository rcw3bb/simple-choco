package xyz.ronella.gradle.plugin.simple.choco.task

/**
 * A convenience task for showing the current choco version.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoVersionTask extends ChocoTask {

    public ChocoVersionTask() {
        super()
        description = 'Displays the chocolatey version.'
        internalArgs += "--version"
    }

}
