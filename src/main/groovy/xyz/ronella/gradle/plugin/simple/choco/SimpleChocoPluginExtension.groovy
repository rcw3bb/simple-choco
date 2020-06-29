package xyz.ronella.gradle.plugin.simple.choco

class SimpleChocoPluginExtension {

    public boolean verbose = false

    public void writeln(String text) {
        if (verbose) {
            println(text)
        }
    }

}
