package xyz.ronella.gradle.plugin.simple.choco

import org.junit.jupiter.api.Test
import java.util.regex.Pattern

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull

/**
 * Test to verify that version numbers are consistent across project files.
 */
class VersionMatchTest {

    @Test
    void versionsMatch() {
        def gradlePropertiesVersion = getVersionFromGradleProperties()
        def changelogVersion = getVersionFromChangelog()
        def readmeVersion = getVersionFromReadme()

        assertNotNull(gradlePropertiesVersion, "Version not found in gradle.properties")
        assertNotNull(changelogVersion, "Version not found in CHANGELOG.md")
        assertNotNull(readmeVersion, "Version not found in README.md")

        assertEquals(gradlePropertiesVersion, changelogVersion, 
            "Version in gradle.properties (${gradlePropertiesVersion}) does not match CHANGELOG.md (${changelogVersion})")
        assertEquals(gradlePropertiesVersion, readmeVersion, 
            "Version in gradle.properties (${gradlePropertiesVersion}) does not match README.md (${readmeVersion})")
    }

    private String getVersionFromGradleProperties() {
        def file = new File("gradle.properties")
        def lines = file.readLines()
        def versionLine = lines.find { it.startsWith("version=") }
        def version = versionLine?.substring("version=".length())?.trim()
        // Remove -SNAPSHOT suffix if present
        return version?.replaceAll("-SNAPSHOT\$", "")
    }

    private String getVersionFromChangelog() {
        def file = new File("CHANGELOG.md")
        def lines = file.readLines()
        if (lines.size() >= 3) {
            def versionLine = lines[2] // Line 3 (0-based index 2)
            def pattern = Pattern.compile("## (\\d+\\.\\d+\\.\\d+)")
            def matcher = pattern.matcher(versionLine)
            if (matcher.find()) {
                return matcher.group(1)
            }
        }
        return null
    }

    private String getVersionFromReadme() {
        def file = new File("README.md")
        def lines = file.readLines()
        if (lines.size() >= 16) {
            def versionLine = lines[15] // Line 16 (0-based index 15)
            def pattern = Pattern.compile("version \"(\\d+\\.\\d+\\.\\d+)\"")
            def matcher = pattern.matcher(versionLine)
            if (matcher.find()) {
                return matcher.group(1)
            }
        }
        return null
    }
}