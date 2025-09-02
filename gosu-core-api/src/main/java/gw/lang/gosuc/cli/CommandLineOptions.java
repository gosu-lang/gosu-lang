package gw.lang.gosuc.cli;

import gw.internal.ext.com.beust.jcommander.Parameter;
import gw.internal.ext.com.beust.jcommander.validators.PositiveInteger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandLineOptions {

  // using String parameter types with arity = 0 will suppress the annoying 'Default: false' help output

  @Parameter(names = {"-ca", "-checkedArithmetic"}, description = "Compile with checked arithmetic")
  private boolean _checkedarithmetic;

    /**
   * @return true if '-ca' or '-checkedArithmetic' was specified on the command line
   */
  public boolean isCheckedArithmetic() {
    return _checkedarithmetic;
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
  private boolean _help;

  /**
   * @return true if '-help' was specified on the command line
   */
  public boolean isHelp() {
    return _help;
  }

  @Parameter(names = "-nowarn", description = "Generate no warnings")
  private boolean _nowarn;

  /**
   * @return true if '-nowarn' was specified on the command line
   */
  public boolean isNoWarn() {
    return _nowarn;
  }

  @Parameter(names = "-sourcepath", description = "Specify where to find input source files")
  private String _sourcepath;

  public String getSourcepath() {
    return _sourcepath == null ? "" : _sourcepath;
  }

  @Parameter(names = "-verbose", description = "Output messages about what the compiler is doing")
  private boolean _verbose;

  /**
   * @return true if '-verbose' was specified on the command line
   */
  public boolean isVerbose() {
    return _verbose;
  }

  @Parameter(names = "-version", description = "Version information")
  private boolean _version;

  /**
   * @return true if '-version' was specified on the command line
   */
  public boolean isVersion() {
    return _version;
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

  @Parameter(names = "-incremental", description = "Enable incremental compilation")
  private boolean _incremental;

  /**
   * @return true if '-incremental' was specified on the command line
   */
  public boolean isIncremental() {
    return _incremental;
  }

  @Parameter(names = "-dependency-file", description = "Path to dependency tracking file for incremental compilation")
  private String _dependencyFile;

  public String getDependencyFile() {
    return _dependencyFile == null ? ".gosuc-deps.json" : _dependencyFile;
  }

  @Parameter(names = "-changed-files", description = "Changed source files for incremental compilation (separated by path separator)")
  private String _changedFiles;

  @Parameter(names = "-deleted-files", description = "Deleted source files for incremental compilation (separated by path separator)")
  private String _deletedFiles;

  public List<String> getChangedFiles() {
    if (_changedFiles == null || _changedFiles.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> files = new ArrayList<>();
    for (String file : _changedFiles.split(java.io.File.pathSeparator)) {
      String trimmed = file.trim();
      if (!trimmed.isEmpty()) {
        files.add(trimmed);
      }
    }
    return files;
  }

  public List<String> getDeletedFiles() {
    if (_deletedFiles == null || _deletedFiles.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> files = new ArrayList<>();
    for (String file : _deletedFiles.split(java.io.File.pathSeparator)) {
      String trimmed = file.trim();
      if (!trimmed.isEmpty()) {
        files.add(trimmed);
      }
    }
    return files;
  }

}
