package xyz.ronella.gradle.plugin.simple.choco

class SimpleChocoPluginExtension {

    public boolean verbose = false

    public boolean isAutoInstall = true;

    public File chocoHome;

    public boolean isNoop;

    public void writeln(String text) {
        if (verbose) {
            println(text)
        }
    }

}
