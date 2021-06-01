package xyz.ronella.gradle.plugin.simple.choco

/**
 * The simple-choco support implementation.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
class SimpleChocoPluginExtension {

    public boolean isAutoInstall = true

    public boolean forceAdminMode;

    public File chocoHome

    public boolean isNoop

    public String[] defaultInstallArgs = []

    public String[] defaultUninstallArgs = []

    public String[] defaultUpgradeArgs = []

}
