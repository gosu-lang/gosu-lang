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

  @Parameter(names = "-changed-types", description = "Changed type FQCNs (Java + Gosu) for incremental compilation (path-separator delimited)")
  private String _changedTypes;

  @Parameter(names = "-removed-types", description = "Removed type FQCNs (Java + Gosu) for incremental compilation (path-separator delimited)")
  private String _removedTypes;

  @Parameter(names = "-local-java-types", description = "FQCNs of same-module Java types for selective tracking (path-separator delimited)")
  private String _localJavaTypes;

  public List<String> getChangedTypes() {
    if (_changedTypes == null || _changedTypes.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> types = new ArrayList<>();
    for (String type : _changedTypes.split(java.io.File.pathSeparator)) {
      String trimmed = type.trim();
      if (!trimmed.isEmpty()) {
        types.add(trimmed);
      }
    }
    return types;
  }

  public List<String> getRemovedTypes() {
    if (_removedTypes == null || _removedTypes.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> types = new ArrayList<>();
    for (String type : _removedTypes.split(java.io.File.pathSeparator)) {
      String trimmed = type.trim();
      if (!trimmed.isEmpty()) {
        types.add(trimmed);
      }
    }
    return types;
  }

  public List<String> getLocalJavaTypes() {
    if (_localJavaTypes == null || _localJavaTypes.trim().isEmpty()) {
      return Collections.emptyList();
    }
    List<String> types = new ArrayList<>();
    for (String type : _localJavaTypes.split(java.io.File.pathSeparator)) {
      String trimmed = type.trim();
      if (!trimmed.isEmpty()) {
        types.add(trimmed);
      }
    }
    return types;
  }

}
