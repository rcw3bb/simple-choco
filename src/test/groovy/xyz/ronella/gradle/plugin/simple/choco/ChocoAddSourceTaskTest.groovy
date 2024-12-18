package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAddSourceTask

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoAddSourceTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void withRequiredParameters() {
        ChocoAddSourceTask chocoTask = project.tasks.chocoAddSource
        chocoTask.sourceName = "mysource"
        chocoTask.url = "http://www.mylocal.source"
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"source\"\"\"\",\"\"\"\"add\"\"\"\",\"\"\"\"-s\"\"\"\",\"\"\"\"http://www.mylocal.source\"\"\"\",\"\"\"\"-n=mysource\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    void withPriority() {
        ChocoAddSourceTask chocoTask = project.tasks.chocoAddSource
        chocoTask.sourceName = "mysource"
        chocoTask.url = "http://www.mylocal.source"
        chocoTask.priority = 1
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"source\"\"\"\",\"\"\"\"add\"\"\"\",\"\"\"\"-s\"\"\"\",\"\"\"\"http://www.mylocal.source\"\"\"\",\"\"\"\"--priority\"\"\"\",\"\"\"\"1\"\"\"\",\"\"\"\"-n=mysource\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

}
