package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*

class ChocoScriptExceptionTest {

    @Test
    void shouldCreateChocoScriptExceptionWithMessage() {
        def message = "Script error message"
        def exception = new ChocoScriptException(message)

        assertTrue(exception instanceof ChocoScriptException)
        assertTrue(exception instanceof ChocoException)
        assertEquals(message, exception.message)
    }

    @Test
    void shouldHaveCorrectSerialVersionUID() {
        assertEquals(-7649551499086850984L, ChocoScriptException.serialVersionUID)
    }
}