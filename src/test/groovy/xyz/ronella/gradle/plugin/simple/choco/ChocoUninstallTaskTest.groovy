package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoUninstallTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packages = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    void defaultUninstallArguments() {
        project.extensions.simple_choco.defaultUninstallArgs = ['-y']
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packages = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    void multiplePackages() {
        project.extensions.simple_choco.defaultUninstallArgs = ['-y']
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packages = ["git", "notepadplusplus"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"notepadplusplus\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }
}
