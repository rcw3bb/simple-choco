package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.nio.file.Paths

import static org.junit.jupiter.api.Assertions.*

class ChocoTaskTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void noParameters() {
        //chocolatey must be installed before running the tests.
        def chocoTask = project.tasks.chocoTask
        String command = chocoTask.executeCommand()
        assertTrue(command.endsWith(ChocoInstaller.EXECUTABLE))
    }

    @Test
    void chocoHomeParameter() {
        File chocoHome = Paths.get(".", "src", "test", "resources", "ChocoHome").toFile()
        project.extensions.simple_choco.chocoHome=chocoHome
        String expectation = Paths.get(chocoHome.absolutePath, ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE)
        def chocoTask = project.tasks.chocoTask
        String command = chocoTask.executeCommand()
        assertEquals(expectation, command)
    }

    @Test
    void autoInstallDefault() {
        assertTrue(project.extensions.simple_choco.isAutoInstall.get())
    }

    @Test
    void autoChocoDownloadURLDefault() {
        assertEquals(project.extensions.simple_choco.chocoDownloadURL.get(), "https://chocolatey.org/install.ps1")
    }

}
