# Simple Choco Gradle Plugin - AI Coding Instructions

## Project Overview

This is a **Gradle plugin for Windows** that wraps Chocolatey package manager commands into convenient Gradle tasks. The plugin enables automation of software installation, upgrades, and management through Gradle build scripts.

### Architecture

- **Language**: Mixed Java/Groovy codebase
  - **Java**: Core execution logic, exceptions, and utility classes (`src/main/java`)
  - **Groovy**: Gradle tasks, plugin implementation, and DSL (`src/main/groovy`)
- **Pattern**: Task-based architecture with base classes and specialized implementations
- **Target Platform**: Windows only (requires Chocolatey)

## Key Components

### Core Classes
- `SimpleChocoPlugin`: Main plugin entry point that registers all tasks
- `ChocoExecutor`: Central command execution engine (Java)
- `ChocoTask`: Base task class that all other tasks extend (Groovy)
- `SimpleChocoPluginExtension`: Plugin configuration DSL (`simple_choco` extension)

### Task Hierarchy
```
ChocoTask (base)
├── ChocoAdminTask (requires admin mode)
├── ChocoInstallTask (convenience for install)
├── ChocoScriptTask (generates temp scripts)
└── [15+ other specialized tasks]
```

## Development Patterns

### Task Creation Pattern
All tasks follow this pattern:
```groovy
class NewChocoTask extends ChocoTask {
    // Constructor sets command and description
    // Override properties for task-specific behavior
}
```

### Configuration Extension
Plugin uses `simple_choco` extension block:
```groovy
simple_choco {
    chocoHome = file('/path/to/choco')
    defaultInstallArgs = ['-y']
    isAutoInstall = true
}
```

### Testing Strategy
- **Groovy tests**: Use `ProjectBuilder` to create test projects
- **Set `isNoop = true`** to prevent actual choco execution in tests
- **Assertion pattern**: Check generated command strings contain expected arguments
- **Test naming**: Match task class names (e.g., `ChocoInstallTaskTest.groovy`)

## Build System

### Gradle Plugin Publishing
- Uses `com.gradle.plugin-publish` plugin
- Shadow JAR configuration relocates dependencies to avoid conflicts
- Plugin ID: `xyz.ronella.simple-choco`
- Implementation class: `xyz.ronella.gradle.plugin.simple.choco.SimpleChocoPlugin`

### Dependencies
- **trivial-chunk**: Utility library (relocated in shadow JAR)
- **Java 21**: Required minimum version
- **PMD**: Static analysis with custom rules in `config/pmd/custom.xml`
- **JaCoCo**: Code coverage reporting

### Key Gradle Commands
```bash
./gradlew test              # Run tests with PMD and coverage
./gradlew publishPlugins    # Publish to Gradle Plugin Portal
./gradlew build            # Full build with shadow JAR
```

## Windows-Specific Considerations

### Administration Mode
- Plugin auto-detects if running in admin mode
- Tasks like `chocoInstall` require elevated privileges
- Uses PowerShell `Start-Process -Verb runas` for elevation
- Configuration: `forceAdminMode` property

### Chocolatey Detection
1. Check `CHOCOLATEY_HOME` environment variable
2. Check `simple_choco.chocoHome` property  
3. Auto-install if `isAutoInstall = true`

## File Locations

### Source Structure
- `src/main/groovy/`: All Gradle tasks and plugin logic
- `src/main/java/`: Core execution engine and exceptions
- `src/test/groovy/`: Task tests using ProjectBuilder
- `build/resources/main/META-INF/gradle-plugins/`: Plugin descriptor

### Configuration Files
- `gradle/libs.versions.toml`: Version catalog for dependencies
- `config/pmd/custom.xml`: PMD rules customization
- `gradle.properties`: Plugin version and group ID

## When Contributing

1. **New tasks**: Extend `ChocoTask` and follow naming convention (`Choco[Command]Task`)
2. **Tests**: Create corresponding test class with `isNoop = true`
3. **Properties**: Add to `SimpleChocoPluginExtension` for plugin configuration
4. **Documentation**: Update README.md task table and examples
5. **Java code**: Place core logic in Java, Gradle DSL in Groovy