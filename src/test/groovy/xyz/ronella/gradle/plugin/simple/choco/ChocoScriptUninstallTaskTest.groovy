package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoScriptUninstallTaskTest {

    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        def chocoTask = project.tasks.chocoScriptUninstall
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("uninstall"))
    }

    @Test
    public void defaultInstallArguments() {
        project.extensions.simple_choco.defaultUninstallArgs += ['-y']
        def chocoTask = project.tasks.chocoScriptUninstall
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("uninstall") && command.contains("-y"))
    }

    @Test
    public void multiplePackages() {
        project.extensions.simple_choco.defaultUninstallArgs += ['-y']
        def chocoTask = project.tasks.chocoScriptUninstall
        chocoTask.packages = [["git"], ["notepadplusplus"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("uninstall git -y") && "uninstall notepadplusplus -y")
    }

}
