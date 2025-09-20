package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoExecutorHelper
import xyz.ronella.gradle.plugin.simple.choco.task.ChocoTask

import java.nio.file.Paths

import static org.junit.jupiter.api.Assertions.*

class ChocoExecutorHelperTest {

    private Project project

    @BeforeEach
    void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    void createExecutorBasicConfiguration() {
        def chocoTask = project.tasks.chocoTask
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        // Execute to get the command string for verification
        String command = executor.execute()
        assertTrue(command.endsWith(ChocoInstaller.EXECUTABLE))
    }

    @Test
    void createExecutorWithChocoHome() {
        File chocoHome = Paths.get(".", "src", "test", "resources", "ChocoHome").toFile()
        project.extensions.simple_choco.chocoHome = chocoHome
        
        def chocoTask = project.tasks.chocoTask
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        String command = executor.execute()
        String expectedPath = Paths.get(chocoHome.absolutePath, ChocoInstaller.BIN_DIR, ChocoInstaller.EXECUTABLE).toString()
        assertEquals(expectedPath, command)
    }

    @Test
    void createExecutorWithCommandAndArgs() {
        def chocoTask = project.tasks.chocoTask
        chocoTask.command.set("install")
        chocoTask.args.set(["git", "-y"])
        
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        String command = executor.execute()
        assertTrue(command.contains("install"))
        assertTrue(command.contains("git"))
        assertTrue(command.contains("-y"))
    }

    @Test
    void createExecutorWithAdminMode() {
        def chocoTask = project.tasks.chocoAdminTask
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        String command = executor.execute()
        // Admin mode tasks should include PowerShell elevation syntax on Windows
        assertTrue(command.contains("-Verb runas") || command.endsWith(ChocoInstaller.EXECUTABLE))
    }

    @Test
    void createExecutorWithExtensionProperties() {
        project.extensions.simple_choco.isAutoInstall = false
        project.extensions.simple_choco.forceAdminMode = false
        project.extensions.simple_choco.noScriptDeletion = true
        
        def chocoTask = project.tasks.chocoTask
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        // Just verify the executor can be created and executed without errors
        String command = executor.execute()
        assertNotNull(command)
    }

    @Test
    void createExecutorWithZArgs() {
        def chocoTask = project.tasks.chocoTask
        chocoTask.command.set("install")
        chocoTask.getZArgs().set(["--confirm"])
        
        def executor = ChocoExecutorHelper.createExecutor(chocoTask)
        
        assertNotNull(executor)
        
        String command = executor.execute()
        assertTrue(command.contains("--confirm"))
    }
}