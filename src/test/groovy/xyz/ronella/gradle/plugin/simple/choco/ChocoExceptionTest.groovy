package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class ChocoExceptionTest {

    @Test
    void shouldCreateChocoExceptionWithDefaultConstructor() {
        def exception = new ChocoException()

        assertTrue(exception instanceof ChocoException)
        assertNull(exception.message)
    }

    @Test
    void shouldCreateChocoExceptionWithMessage() {
        def message = "Test error message"
        def exception = new ChocoException(message)

        assertTrue(exception instanceof ChocoException)
        assertEquals(message, exception.message)
    }

    @Test
    void shouldExtendGradleException() {
        def exception = new ChocoException("test")

        assertTrue(exception instanceof org.gradle.api.GradleException)
    }
}