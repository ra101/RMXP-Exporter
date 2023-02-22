package exporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import picocli.CommandLine;

public class CLI {

  public static void main(String[] args) {
    System.exit(new CommandLine(new Exporter()).execute(args));
  }

  public static final String EXENAME = "exporter";
  public static final String NAME = "RMXP Exporter";
  public static final String VERSION = "2.1.1";
  public static final String DISPLAY_VER = NAME + " v" + VERSION;
  public static final String DESC =
    "`exporter` streamlines exporting RPG Maker XP projects for distribution.\n" +
    "  It can create both RGSS-Archives(.rgssad) and Distributable-Packages(.zip)\n" +
    "  based on specified configurations.";
}

@CommandLine.Command(
  name = CLI.EXENAME,
  subcommands = {
    ArchiveCommand.class, PackageCommand.class, ListCommand.class,
  }
)
final class Exporter extends BaseCommand {

  @CommandLine.Option(
    names = { "--project", "-d" },
    paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = "RMXP project path (def: Current Folder)"
  )
  public Path projectPath;

  @CommandLine.Option(
    names = { "--config-file", "-f" },
    paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Config file path (def: Project\\.exporterconfig)"
  )
  public Path configFile;

  @CommandLine.Option(
    names = { "--output-arc", "-o" },
    paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = ".rgssad file path (def: Project\\Game.rgssad)"
  )
  public Path outputArc;

  @CommandLine.Option(
    names = { "--output-pak", "-O" },
    paramLabel = "<s>",
    scope = CommandLine.ScopeType.INHERIT,
    description = ".zip file path (def: Project\\Dist\\Project.zip)\n"
  )
  public Path outputPak;

  @CommandLine.Option(
    names = { "--silent", "-s" },
    defaultValue = "false",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Do not output any information while processing."
  )
  public Boolean silentFlag;

  @CommandLine.Option(
    names = { "--clean", "-c" },
    defaultValue = "false",
    scope = CommandLine.ScopeType.INHERIT,
    description = "Delete old Output Files.\n"
  )
  public Boolean cleanFlag;

  public void resolveOptions() throws Exception {
    if (projectPath == null || projectPath.toString().isEmpty()) {
      projectPath = Paths.get(System.getProperty("user.dir"));
    }
    projectPath = projectPath.toAbsolutePath();
    checkPathExists(projectPath);

    configFile = resolveRelPath(configFile, ".exporterconfig");
    checkPathExists(configFile);

    outputArc = resolveRelPath(outputArc, "Game.rgssad");
    outputPak =
      resolveRelPath(
        outputPak,
        String.format("Dist\\%s.zip", projectPath.getFileName().toString())
      );
  }

  Path resolveRelPath(Path argPath, String basename) throws Exception {
    if (argPath == null || argPath.toString().isEmpty()) {
      argPath = Paths.get(projectPath.toString(), basename);
    }
    argPath = argPath.toAbsolutePath();
    return argPath;
  }

  void checkPathExists(Path path) throws Exception {
    if (Files.exists(path) == false) {
      String Error = String.format("Error: `%s` Not Found!\n", path);
      throw new CommandLine.ParameterException(new CommandLine(this), Error);
    }
  }

  @Override
  public Integer call() throws Exception {
    throw new CommandLine.ParameterException(
      new CommandLine(this),
      "Error: Either `--archive`, `--package` or `--list-files` must be Specified!\n"
    );
  }
}

@CommandLine.Command(
  name = "--list-files",
  aliases = { "-l" },
  description = "List all [files to be archived] and [files to be packaged] as per Config file."
)
final class ListCommand extends BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    super.call();
    System.out.printf("Hello, %s!%n", exporter.projectPath);
    return 0;
  }
}

@CommandLine.Command(
  name = "--package",
  aliases = { "-p" },
  description = "Create a Distributable Package (.zip) as per Config file.\n"
)
final class PackageCommand extends BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    super.call();
    System.out.printf("Hello, %s!%n", exporter.projectPath);
    return 0;
  }
}

@CommandLine.Command(
  name = "--archive",
  aliases = { "-a" },
  description = "Create an RGSS Archive (.rgssad) as per Config file."
)
final class ArchiveCommand extends BaseSubCommand {

  @Override
  public Integer call() throws Exception {
    super.call();
    System.out.printf("Hello, %s!%n", exporter.outputPak);
    return 0;
  }
}

@CommandLine.Command(
  version = CLI.DISPLAY_VER,
  mixinStandardHelpOptions = true,
  sortOptions = false,
  headerHeading = CLI.DISPLAY_VER + "\n\n" + CLI.DESC + "\n\n",
  synopsisHeading = "Usage:\n\t",
  customSynopsis = "exporter.exe [options] {--archive|--package|--list-files}\n",
  optionListHeading = "Options:\n",
  commandListHeading = "\nCommands:\n"
)
class BaseCommand implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    throw new CommandLine.PicocliException("Not Implemented!");
  }
}

class BaseSubCommand extends BaseCommand {

  @CommandLine.ParentCommand
  protected Exporter exporter;

  @CommandLine.Option(
    names = { "--package", "-a" },
    defaultValue = "false",
    hidden = true
  )
  private Boolean packageFlag;

  @CommandLine.Option(
    names = { "--archive", "-e" },
    defaultValue = "false",
    hidden = true
  )
  private Boolean archiveFlag;

  @CommandLine.Option(
    names = { "--list-files", "-l" },
    defaultValue = "false",
    hidden = true
  )
  private Boolean listFilesFlag;

  protected Integer singleCmdCheck() throws Exception {
    if (listFilesFlag || archiveFlag || packageFlag) {
      throw new CommandLine.ParameterException(
        new CommandLine(exporter),
        "Error: Either use `--archive`, `--package` or `--list-files`, Only One!\n"
      );
    }
    return 0;
  }

  @Override
  public Integer call() throws Exception {
    singleCmdCheck();
    exporter.resolveOptions();
    return 0;
    // use super.call(); in inherited classes
  }
}
