package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoUpgradeTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        def chocoTask = project.tasks.chocoUpgrade
        chocoTask.packages = ["chocolatey"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"upgrade\"\"\"\",\"\"\"\"chocolatey\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    void defaultUpgradeArguments() {
        project.extensions.simple_choco.defaultUpgradeArgs = ['-y']
        def chocoTask = project.tasks.chocoUpgrade
        chocoTask.packages = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"upgrade\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    void multiplePackages() {
        project.extensions.simple_choco.defaultUpgradeArgs = ['-y']
        def chocoTask = project.tasks.chocoUpgrade
        chocoTask.packages = ["git", "notepadplusplus"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"upgrade\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"notepadplusplus\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }
}
