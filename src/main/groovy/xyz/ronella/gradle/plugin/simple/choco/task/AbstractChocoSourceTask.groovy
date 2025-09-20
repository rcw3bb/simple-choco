package xyz.ronella.gradle.plugin.simple.choco.task

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import xyz.ronella.gradle.plugin.simple.choco.ChocoExecutor
import xyz.ronella.trivial.handy.RegExMatcher

import javax.inject.Inject
import java.util.stream.Collectors

/**
 * The base class for choco source tasks.
 *
 * @author Ron Webb
 * @since v2.1.0
 */
abstract class AbstractChocoSourceTask extends ChocoAdminTask {
    @Input
    abstract Property<String> getSourceName()

    @Inject
    AbstractChocoSourceTask(ObjectFactory objects) {
        super(objects)
        internalCommand.convention('source')
    }

    protected def loadedSourceNames() {
        def extension = getExtension().get()
        ChocoExecutor executor = ChocoExecutor.getBuilder()
                .addAutoInstall(extension.isAutoInstall.get())
                .addNoop(extension.isNoop.get())
                .addChocoHome(extension.chocoHome.getOrNull())
                .addCommand('source')
                .addArgs('list')
                .addLogging(hasLogging.get())
                .dontShowCommand()
                .build()

        def names = List.<String>of()

        executor.executeSingleCommand { ___output, ___error ->
            BufferedReader output = new BufferedReader(new InputStreamReader(___output))
            String outputStr = output.lines().collect(Collectors.joining("\n"))

            var srcLines = Optional.<String>ofNullable(outputStr).orElse("").split("\n")
            var excludedLine1 = Arrays.stream(srcLines).toList().subList(1, srcLines.length)

            names = excludedLine1.stream()
                    .map(___line -> RegExMatcher.find("(.*?)\\s-.*", ___line)
                            .group(1)).toList()
        }

        return names
    }

    @Override
    String executeCommand() {
        internalArgs.add(String.format("-n=%s", sourceName.get()))
        ChocoExecutor executor = ChocoExecutorHelper.createExecutor(this)
        return executor.execute()
    }
}
