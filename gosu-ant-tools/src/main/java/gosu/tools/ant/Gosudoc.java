package gosu.tools.ant;

import gosu.tools.ant.util.AntLoggingHelper;
import gw.lang.Gosu;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Access to the GosuDoc tool from Ant.
 */
public class Gosudoc extends Task {
  private final AntLoggingHelper log = new AntLoggingHelper(this);

  private Path _inputDirs;
  private File _outputDir;
  private boolean _verbose = false;
  private Path _runtimeClasspath;
  private String _title = "Gosu Documentation";
  
  /**
   * Specify where to find source file
   *
   * @param inputDirs a Path instance containing the various source directories.
   */
  public void setInputDirs(Path inputDirs) {
    if (_inputDirs == null) {
      _inputDirs = inputDirs;
    } else {
      _inputDirs.append(inputDirs);
    }
  }

  /**
   * Set the directory where the GosuDoc output will be generated.
   *
   * @param dir the output directory.
   */
  public void setOutputDir(File dir) {
    _outputDir = dir;
  }

  public void setVerbose(boolean verbose) {
    _verbose = verbose;
  }
  
  /**
   * Adds a path to the classpath.
   *
   * @return a classpath to be configured
   */
  public Path createClasspath() {
    if (_runtimeClasspath == null) {
      _runtimeClasspath = new Path(getProject());
    }
    return _runtimeClasspath.createPath();
  }

  /**
   * Adds a reference to a classpath defined elsewhere.
   *
   * @param ref a reference to a classpath
   */
  public void setClasspathRef(Reference ref) {
    createClasspath().setRefid(ref);
  }
  
  public void execute() throws BuildException {
    //List<String> packagesToDoc = new ArrayList<String>();

    boolean didInit = Gosu.bootstrapGosuWhenInitiatedViaClassfile();
    
    final String GOSUDOC_FQN = "gw.gosudoc.GSDocHTMLWriter";
    
    if(TypeSystem.getByFullNameIfValid(GOSUDOC_FQN) == null) {
      throw new BuildException("Could not find " + GOSUDOC_FQN + " on the classpath.  Is the gosu-doc JAR available?");
    }
    
    IGosuObject gsDocHTMLWriter = ReflectUtil.constructGosuClassInstance(GOSUDOC_FQN);
    
    List<File> inputDirs = new ArrayList<>();
    String[] inputDirList = _inputDirs.list();
    for(String filename : inputDirList) {
      File file = getProject().resolveFile(filename);
      if (!file.exists()) {
        throw new BuildException("inputdir \"" + file.getPath() + "\" does not exist!", getLocation());
      }
      inputDirs.add(file);
    }

    ReflectUtil.setProperty(gsDocHTMLWriter, "InputDirs", inputDirs); //expects List<File>
    ReflectUtil.setProperty(gsDocHTMLWriter, "Output", _outputDir); //expects File
    ReflectUtil.setProperty(gsDocHTMLWriter, "Filters", Collections.emptyList()); //expects List
    ReflectUtil.setProperty(gsDocHTMLWriter, "ExternalDocs", Collections.emptyList()); //expects List<String>
    ReflectUtil.setProperty(gsDocHTMLWriter, "Verbose", _verbose); //expects Boolean

    try {
      ReflectUtil.invokeMethod(gsDocHTMLWriter, "write");
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new BuildException(e);
    } finally {
      //uninit Gosu
      GosuInitialization.instance(TypeSystem.getExecutionEnvironment()).uninitializeRuntime();
    }

  }

}
