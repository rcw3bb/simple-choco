# Changelog

## 3.1.0 : 2025-09-21

### New
* Now tested with Gradle 9.

## 3.0.1 : 2024-12-09

### Minor Change

* Make the shadow package part of the plugin package.

## 3.0.0 : 2024-11-17

### New

* Add priority convenience parameter for ChocoAddSourceTask.

### Change

* Both **chocoScript** and **chocoScriptAdmin** tasks are updated to only accept **commands** property. Both the **command** and **packages** properties are not applicable anymore.

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

