package exporter;

import java.util.concurrent.Callable;
import picocli.CommandLine;

/**
 * Hello world!
 *
 */

@CommandLine.Command
public class CLI {
    public static void main(String[] args) {
        System.exit(new CommandLine(new Exporter()).execute(args));
    }
}

@CommandLine.Command(name = "exporter", mixinStandardHelpOptions = true, version = "RMXP Exporter 2.1.1", description = "Prints the checksum (MD5 by default) of a file to STDOUT.", subcommands = {
        CommandLine.HelpCommand.class })
class Exporter implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.printf("[hello] Hello Fucking World!");
        return 0;
    }
}
