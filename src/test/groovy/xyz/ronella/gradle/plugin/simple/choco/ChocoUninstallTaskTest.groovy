package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoUninstallTaskTest {

    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packageNames = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    public void defaultUninstallArguments() {
        project.extensions.simple_choco.defaultUninstallArgs += ['-y']
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packageNames = ["git"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    public void multiplePackages() {
        project.extensions.simple_choco.defaultUninstallArgs += ['-y']
        def chocoTask = project.tasks.chocoUninstall
        chocoTask.packageNames = ["git", "notepadplusplus"]
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"uninstall\"\"\"\",\"\"\"\"git\"\"\"\",\"\"\"\"notepadplusplus\"\"\"\",\"\"\"\"-y\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }
}
