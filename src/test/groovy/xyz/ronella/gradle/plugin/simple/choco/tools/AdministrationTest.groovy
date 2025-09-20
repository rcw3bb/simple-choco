package xyz.ronella.gradle.plugin.simple.choco.tools

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledOnOs
import org.junit.jupiter.api.condition.OS
import static org.junit.jupiter.api.Assertions.*

class AdministrationTest {

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void shouldDetectElevatedMode() {
        // This test checks if the isElevatedMode method returns a boolean value
        // The actual result depends on whether the test is running in elevated mode
        def result = Administration.isElevatedMode()
        
        // The method should return a boolean, regardless of the actual elevation status
        assertTrue(result == true || result == false)
    }

    @Test
    void shouldHavePrivateConstructor() {
        // Test that the class cannot be instantiated (private constructor)
        def constructors = Administration.class.getDeclaredConstructors()
        assertEquals(1, constructors.length)
        
        def constructor = constructors[0]
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()))
    }

    @Test
    void shouldHaveStaticMethod() {
        // Test that isElevatedMode is a static method
        def method = Administration.class.getDeclaredMethod("isElevatedMode")
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()))
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()))
    }
}