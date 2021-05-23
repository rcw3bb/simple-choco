package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoSourceListTaskTest {

    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.verbose = true
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        def chocoTask = project.tasks.chocoSourceList
        String command = chocoTask.executeCommand()
        String endsWith = "choco.exe source list"
        assertTrue(command.endsWith(endsWith))
    }
}
