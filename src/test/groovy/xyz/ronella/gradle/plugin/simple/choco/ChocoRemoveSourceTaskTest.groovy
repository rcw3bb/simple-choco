package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAddSourceTask

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoRemoveSourceTaskTest {

    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        def chocoTask = project.tasks.chocoRemoveSource
        chocoTask.sourceName = "mysource"
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"source\"\"\"\",\"\"\"\"remove\"\"\"\",\"\"\"\"-n=mysource\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

}
