package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import xyz.ronella.trivial.handy.OSType
import java.io.InputStream
import java.util.function.BiConsumer
import static org.junit.jupiter.api.Assertions.*

class ChocoExecutorTest {

    @Test
    void shouldCreateChocoExecutorThroughBuilder() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addNoop(true)
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldBuildExecutorWithAllBuilderOptions() {
        def chocoHome = new File("c:\\test\\choco")
        def commands = [[["install", "git"]]]

        def executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.WINDOWS)
                .addAutoInstall(true)
                .addChocoHome(chocoHome)
                .addCommand("install")
                .addArgs("git", "-y")
                .addZArgs("--force")
                .addNoop(true)
                .addAdminMode(true)
                .addLogging(true)
                .addRunningOnAdmin(false)
                .addForceAdminMode(true)
                .addScriptMode(true)
                .addTaskName("testTask")
                .addCommands(commands)
                .addNoScriptDeletion(true)
                .dontShowCommand()
                .addChocoDownloadURL("https://chocolatey.org/install.ps1")
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldExecuteCommandInNoopMode() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addNoop(true)
                .build()

        def result = executor.execute()

        assertNotNull(result)
        // In noop mode, it should return the command that would be executed
    }

    @Test
    void shouldExecuteSingleCommandWithCustomLogicInNoopMode() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addNoop(true)
                .build()
        
        BiConsumer<InputStream, InputStream> logic = { output, error ->
            // Custom logic for handling streams
        }

        def result = executor.executeSingleCommand(logic)

        assertNotNull(result)
        assertTrue(result.length() > 0)
    }

    @Test
    void shouldHandleArgsAndZArgsInBuilder() {
        def builder = ChocoExecutor.getBuilder()
                .addArgs()  // Empty args
                .addZArgs() // Empty zArgs
                .addNoop(true)

        assertNotNull(builder)
        
        def executor = builder.build()
        
        assertNotNull(executor)
    }

    @Test
    void shouldHandleScriptMode() {
        def commands = [["install", "git"], ["install", "nodejs"]]
        def commandsList = [commands]

        def executor = ChocoExecutor.getBuilder()
                .addScriptMode(true)
                .addTaskName("testScript")
                .addCommands(commandsList)
                .addNoop(true)
                .build()

        def result = executor.execute()

        assertNotNull(result)
    }

    @Test
    void shouldHandleAdminModeConfiguration() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("install")
                .addArgs("git")
                .addAdminMode(true)
                .addRunningOnAdmin(false)
                .addForceAdminMode(true)
                .addNoop(true)
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldHandleLoggingConfiguration() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addLogging(true)
                .addNoop(true)
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldHandleAutoInstallConfiguration() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addAutoInstall(true)
                .addChocoDownloadURL("https://chocolatey.org/install.ps1")
                .addNoop(true)
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldHandleNoScriptDeletionConfiguration() {
        def commands = [["install", "git"]]
        def commandsList = [commands]

        def executor = ChocoExecutor.getBuilder()
                .addScriptMode(true)
                .addTaskName("testNoDelete")
                .addCommands(commandsList)
                .addNoScriptDeletion(true)
                .addNoop(true)
                .build()

        assertNotNull(executor)
    }

    @Test
    void shouldHandleShowCommandConfiguration() {
        def executor1 = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addNoop(true)
                .build()
        
        def executor2 = ChocoExecutor.getBuilder()
                .addCommand("list")
                .dontShowCommand()
                .addNoop(true)
                .build()

        assertNotNull(executor1)
        assertNotNull(executor2)
    }

    @Test
    void shouldReturnBuilderInstanceForMethodChaining() {
        def builder = ChocoExecutor.getBuilder()

        assertSame(builder, builder.addOSType(OSType.WINDOWS))
        assertSame(builder, builder.addAutoInstall(true))
        assertSame(builder, builder.addChocoHome(new File("test")))
        assertSame(builder, builder.addCommand("test"))
        assertSame(builder, builder.addArgs("arg1", "arg2"))
        assertSame(builder, builder.addZArgs("zarg1"))
        assertSame(builder, builder.addNoop(true))
        assertSame(builder, builder.addAdminMode(true))
        assertSame(builder, builder.addLogging(true))
        assertSame(builder, builder.addRunningOnAdmin(true))
        assertSame(builder, builder.addForceAdminMode(true))
        assertSame(builder, builder.addScriptMode(true))
        assertSame(builder, builder.addTaskName("test"))
        assertSame(builder, builder.addCommands([]))
        assertSame(builder, builder.addNoScriptDeletion(true))
        assertSame(builder, builder.dontShowCommand())
        assertSame(builder, builder.addChocoDownloadURL("url"))
    }

    @Test
    void shouldThrowChocoExecutableExceptionWhenExecutableNotFoundAndNotAutoInstall() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addAutoInstall(false)
                .addOSType(OSType.WINDOWS)
                .addChocoHome(new File("c:\\nonexistent\\choco"))
                .build()

        assertThrows(ChocoExecutableException.class, () -> executor.execute())
    }

    @Test
    void shouldHandleNonWindowsOS() {
        def executor = ChocoExecutor.getBuilder()
                .addCommand("list")
                .addOSType(OSType.LINUX)
                .build()

        assertThrows(ChocoExecutableException.class, () -> executor.execute())
    }
}