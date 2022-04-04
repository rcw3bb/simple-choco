package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptTask

import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoScriptAdminTaskTest {
    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        String command = chocoTask.executeCommand()
        assertNull(command)
    }

    @Test
    void commandNoPackages() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        chocoTask.command = "install"
        String command = chocoTask.executeCommand()
        assertNull(command)
    }

    @Test
    void withCommandAndAPackage() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        chocoTask.command = "install"
        chocoTask.packages = [["notepadplusplus"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("powershell") && command.contains("runas"))
    }
}
