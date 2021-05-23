package xyz.ronella.gradle.plugin.simple.choco

import static org.junit.jupiter.api.Assertions.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SimpleChocoPluginTest {
    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.verbose = true
    }

    @Test
    public void testProject() {
        project.tasks.chocoTask.executeCommand()
        assertTrue(project.extensions.simple_choco.verbose)
    }
}