package exporter;

import picocli.CommandLine;

/**
 * Hello world!
 *
 */

@CommandLine.Command
public class App {
    public static void main(String[] args) {
        new CommandLine(new Exporter()).execute(args);
    }
}

@CommandLine.Command(name = "exporter")
class Exporter implements Runnable {

    @Override
    public void run() {
        System.out.println("[hello] Hello Fucking World!");
    }
}
