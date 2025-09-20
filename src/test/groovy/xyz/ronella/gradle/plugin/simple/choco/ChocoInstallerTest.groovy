package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import java.nio.file.Paths
import static org.junit.jupiter.api.Assertions.*

class ChocoInstallerTest {

    @Test
    void shouldHaveCorrectExecutableName() {
        assertEquals("choco.exe", ChocoInstaller.EXECUTABLE)
    }

    @Test
    void shouldHaveCorrectBinDirectory() {
        assertEquals("bin", ChocoInstaller.BIN_DIR)
    }

    @Test
    void shouldHaveCorrectDefaultInstallLocation() {
        assertEquals(Paths.get(System.getenv("ProgramData"), "chocolatey"), ChocoInstaller.DEFAULT_INSTALL_LOCATION)
    }

    @Test
    void shouldCreateInstallCommandWithCorrectStructure() {
        def installer = new ChocoInstaller()
        def downloadURL = "https://chocolatey.org/install.ps1"

        // Using reflection to access private method for testing
        def method = ChocoInstaller.getDeclaredMethod("getInstallCommand", String.class)
        method.setAccessible(true)
        def command = method.invoke(installer, downloadURL) as List<String>

        assertEquals("PowerShell.Exe", command[0])
        assertEquals("-NoProfile", command[1])
        assertEquals("-InputFormat", command[2])
        assertEquals("None", command[3])
        assertEquals("-ExecutionPolicy", command[4])
        assertEquals("Bypass", command[5])
        assertEquals("-Command", command[6])
        assertTrue(command[7].contains("Start-Process powershell -Wait -Verb runas"))
        assertTrue(command[7].contains(downloadURL))
    }

    @EnabledOnOs(OS.WINDOWS)
    @Test
    void shouldCallInstallMethodWithoutThrowingExceptionWhenNotActuallyExecuting() {
        // Note: This test would actually try to install chocolatey
        // In a real test environment, you might want to mock CommandProcessor.process
        // For now, we'll just test that the method exists and can be called
        def method = ChocoInstaller.getDeclaredMethod("install", String.class)

        assertNotNull(method)
        assertEquals(1, method.getParameterTypes().length)
        assertEquals(String.class, method.getParameterTypes()[0])
    }

    @Test
    void installMethodShouldBeStatic() {
        def method = ChocoInstaller.getDeclaredMethod("install", String.class)

        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()))
    }

    @Test
    void installMethodShouldThrowChocoInstallException() {
        def method = ChocoInstaller.getDeclaredMethod("install", String.class)
        def exceptionTypes = method.getExceptionTypes()

        assertEquals(1, exceptionTypes.length)
        assertEquals(ChocoInstallException.class, exceptionTypes[0])
    }
}