package xyz.ronella.gradle.plugin.simple.choco

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertTrue

class ChocoScriptInstallTaskTest {
    private Project project;

    @BeforeEach
    public void initProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'xyz.ronella.simple-choco'
        project.extensions.simple_choco.isNoop = true
    }

    @Test
    public void noParameters() {
        def chocoTask = project.tasks.chocoScriptInstall
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("install"))
    }

    @Test
    public void defaultInstallArguments() {
        project.extensions.simple_choco.defaultInstallArgs += ['-y']
        def chocoTask = project.tasks.chocoScriptInstall
        chocoTask.packages = [["git"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("install") && command.contains("-y"))
    }

    @Test
    public void multiplePackages() {
        project.extensions.simple_choco.defaultInstallArgs += ['-y']
        def chocoTask = project.tasks.chocoScriptInstall
        chocoTask.packages = [["git"], ["notepadplusplus"]]
        String command = chocoTask.executeCommand()
        assertTrue(command.contains("install git -y") && "install notepadplusplus -y")
    }

}
