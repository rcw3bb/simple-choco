package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.tasks.Input

/**
 * A convenience task for adding a choco source.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class ChocoAddSourceTask extends ChocoRemoveSourceTask {

    protected String url

    @Input
    public String getUrl() {
        return url
    }

    public void setUrl(String url) {
        this.url = url
    }

    public ChocoAddSourceTask() {
        super()
        description = 'Add a source to where chocolatey search for a package'
        isAdminMode = true
        internalCommand = 'source'
        internalArgs = ["add"]
    }

    @Override
    public String executeCommand() {
        internalArgs += "-s"
        internalArgs += url
        super.executeCommand()
    }
}
