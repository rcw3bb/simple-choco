package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoAddSourceTask

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoAddSourceTaskTest {

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
        ChocoAddSourceTask chocoTask = project.tasks.chocoAddSource
        chocoTask.name = "mysource"
        chocoTask.url = "http://www.mylocal.source"
        String command = chocoTask.executeCommand()
        String endsWith = "choco.exe source add -n=\"mysource\" -s \"http://www.mylocal.source\""
        assertTrue(command.endsWith(endsWith))
    }

}
