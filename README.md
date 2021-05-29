# Simple Choco Gradle Plugin

The plugin that allows you access to chocolatey commands inside gradle as task.

# Pre-requisite

* Java 8 (Minimum)
* Windows 

## Plugging in the simple-choco

In your **build.gradle** file add the following plugin:

```groovy
plugins {
    id "xyz.ronella.simple-choco" version "1.0.0"
}
```

> A **Simple Chocolatey tasks** group will be added to the available tasks at your disposal. You can use the following command to see them:
>
> ```
> gradlew tasks --group "Simple Chocolatey"
> ```
>
> Expect to see the available tasks like the following:
>
> ```
> Simple Chocolatey tasks
> -----------------------
> chocoAddSource - Adds a source to where chocolatey search for a package.
> chocoAdminTask - Executes any valid chocolatey commands in administration mode.
> chocoInstall - Installs packages from the chocolatey sources.
> chocoListInstalled - Lists locally installed packages by chocolatey.
> chocoRemoveSource - Removes a source to where chocolatey search for a package.
> chocoSourceList - Displays the sources that the chocolatey is using.
> chocoTask - Executes any valid chocolatey commands.
> chocoUninstall - Uninstalls some packages installed by chocolatey.
> chocoUpgrade - Upgrades some installed chocolatey packages
> chocoUpgradeChoco - Upgrades the current chocolatey.
> chocoVersion - Displays the chocolatey version.
> ```

## CHOCOLATEY_HOME Environment Variable

The first location that the plugin will try to look for the **chocolatey executable** will be the location set by **CHOCOLATEY_HOME** environment variable. If the plugin cannot detect the location of the installed **chocolatey application**, it is advisable to set this variable to the correct directory where the choco executable lives.

## Plugin Properties

| Property | Description | Type | Default |
|-----|------|------|-----|
| simple_choco.chocoHome | Indicates the chocolatey installation directory when not automatically detected and **CHOCOLATEY_HOME was not set**. | File | null |
| simple_choco.defaultInstallArgs | Holds the arguments that will always be present on any **chocoInstall** tasks. | String[] | empty |
| simple_choco.defaultUninstallArgs | Holds the arguments that will always be present on any **chocoUninstall** tasks. | String[] | empty |
| simple_choco.defaultUpgradeArgs | Holds the arguments that will always be present on any **chocoUpgrade** tasks. | String[] | empty |
| simple_choco.isAutoInstall | On the first use of any choco tasks and the choco executable was not found. The plugin will try to **install the chocolatey application**. If this process failed, try to install the chocolatey application manually and set the CHOCOLATEY_HOME environment variable accordingly. | boolean | true |

## General Syntax

```
<CHOCO_EXECUTABLE> <CHOCO_COMMAND> <CHOCO_COMMAND_ARGS> <CHOCO_COMMAND_ZARGS>
```

| Token | Description | Task Property | Type |
|------|------|------|------|
| CHOCO_EXECUTABLE | The choco executable. |  | |
| CHOCO_COMMAND | The choco command to be executed *(e.g. install, update, uninstall, et all)*. This is optional, since not everything has command *(e.g. --version)*. | command | String |
| CHOCO_COMMAND_ARGS | The arguments for the choco command. | args | String[] |
| CHOCO_COMMAND_ZARGS | The arguments that will always be after the CHOCO_COMMAND_ARGS. | zargs | String[] |

> All these task properties *(i.e. command, args, zargs)* are always available to all the tasks *(i.e. including the convience tasks)*.

#### Example

```
choco install notepadplusplus -y
```

| Token               | Value          |
| ------------------- | -------------- |
| CHOCO_EXECUTABLE    | choco          |
| CHOCO_COMMAND       | install        |
| CHOCO_COMMAND_ARGS  | notepadpluplus |
| CHOCO_COMMAND_ZARGS | -y             |

## Using chocoTask

