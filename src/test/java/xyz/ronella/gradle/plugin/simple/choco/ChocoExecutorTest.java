package xyz.ronella.gradle.plugin.simple.choco;

import org.junit.jupiter.api.Test;
import xyz.ronella.gradle.plugin.simple.choco.tools.OSType;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ChocoExecutorTest {

    @Test
    public void adminMode() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(true)
                .build();

        String command = executor.execute();

        assertTrue(command.startsWith("powershell.exe"));
    }

    @Test
    public void adminModeRunningOnAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(true)
                .addRunningOnAdmin(true)
                .build();

        String command = executor.execute();

        assertFalse(command.startsWith("powershell.exe"));
    }

    @Test
    public void adminModeRunningOnAdminForceAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(true)
                .addRunningOnAdmin(true)
                .addForceAdminMode(true)
                .build();

        String command = executor.execute();

        assertTrue(command.startsWith("powershell.exe"));
    }

    @Test
    public void adminModeForceAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(true)
                .addForceAdminMode(true)
                .build();

        String command = executor.execute();

        assertTrue(command.startsWith("powershell.exe"));
    }

    @Test
    public void forceAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addForceAdminMode(true)
                .build();

        String command = executor.execute();

        assertFalse(command.startsWith("powershell.exe"));
    }

    @Test
    public void runningOnAdminMode() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addRunningOnAdmin(true)
                .build();

        String command = executor.execute();

        assertFalse(command.startsWith("powershell.exe"));
    }

    @Test
    public void scriptModeMultiPackagesNonAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(false)
                .addScriptMode(true)
                .addCommand("install")
                .addTaskName("testTask")
                .addPackages(Arrays.asList(
                        Arrays.asList("git", "/NoAutoCrlf", "/WindowsTerminal", "/SChannel"),
                        Collections.singletonList("notepadplusplus"),
                        Collections.singletonList("lzip")
                        ))
                .build();

        String command = executor.execute();

        System.out.println(command);

        assertFalse(command.contains("runas"));
    }

    @Test
    public void scriptModeMultiPackagesAdmin() {
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addOSType(OSType.Windows)
                .addNoop(true)
                .addAdminMode(true)
                .addScriptMode(true)
                .addCommand("install")
                .addTaskName("testTask")
                .addPackages(Arrays.asList(
                        Arrays.asList("git", "/NoAutoCrlf", "/WindowsTerminal", "/SChannel"),
                        Collections.singletonList("notepadplusplus"),
                        Collections.singletonList("lzip")
                ))
                .build();

        String command = executor.execute();

        assertTrue(command.contains("runas"));
    }

}
