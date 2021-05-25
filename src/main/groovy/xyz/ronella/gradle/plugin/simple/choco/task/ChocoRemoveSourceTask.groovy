package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

/**
 * A convenience task for removing a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoRemoveSourceTask extends ChocoTask {

    public String sourceName

    @Input
    public String getSourceName() {
        return this.sourceName
    }

    public void setSourceName(String name) {
        this.sourceName = name
    }

    public ChocoRemoveSourceTask() {
        super()
        description = 'Remove a source to where chocolatey search for a package'
        isAdminMode = true
        internalCommand = 'source'
        internalArgs = ["remove"]
    }

    @Override
    public String executeCommand() {
        internalArgs += String.format("-n=%s", sourceName)
        super.executeCommand()
    }

}
