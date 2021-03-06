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
    void noParameters() {
        ChocoAddSourceTask chocoTask = project.tasks.chocoAddSource
        chocoTask.sourceName = "mysource"
        chocoTask.url = "http://www.mylocal.source"
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"source\"\"\"\",\"\"\"\"add\"\"\"\",\"\"\"\"-s\"\"\"\",\"\"\"\"http://www.mylocal.source\"\"\"\",\"\"\"\"-n=mysource\"\"\"\"\""
        println "Command: ${command}"
        println "EndsWith: ${endsWith}"
        assertTrue(command.endsWith(endsWith))
    }

}
