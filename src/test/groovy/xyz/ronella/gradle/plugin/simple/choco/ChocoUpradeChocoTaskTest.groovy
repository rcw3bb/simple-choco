package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoUpradeChocoTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    void noParameters() {
        def chocoTask = project.tasks.chocoUpgradeChoco
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"upgrade\"\"\"\",\"\"\"\"chocolatey\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

}
