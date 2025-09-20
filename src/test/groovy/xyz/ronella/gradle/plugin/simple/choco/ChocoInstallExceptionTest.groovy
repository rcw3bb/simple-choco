package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class ChocoInstallExceptionTest {

    @Test
    void shouldCreateChocoInstallExceptionWithDefaultConstructor() {
        def exception = new ChocoInstallException()

        assertTrue(exception instanceof ChocoInstallException)
        assertTrue(exception instanceof ChocoException)
        assertNull(exception.message)
    }

    @Test
    void shouldHaveCorrectSerialVersionUID() {
        assertEquals(964486709520991944L, ChocoInstallException.serialVersionUID)
    }
}