All the member tasks of **Simple Chocolatey** group is a child for **chocoTask**. The **child task** normally just have a default command and/or arguments *(e.g. **chocoVersion** task has **--version as the argument**)*. 

Whatever you can do with the **choco command** in console you can do it in gradle with this task. 

| Task Name | Task Property | Type     |
| --------- | ------------- | -------- |
| chocoTask | command       | String   |
|           | args          | String[] |
|           | zargs         | String[] |

#### Example

Translate the following **choco install command** into a task in gradle:

```
choco install git /NoAutoCrlf /WindowsTerminal /SChannel -y
```

**Use the task itself using the following:**

```groovy
chocoTask {
  command = 'install'
  args = ['git', '/NoAutoCrlf', '/WindowsTerminal', '/SChannel', '-y'] 
}
```

> If you are *running gradle in administration mode* this will work. However if not use the chocoAdminTask as follows:
>
> ```
> chocoAdminTask {
>   command = 'install'
>   args = ['git', '/NoAutoCrlf', '/WindowsTerminal', '/SChannel', '-y'] 
> }
> ```

**Using the child task chocoInstall with the following:**

```groovy
chocoInstall {
  packages = ['git', '/NoAutoCrlf', '/WindowsTerminal', '/SChannel', '-y']
}
```

> You don't need to set the **command property** because it was already preset with **install**.

**Create your own task of type ChocoTask like the following:**

```groovy
task installNotepadPlusPlus(type: ChocoTask) {
  command = 'install'
  args = ['notepadplusplus']
}
```

> To use **ChocoTask class** as the type of your task, you must add the following at the top of your **build.gradle** file:
>
> ```groovy
> import xyz.ronella.gradle.plugin.simple.choco.task.*
> ```
>
> Note: Each **default simple chocolatey tasks** has equivalent class file. The class file has the prefix **Choco** instead of **choco** of the normal gradle task *(e.g. **chocoInstall** gradle task has an equivalent class of **ChocoInstallTask**)*. Notice, that the class equivalent has the suffix **Task**.

**Create your own task of type ChocoInstallTask for convenience like the following:**

``` groovy
task installSoftwares(type: ChocoInstallTask ) {
  packages = ['notepadplusplus', 'lzip']
}
```

> You don't need to set the **command property** because it was already preset with **install**.

## Sample build.gradle File

``` groovy
plugins {
  id "xyz.ronella.simple-choco" version "1.0.0"
}

simple_choco.defaultInstallArgs=['-y']

task installSoftwares(type: ChocoInstallTask ) {
  packages = ['notepadplusplus', 'lzip']
}
```
## Convenience Tasks and Their Task Properties

| Task Name       | Task Property | Task Type | Admin  Mode | Description |
| --------------- | ------------- | ------- | ------- | ------- |
| chocoAddSource | sourceName |String  | Yes         |The task for adding a chocolatey source repository.  |
|  | url |String | | |
| chocoAdminTask | args |String[] | Yes | The task for executing a choco commands in administration mode. |
| | command |String | | |
| | zargs |String[] | | |
| chocoInstall | packages |String[]  |Yes  |The task for installing packages.  |
| chocoListInstalled |  |||The task for listing install packages with choco.|
| chocoRemoveSource | sourceName |String  |Yes  |The task for removing a chocolatey source repository.  |
| chocoSourceList |     |  |  | The task for displaying the sources the chocolatey is using. |
| chocoTask | args |String[]  |  |The task for executing a choco commands.  |
|  | command | String    |     |     |
|  | zargs |String[] | | |
| chocoUninstall | packages |String[]  |Yes  |The task for uninstalling packages  |
| chocoUpgradeChoco |          |  | Yes | The task for upgrading the chocolatey application. |
| chocoUpgrade | packages  | String[] | Yes | The task for upgrading packages. |
| chocoVersion |  |  |  | The task for displaying the chocolatey version. |

> All tasks in **admin mode** will be run in **administration** to do its job. Expect to see windows dialog the allow it to make changes.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb