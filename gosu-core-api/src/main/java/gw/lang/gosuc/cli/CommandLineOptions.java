package gw.lang.gosuc.cli;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

class CommandLineOptions {

  // using String parameter types with arity = 0 will suppress the annoying 'Default: false' help output

  @Parameter(names = {"-cp", "-classpath"}, description = "Specify where to find user class files")
  private String _classpath;

  protected String getClasspath() {
    return _classpath == null ? "" : _classpath;
  }

  @Parameter(names = "-d", description = "Specify where to place generated class files")
  private String _destDir;

  protected String getDestDir() {
    return _destDir == null ? "" : _destDir;
  }

  @Parameter(names = "-help", description = "Print a synopsis of standard options", help = true)
  private Boolean _help = null;

  /**
   * @return true if and only if '-help' was specified on the command line
   */
  protected boolean isHelp() {
    return !(_help == null || !_help);
  }

  @Parameter(names = "-nowarn", description = "Generate no warnings")
  private Boolean _nowarn = null;

  /**
   * @return true if and only if '-nowarn' was specified on the command line
   */
  protected boolean isNoWarn() {
    return !(_nowarn == null || !_nowarn);
  }

  @Parameter(names = "-sourcepath", description = "Specify where to find input source files")
  private String _sourcepath;

  protected String getSourcepath() {
    return _sourcepath == null ? "" : _sourcepath;
  }

  @Parameter(names = "-verbose", description = "Output messages about what the compiler is doing")
  private Boolean _verbose = null;

  /**
   * @return true if and only if '-verbose' was specified on the command line
   */
  protected boolean isVerbose() {
    return !(_verbose == null || !_verbose);
  }

  @Parameter(names = "-version", description = "Version information")
  private Boolean _version = null;

  /**
   * @return true if and only if '-version' was specified on the command line
   */
  protected boolean isVersion() {
    return !(_version == null || !_version);
  }

  @Parameter(description = "<source files>")
  private List<String> _srcFiles = new ArrayList<>();

  protected List<String> getSourceFiles() {
    return _srcFiles;
  }

}
