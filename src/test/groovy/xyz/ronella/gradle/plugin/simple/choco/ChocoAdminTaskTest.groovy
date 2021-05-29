package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.nio.file.Paths

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoAdminTaskTest {

    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        //chocolatey must be installed before running the tests.
        def chocoTask = project.tasks.chocoAdminTask
        String command = chocoTask.executeCommand()
        String endsWith = "-Wait -Verb runas -argumentlist \"\"\"\"\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    public void chocoHomeParameter() {
        File chocoHome = Paths.get(".", "src", "test", "resources", "ChocoHome").toFile()
        project.extensions.simple_choco.chocoHome=chocoHome
        String expectation = Paths.get(chocoHome.absolutePath, ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE)
        def chocoTask = project.tasks.chocoAdminTask
        String command = chocoTask.executeCommand()
        String endsWith = "-Verb runas -argumentlist \"\"\"\"\"\"\"\"\""
        assertTrue(command.endsWith(endsWith))
    }

    @Test
    public void autoInstallDefault() {
        assertTrue(project.extensions.simple_choco.isAutoInstall)
    }

    @Test
    public void noChocoExecutable() {
        File chocoHome = Paths.get(".", "src", "test", "resources", "Choco").toFile()
        project.extensions.simple_choco.chocoHome=chocoHome
        String expectation = Paths.get(chocoHome.absolutePath, ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE)
        def chocoTask = project.tasks.chocoAdminTask
        String command = chocoTask.executeCommand()
        assertTrue(command.isEmpty())
    }
}
