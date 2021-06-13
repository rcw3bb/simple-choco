package xyz.ronella.gradle.plugin.simple.choco

/**
 * The simple-choco support implementation.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class SimpleChocoPluginExtension {

    /**
     * Indicates that the choco will be installed when not found.
     */
    public boolean isAutoInstall = true

    /**
     * Force the admin mode behavior.
     *
     * @since 1.1.0
     */
    public boolean forceAdminMode

    /**
     * Don't the delete the script generated during script mode.
     *
     * @since 1.1.0
     */
    public boolean noScriptDeletion

    /**
     * Overrides the default location of choco.
     */
    public File chocoHome

    /**
     * Indicates that no changes will be made.
     */
    public boolean isNoop

    /**
     * Always add all the parameters held during install task.
     */
    public String[] defaultInstallArgs = []

    /**
     * Always add all the parameters held during uninstall task.
     */
    public String[] defaultUninstallArgs = []

    /**
     * Always add all the parameters held during upgrade task.
     */
    public String[] defaultUpgradeArgs = []

}
