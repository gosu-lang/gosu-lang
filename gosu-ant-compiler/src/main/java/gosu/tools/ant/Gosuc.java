package gosu.tools.ant;

import gosu.tools.ant.util.AntLoggingHelper;
import gw.lang.gosuc.simple.ICompilerDriver;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ant task for compiling Gosu files to disk.
 * <p>The following parameters are available:
 *   <ul>
 *     <li>"srcdir" : A Path containing one or more source directories</li>
 *     <li>"destdir" : A File representing the output destination of the compilation</li>
 *     <li>"checkedarithmetic" : Compile with checked arithmetic if true.  Defaults to {@code false}.</li>
 *     <li>"failonerror" : Ignore compile errors and continue if true.  Defaults to {@code true}.</li>
 *   </ul>
 */
public class Gosuc extends GosuMatchingTask {
  private final AntLoggingHelper log = new AntLoggingHelper(this);
  
  private Path _src;
  private File _destDir;
  private Path _compileClasspath;
  private boolean _failOnError = true;
  private boolean _checkedArithmetic = false;
  private boolean _force = true;
  private Set<String> _scriptExtensions = new HashSet<>(Arrays.asList("gs", "gsx", "gst", "gsp"));

  protected List<File> compileList = new ArrayList<>();

  /**
   * Adds a path for source compilation.
   *
   * @return a nested src element.
   */
  public Path createSrc() {
    if (_src == null) {
      _src = new Path(getProject());
    }
    return _src.createPath();
  }

  /**
   * Recreate src.
   *
   * @return a nested src element.
   */
  protected Path recreateSrc() {
    _src = null;
    return createSrc();
  }

  /**
   * Set the source directories to find the source Gosu files.
   *
   * @param srcDir the source directories as a path
   */
  public void setSrcdir(Path srcDir) {
    if (_src == null) {
      _src = srcDir;
    } else {
      _src.append(srcDir);
    }
  }

  /**
   * Gets the source dirs to find the source Gosu files.
   *
   * @return the source directories as a path
   */
  public Path getSrcdir() {
    return _src;
  }

  /**
   * Set the destination directory into which the Gosu source
   * files should be compiled.
   *
   * @param destDir the destination directory
   */
  public void setDestdir(File destDir) {
    _destDir = destDir;
  }

  /**
   * Gets the destination directory into which the Gosu source files
   * should be compiled.
   *
   * @return the destination directory
   */
  public File getDestdir() {
    return _destDir;
  }

  /**
   * Adds a path to the classpath.
   *
   * @return a classpath to be configured
   */
  public Path createClasspath() {
    if (_compileClasspath == null) {
      _compileClasspath = new Path(getProject());
    }
    return _compileClasspath.createPath();
  }

  /**
   * Adds a reference to a classpath defined elsewhere.
   *
   * @param ref a reference to a classpath
   */
  public void setClasspathRef(Reference ref) {
    createClasspath().setRefid(ref);
  }

  private Set<String> getScriptExtensions() {
    return _scriptExtensions;
  }

  /**
   * Indicates whether the build will continue
   * even if there are compilation errors; defaults to true.
   *
   * @param fail if true halt the build on failure
   */
  public void setFailOnError(boolean fail) {
    _failOnError = fail;
  }

  /**
   * Gets the FailOnError flag.
   *
   * @return the FailOnError flag
   */
  public boolean getFailOnError() {
    return _failOnError;
  }

  public boolean isCheckedArithmetic() {
    return _checkedArithmetic;
  }

  public void setCheckedArithmetic( boolean checkedArithmetic ) {
    _checkedArithmetic = checkedArithmetic;
  }

  /**
   * Gets the Force flag.<br>
   * ant's directory scanner is timestamp based.  Without proper <depend> tasks, this could result in successful, but incomplete compilation.<br>
   * Therefore we default 'force' to true, which causes compilation of all matching source files, regardless of the timestamp comparison between source and target.
   * 
   * @return
   */
  public boolean isForce() {
    return _force;
  }

  public void setForce( boolean force ) {
    _force = force;
  }


  /**
   * Scans the directory looking for source files to be compiled.
   * The results are returned in the class variable compileList
   *
   * @param srcDir  The source directory
   * @param destDir The destination directory
   * @param files   An array of filenames
   */
  protected void scanDir(File srcDir, File destDir, String[] files) {
    GlobPatternMapper m = new GlobPatternMapper();
    SourceFileScanner sfs = new SourceFileScanner(this);
    List<File> newFiles;
    if(!isForce()) {
      log.warn("Relying on ant's SourceFileScanner, which only looks at timestamps.  If broken references result, try setting option 'force' to true.");
    }
    for (String extension : getScriptExtensions()) {
      m.setFrom("*." + extension);
      m.setTo("*.class");
      log.debug("Scanning for *." + extension + " files...");
      if(isForce()) {
        newFiles = asFiles(srcDir, files, m);
      } else {
        newFiles = Arrays.asList(sfs.restrictAsFiles(files, srcDir, destDir, m));
      }
      log.debug("Found these files:");
      for(File newFile : newFiles) {
        log.debug('\t' + newFile.getAbsolutePath());
      }
      compileList.addAll(newFiles);
    }

  }

