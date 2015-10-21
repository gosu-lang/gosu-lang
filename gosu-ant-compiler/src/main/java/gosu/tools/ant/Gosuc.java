package gosu.tools.ant;

import gw.lang.gosuc.simple.ICompilerDriver;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
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

public class Gosuc extends GosuMatchingTask {

  private Path _src;
  private File _destDir;
  private Path _compileClasspath;
  private boolean _failOnError = true;
  private boolean _checkedArithmetic = false;
  private Set<String> _scriptExtensions = new HashSet<>(Arrays.asList("gs", "gsx", "gst"));

  protected File[] compileList = new File[0];
  
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
    File[] newFiles;
    for (String extension : getScriptExtensions()) {
      m.setFrom("*." + extension);
      m.setTo("*.class");
      newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
      addToCompileList(newFiles);
    }

  }

  protected void addToCompileList(File[] newFiles) {
    if (newFiles.length > 0) {
      File[] newCompileList = new File[compileList.length + newFiles.length];
      System.arraycopy(compileList, 0, newCompileList, 0, compileList.length);
      System.arraycopy(newFiles, 0, newCompileList, compileList.length, newFiles.length);
      compileList = newCompileList;
    }
  }

  /**
   * Gets the list of files to be compiled.
   *
   * @return the list of files as an array
   */
  public File[] getFileList() {
    return compileList;
  }
  
  /**
   * Executes the task.
   *
   * @throws BuildException if an error occurs
   */
  public void execute() throws BuildException {
    log("srcdir=" + getSrcdir(), Project.MSG_DEBUG);
    log("destdir=" + getDestdir(), Project.MSG_DEBUG);
    log("failOnError=" + getFailOnError(), Project.MSG_DEBUG);
    log("checkedArithmetic=" + isCheckedArithmetic(), Project.MSG_DEBUG);
    log("_compileClasspath=" + _compileClasspath, Project.MSG_DEBUG);

    if(isCheckedArithmetic()) {
      System.setProperty("checkedArithmetic", "true");
    }
    
    ICompilerDriver driver = new SoutCompilerDriver();
    IGosuCompiler gosuc = new gw.lang.gosuc.simple.GosuCompiler();

    List<String> classpath = new ArrayList<>();
    classpath.addAll(Arrays.asList(_compileClasspath.list()));
    classpath.addAll(getJreJars());

    log("Initializing Gosu compiler...", Project.MSG_INFO);
    log("\tsourceFolders:" + Arrays.asList(getSrcdir().list()), Project.MSG_DEBUG);
    log("\tclasspath:" + classpath, Project.MSG_DEBUG);
    log("\toutputPath:" + getDestdir().getAbsolutePath(), Project.MSG_DEBUG);

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

    log("About to compile these files:", Project.MSG_DEBUG);
    for(File file : compileList) {
      log("\t" + file.getAbsolutePath() , Project.MSG_DEBUG);
    }
    
    for(File file : compileList) {
      try {
        gosuc.compile(file, driver);
      } catch (Exception e) {
        log(e.getMessage(), Project.MSG_ERR);
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

    sb.append(hasWarningsOrErrors ? ':' : "");
    log(sb.toString(), hasWarningsOrErrors ? Project.MSG_WARN : Project.MSG_INFO);
    warningMessages.forEach(this::log);
    errorMessages.forEach(this::log);

    if(errorsInCompilation) {
      if(getFailOnError()) {
        buildError("Gosu compilation failed with errors; see compiler output for details.");
      } else {
        log("Gosu Compiler: Ignoring compilation failure(s) as 'failOnError' was set to false", Project.MSG_WARN);
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
