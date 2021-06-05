package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptTask

import static org.junit.jupiter.api.Assertions.*

class ChocoScriptTaskTest {
    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        ChocoScriptTask chocoTask = project.tasks.chocoScript
        String command = chocoTask.executeCommand()
        assertNull(command)
    }

    @Test
    public void commandNoPackages() {
        ChocoScriptTask chocoTask = project.tasks.chocoScript
        chocoTask.command = "install"
        String command = chocoTask.executeCommand()
        assertNull(command)
    }

    @Test
    public void withCommandAndAPackage() {
        ChocoScriptTask chocoTask = project.tasks.chocoScript
        chocoTask.command = "install"
        chocoTask.packages = [["notepadplusplus"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("powershell") && !command.contains("runas"))
    }

}