  /**
   * Converts an array of relative String filenames to a {@code List<File>} 
   * @param srcDir The root directory of all files
   * @param files All files are relative to srcDir
   * @return a List of Files by joining srcDir to each file
   */
  private List<File> asFiles(File srcDir, String[] files, FileNameMapper m) {
    List<File> newFiles = new ArrayList<>();
    for(String file : files) {
      boolean hasMatchingExtension = m.mapFileName(file) != null; //use mapFileName as a check to validate if the source file extension is recognized by the mapper or not
      if(hasMatchingExtension) {
        newFiles.add(new File(srcDir, file));
      }
    }
    return newFiles;
  }

  /**
   * Gets the list of files to be compiled.
   *
   * @return the list of files
   */
  public List<File> getFileList() {
    return compileList;
  }
  
  /**
   * Executes the task.
   *
   * @throws BuildException if an error occurs
   */
  public void execute() throws BuildException {
    log.debug("src/srcdir=" + getSrcdir());
    log.debug("destdir=" + getDestdir());
    log.debug("failOnError=" + getFailOnError());
    log.debug("checkedArithmetic=" + isCheckedArithmetic());
    log.debug("_compileClasspath=" + _compileClasspath);

    if(isCheckedArithmetic()) {
      System.setProperty("checkedArithmetic", "true");
    }
    
    ICompilerDriver driver = new SoutCompilerDriver();
    IGosuCompiler gosuc = new gw.lang.gosuc.simple.GosuCompiler();

    List<String> classpath = new ArrayList<>();
    classpath.addAll(Arrays.asList(_compileClasspath.list()));
    classpath.addAll(getJreJars());

    log.info("Initializing Gosu compiler...");
    log.debug("\tsourceFolders:" + Arrays.asList(getSrcdir().list()));
    log.debug("\tclasspath:" + classpath);
    log.debug("\toutputPath:" + getDestdir().getAbsolutePath());

    String[] list = getSrcdir().list();
    for (String filename : list) {
      File file = getProject().resolveFile(filename);
      if (!file.exists()) {
        throw new BuildException("srcdir \"" + file.getPath() + "\" does not exist!", getLocation());
      }
      DirectoryScanner ds = this.getDirectoryScanner(file);
      String[] files = ds.getIncludedFiles();
      scanDir(file, _destDir != null ? _destDir : file, files);
    }
    
    gosuc.initializeGosu(Arrays.asList(getSrcdir().list()), classpath, getDestdir().getAbsolutePath());

    log.debug("About to compile these files:");
    for(File file : compileList) {
      log.debug("\t" + file.getAbsolutePath());
    }
    
    for(File file : compileList) {
      try {
        gosuc.compile(file, driver);
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new BuildException(e);
      }
    }

    gosuc.unitializeGosu();

    List<String> warnings = ((SoutCompilerDriver) driver).getWarnings();
    boolean errorsInCompilation = ((SoutCompilerDriver) driver).hasErrors();
    List<String> errors = ((SoutCompilerDriver) driver).getErrors();
    
    List<String> warningMessages = new ArrayList<>();
    List<String> errorMessages = new ArrayList<>();
   
    warnings.forEach(warning -> warningMessages.add("[WARNING] " + warning));
    int numWarnings = warningMessages.size();

    int numErrors = 0;
    if(errorsInCompilation) {
      errors.forEach(error -> errorMessages.add("[ERROR] " + error));
      numErrors = errorMessages.size();
    }

    boolean hasWarningsOrErrors = numWarnings > 0 || errorsInCompilation;
    StringBuilder sb;
    sb = new StringBuilder();
    sb.append("Gosu compilation completed");
    if(hasWarningsOrErrors) {
      sb.append(" with ");
      if(numWarnings > 0) {
        sb.append(numWarnings).append(" warning").append(numWarnings == 1 ? "" : 's');
      }
      if(errorsInCompilation) {
        sb.append(numWarnings > 0 ? " and " : "");
        sb.append(numErrors).append(" error").append(numErrors == 1 ? "" : 's');
      }
    } else {
      sb.append(" successfully.");
    }

    if(hasWarningsOrErrors) {
      log.warn(sb.toString());
    } else {
      log.info(sb.toString());
    }

    warningMessages.forEach(log::info);
    errorMessages.forEach(log::error);

    if(errorsInCompilation) {
      if(getFailOnError()) {
        buildError("Gosu compilation failed with errors; see compiler output for details.");
      } else {
        log.warn("Gosu Compiler: Ignoring compilation failure(s) as 'failOnError' was set to false");
      }
    }
    
  }

  /**
   * Get all JARs from the lib directory of the System's java.home property
   * @return List of absolute paths to all JRE libraries
   */
  private List<String> getJreJars() {
    String javaHome = System.getProperty("java.home");
    java.nio.file.Path libsDir = FileSystems.getDefault().getPath(javaHome, "/lib");
    try {
      return Files.walk(libsDir)
          .filter( path -> path.toFile().isFile())
          .filter( path -> path.toString().endsWith(".jar"))
          .map( java.nio.file.Path::toString )
          .collect(Collectors.toList());
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
