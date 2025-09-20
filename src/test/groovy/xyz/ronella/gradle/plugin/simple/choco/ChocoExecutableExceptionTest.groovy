package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class ChocoExecutableExceptionTest {

    @Test
    void shouldCreateChocoExecutableExceptionWithDefaultConstructor() {
        def exception = new ChocoExecutableException()

        assertTrue(exception instanceof ChocoExecutableException)
        assertTrue(exception instanceof ChocoException)
        assertEquals("Chocolatey executable not found.", exception.message)
    }

    @Test
    void shouldHaveCorrectSerialVersionUID() {
        assertEquals(1488334312896743160L, ChocoExecutableException.serialVersionUID)
    }
}