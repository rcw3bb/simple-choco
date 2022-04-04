package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoScriptUpgradeTaskTest {
    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        def chocoTask = project.tasks.chocoScriptUpgrade
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("upgrade"))
    }

    @Test
    void defaultInstallArguments() {
        project.extensions.simple_choco.defaultUpgradeArgs = ['-y']
        def chocoTask = project.tasks.chocoScriptUpgrade
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("upgrade") && command.contains("-y"))
    }

    @Test
    void multiplePackages() {
        project.extensions.simple_choco.defaultUpgradeArgs = ['-y']
        def chocoTask = project.tasks.chocoScriptUpgrade
        chocoTask.packages = [["git"], ["notepadplusplus"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("upgrade git -y") && "upgrade notepadplusplus -y")
    }

}
