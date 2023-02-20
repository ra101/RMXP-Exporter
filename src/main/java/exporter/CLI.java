package exporter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import picocli.CommandLine;


@CommandLine.Command
public class CLI {

  public static final String EXENAME = "exporter";
  public static final String NAME = "RMXP Exporter";
  public static final String VERSION = "2.1.1";
  public static final String DISPLAY_VER =  NAME + " v" + VERSION;
  public static final String DESC =
    "`exporter` is tool for creating a distributable archive of your RPG Maker XP";

  @CommandLine.Command(
    version = DISPLAY_VER,
    mixinStandardHelpOptions = true,
    sortOptions = false,
    headerHeading = DISPLAY_VER + "\n\n" + DESC + "\n\n",
    synopsisHeading = "Usage:\n\t",
    customSynopsis = "exporter.exe [options] {--encrypt|--archive|--list-files}\n",
    optionListHeading = "Options:\n",
    commandListHeading = "\nCommands:\n"
  )
  static class BaseCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      throw new CommandLine.PicocliException("Not Implemented!");
    }
  }

  static class BaseSubCommand extends BaseCommand {

    @CommandLine.ParentCommand
    protected Exporter exporter;

    @CommandLine.Option(
      names = { "--archive", "-a" },  defaultValue="false", hidden = true
    )
    private Boolean archiveFlag;

    @CommandLine.Option(
      names = { "--encrypt", "-e" }, defaultValue="false", hidden = true
    )
    private Boolean encryptFlag;

    @CommandLine.Option(
      names = { "--list-files", "-l" },  defaultValue="false", hidden = true
    )
    private Boolean listFilesFlag;

    protected Integer singleCmdCheck() throws Exception {
      if (listFilesFlag || archiveFlag || encryptFlag) {
        throw new CommandLine.ParameterException(
          new CommandLine(exporter),
          "Error: Either use `--encrypt`, `--archive` or `--list-files`, Only One!\n"
        );
      }
      return 0;
    }
  }

  public static void main(String[] args) {
    CommandLine asas = new CommandLine(new Exporter());
    asas.registerConverter(Path.class, s -> Paths.get(s));
    System.exit(asas.execute(args));
  }
}

@CommandLine.Command(
  name = CLI.EXENAME,
  subcommands = { EncryptCommand.class, ArchiveCommand.class, ListCommand.class }
)
final class Exporter extends CLI.BaseCommand {

  public String currentPath = System.getProperty("user.dir");

  @CommandLine.Option(
    names = { "--project", "-d" }, paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = "RMXP project path. (default: Current Folder)"
  )
  public Path projectPath;

  @CommandLine.Option(
    names = { "--ignore-file", "-f" }, paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Ignore File path. (default: Project\\.exporterignore)"
  )
  public Path ignoreFile;

  @CommandLine.Option(
    names = { "--output-file", "-o" }, paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = ".rgssad File path (default: Project\\Game.rgssad)"
  )
  public Path outputFile;

  @CommandLine.Option(
    names = { "--output-dir", "-O" }, paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Output Dir for Zip. (default: Project\\Dist\\Game.zip)\n"
  )
  public Path outputFolder;

  @CommandLine.Option(
    names = { "--silent", "-s" }, defaultValue = "false",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Do not output any information while processing."
  )
  public Boolean silentFlag;

  @CommandLine.Option(
    names = { "--clean", "-c" }, defaultValue = "false",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Delete old Output Files.\n"
  )
  public Boolean cleanFlag;

  @Override
  public Integer call() throws Exception {
    throw new CommandLine.ParameterException(
      new CommandLine(this),
      "Error: Either `--encrypt`, `--archive` or `--list-files` must be Specified!\n"
    );
  }
}

@CommandLine.Command(
  name = "--list-files", aliases = { "-l" },
  description = "..."
)
final class ListCommand extends CLI.BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    singleCmdCheck();
    System.out.printf("Hello, %s!%n", exporter.projectPath);
    return 0;
  }
}

@CommandLine.Command(
  name = "--archive", aliases = { "-a" },
  description = "..."
)
final class ArchiveCommand extends CLI.BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    singleCmdCheck();
    System.out.printf("Hello, %s!%n", exporter.projectPath);
    return 0;
  }
}

@CommandLine.Command(
  name = "--encrypt", aliases = { "-e" },
  description = "..."
)
final class EncryptCommand extends CLI.BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    singleCmdCheck();
    System.out.printf("Hello, %s!%n", exporter.projectPath);
    return 0;
  }
}
