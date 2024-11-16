# Changelog

## 2.1.0 : 2024-11-16

### New

* Usage of **version catalog** for dependency management.
* The **source compatibility** is now **Java 21**.
* **Adding package source** only works if the target source is **not yet registered**.
* **Removing package source** only works if the target source is **already registered**.
* Make the **choco download URL configurable**.
* All the **default arguments** *(i.e. defaultInstallArgs, defaultUpgradeArgs and defaultUninstallArgs)* in extensions are now defaulted with **-y**.
* The **forceAdminMode** in extensions is defaulted to **true**.

## 2.0.0 : 2022-04-05

### New

* Refactored to **use lazy configuration**.
* **Display the script** that was ran when the **simple_choco.noScriptDeletion was set to true**.
* The **source compatibility** is now **Java 11**.

### Update

* Stabilized the installation of auto installation chocolatey application.

## 1.1.0 : 2021-06-13

### New

* Ability to create temporary script to execute multiple choco commands in one task.
* Ability to detect if the task is already running in administration mode.

## 1.0.0 : 2021-05-29

### Initial Version

