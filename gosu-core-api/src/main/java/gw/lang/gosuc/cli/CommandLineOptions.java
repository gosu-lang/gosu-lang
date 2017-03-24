package gw.lang.gosuc.cli;

import gw.internal.ext.com.beust.jcommander.Parameter;
import gw.internal.ext.com.beust.jcommander.validators.PositiveInteger;

import java.util.ArrayList;
import java.util.List;

public class CommandLineOptions {

  // using String parameter types with arity = 0 will suppress the annoying 'Default: false' help output

  @Parameter(names = {"-ca", "-checkedArithmetic"}, description = "Compile with checked arithmetic")
  private Boolean _checkedarithmetic = null;

  /**
   * @return true if and only if '-ca' or '-checkedArithmetic' was specified on the command line
   */
  public boolean isCheckedArithmetic() {
    return !(_checkedarithmetic == null || !_checkedarithmetic);
  }

  @Parameter(names = {"-cp", "-classpath"}, description = "Specify where to find user class files")
  private String _classpath;

  public String getClasspath() {
    return _classpath == null ? "" : _classpath;
  }

  @Parameter(names = "-d", description = "Specify where to place generated class files")
  private String _destDir;

  public String getDestDir() {
    return _destDir == null ? "" : _destDir;
  }

  @Parameter(names = "-help", description = "Print a synopsis of standard options", help = true)
  private Boolean _help = null;

  /**
   * @return true if and only if '-help' was specified on the command line
   */
  public boolean isHelp() {
    return !(_help == null || !_help);
  }

  @Parameter(names = "-nowarn", description = "Generate no warnings")
  private Boolean _nowarn = null;

  /**
   * @return true if and only if '-nowarn' was specified on the command line
   */
  public boolean isNoWarn() {
    return !(_nowarn == null || !_nowarn);
  }

  @Parameter(names = "-sourcepath", description = "Specify where to find input source files")
  private String _sourcepath;

  public String getSourcepath() {
    return _sourcepath == null ? "" : _sourcepath;
  }

  @Parameter(names = "-verbose", description = "Output messages about what the compiler is doing")
  private Boolean _verbose = null;

  /**
   * @return true if and only if '-verbose' was specified on the command line
   */
  public boolean isVerbose() {
    return !(_verbose == null || !_verbose);
  }

  @Parameter(names = "-version", description = "Version information")
  private Boolean _version = null;

  /**
   * @return true if and only if '-version' was specified on the command line
   */
  public boolean isVersion() {
    return !(_version == null || !_version);
  }

  @Parameter(description = "<source files>")
  private List<String> _srcFiles = new ArrayList<>();

  public List<String> getSourceFiles() {
    return _srcFiles;
  }
  public void setSourceFiles( List<String> srcFiles ) {
    _srcFiles = srcFiles;
  }

  @Parameter(names = "-maxerrs", description = "Set the maximum number of errors to print", validateWith = PositiveInteger.class)
  private int _maxerrs = 100;

  /**
   * @return maximum error threshold. Defaults to 1,000.
   */
  public int getMaxErrs() {
    return _maxerrs;
  }

  @Parameter(names = "-maxwarns", description = "Set the maximum number of warnings to print", validateWith = PositiveInteger.class)
  private int _maxwarns = Integer.MAX_VALUE;

  /**
   * @return maximum warning threshold. Defaults to Integer.MAX_VALUE.
   */
  public int getMaxWarns() {
    return _maxwarns;
  }

}
