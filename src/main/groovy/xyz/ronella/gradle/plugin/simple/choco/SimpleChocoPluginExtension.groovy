package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

/**
 * The simple-choco support implementation.
 *
 * @author Ron Webb
 * @since v1.0.0
 */
abstract class SimpleChocoPluginExtension {

    /**
     * Indicates that the choco will be installed when not found.
     */
    abstract Property<Boolean> getIsAutoInstall()

    /**
     * Force the admin mode behavior.
     *
     * @since 1.1.0
     */
    abstract Property<Boolean> getForceAdminMode()

    /**
     * Don't the delete the script generated during script mode.
     *
     * @since 1.1.0
     */
    abstract Property<Boolean> getNoScriptDeletion()

    /**
     * Overrides the default location of choco.
     */
    abstract Property<File> getChocoHome()

    /**
     * Indicates that no changes will be made.
     */
    abstract Property<Boolean> getIsNoop()

    /**
     * Always add all the parameters held during install task.
     */
    abstract ListProperty<String> getDefaultInstallArgs()

    /**
     * Always add all the parameters held during uninstall task.
     */
    abstract ListProperty<String> getDefaultUninstallArgs()

    /**
     * Always add all the parameters held during upgrade task.
     */
    abstract ListProperty<String> getDefaultUpgradeArgs()

    /**
     * The URL where to download of the chocolatey binary.
     * @since 2.1.0
     */
    abstract Property<String> getChocoDownloadURL()

    SimpleChocoPluginExtension() {
        isAutoInstall.convention(true)
        forceAdminMode.convention(true)
        noScriptDeletion.convention(false)
        isNoop.convention(false)
        defaultInstallArgs.convention(['-y'])
        defaultUpgradeArgs.convention(['-y'])
        defaultUninstallArgs.convention(['-y'])
        chocoDownloadURL.convention('https://chocolatey.org/install.ps1')
    }

}
