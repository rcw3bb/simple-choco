package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoInstallTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        def chocoTask = project.tasks.chocoInstall
        chocoTask.packages = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"install\"\"\"\",\"\"\"\"git\"\"\"\"\""
        assertTrue(command.contains(endsWith))
    }

    @Test
    void defaultInstallArguments() {
        project.extensions.simple_choco.defaultInstallArgs = ['-y']
        def chocoTask = project.tasks.chocoInstall
        chocoTask.packages = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"install\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.contains(endsWith))
    }

    @Test
    void multiplePackages() {
        project.extensions.simple_choco.defaultInstallArgs = ['-y']
        def chocoTask = project.tasks.chocoInstall
        chocoTask.packages = ["git", "notepadplusplus"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"install\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"notepadplusplus\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.contains(endsWith))
    }
}
