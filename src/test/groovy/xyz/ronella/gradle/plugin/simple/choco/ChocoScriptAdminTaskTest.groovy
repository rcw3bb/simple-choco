package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoScriptTask

import static org.junit.jupiter.api.Assertions.assertThrows
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
    void withCommandArg() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        chocoTask.command = "install"
        assertThrows(ChocoScriptException.class, {
            chocoTask.executeCommand()
        })
    }

    @Test
    void withEmptyCommand() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        chocoTask.commands = []
        assertThrows(ChocoScriptException.class, {
            chocoTask.executeCommand()
        })
    }

    @Test
    void withCommands() {
        ChocoScriptTask chocoTask = project.tasks.chocoScriptAdmin
        chocoTask.commands = [
                ["source", "add", "--name", "source1", "--source", "https://test.com"],
                ["source", "add", "--name", "source2", "--source", "https://test.com"]
        ]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("source1") && command.contains("source2"))
    }

}
