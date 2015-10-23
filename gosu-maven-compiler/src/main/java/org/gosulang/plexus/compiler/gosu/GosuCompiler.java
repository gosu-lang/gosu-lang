package org.gosulang.plexus.compiler.gosu;

import gw.lang.gosuc.simple.ICompilerDriver;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;
import org.codehaus.plexus.compiler.AbstractCompiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.compiler.CompilerResult;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GosuCompiler extends AbstractCompiler {

  public GosuCompiler() {
    super(CompilerOutputStyle.ONE_OUTPUT_FILE_PER_INPUT_FILE, "", ".class", null); // see MCOMPILER-199, mentioned in AbstractCompileMojo#getCompileSources.  It appears the empty string is the only workaround to have more than one static file suffix.
  }

  @Override
  public String[] createCommandLine(CompilerConfiguration config) throws CompilerException {
    return new String[0];
  }

  @Override
  public CompilerResult performCompile(CompilerConfiguration config) throws CompilerException {

    if (config.isFork()) {
      throw new CompilerException("This compiler does not support forked compilation.");
    }

    File destinationDir = new File(config.getOutputLocation());

    if (!destinationDir.exists()) {
      destinationDir.mkdirs();
    }

    String[] sourceFiles = getSourceFiles(config);

    if ((sourceFiles == null) || (sourceFiles.length == 0)) {
      return new CompilerResult().success( true );
    }

    if ((getLogger() != null) && getLogger().isInfoEnabled()) {
      getLogger().info("Compiling " + sourceFiles.length + " " +
          "source file" + (sourceFiles.length == 1 ? "" : "s") +
          " to " + destinationDir.getAbsolutePath());
    }

    CompilerResult result;

    result = compileInProcess(config);

    return result;
  }

  CompilerResult compileInProcess(CompilerConfiguration config) throws CompilerException {
    return compileInProcess(new String[0], config);
  }

  CompilerResult compileInProcess(String[] args, CompilerConfiguration config) throws CompilerException {
    ICompilerDriver driver = new SoutCompilerDriver();
    IGosuCompiler gosuc = new gw.lang.gosuc.simple.GosuCompiler();

    List<String> classpath = new ArrayList<>();
    classpath.addAll(config.getClasspathEntries());
    classpath.addAll(getJreJars());
    classpath.addAll(getGosuJars());

    gosuc.initializeGosu(config.getSourceLocations(), classpath, config.getOutputLocation());

    for(File file : config.getSourceFiles()) {
      try {
        gosuc.compile(file, driver);
      } catch (Exception e) {
        getLogger().error(e.getMessage());
      }
    }

    gosuc.unitializeGosu();

    boolean success = true;
    List<CompilerMessage> errorMessages = new ArrayList<>();

    for(String warningMsg : ((SoutCompilerDriver) driver).getWarnings()) {
      errorMessages.add(new CompilerMessage(warningMsg, CompilerMessage.Kind.WARNING));
    }

    if (((SoutCompilerDriver) driver).hasErrors()) {
      success = false;
      for(String errorMsg : ((SoutCompilerDriver) driver).getErrors()) {
        errorMessages.add(new CompilerMessage(errorMsg, CompilerMessage.Kind.ERROR));
      }
    }

    return new CompilerResult(success, errorMessages);
  }

  /**
   * Get all JARs from the lib directory of the System's java.home property
   * @return List of absolute paths to all JRE libraries
   */
  private List<String> getJreJars() {
    String javaHome = System.getProperty("java.home");
    Path libsDir = FileSystems.getDefault().getPath(javaHome, "/lib");
    try {
      return Files.walk(libsDir)
          .filter( path -> path.toFile().isFile())
          .filter( path -> path.toString().endsWith(".jar"))
          .map( Path::toString )
          .collect(Collectors.toList());
    } catch (SecurityException | IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * Get all the Gosu JARs, to be shared with the compiler for its initialization
   * @return List of absolute paths to gosu-core-api and gosu-core libraries
   */
  private List<String> getGosuJars() throws CompilerException {
    getLogger().info("Adding Gosu JARs to compiler classpath");
    return Arrays.asList(getClassLocation("gw.internal.gosu.parser.MetaType"), //get gosu-core
        getClassLocation("gw.lang.Gosu")); //get gosu-core-api
  }

  private String getClassLocation(String className) throws CompilerException {
    Class clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new CompilerException("Cannot find the requested className <" + className + "> in classpath.  Please add Gosu as a project dependency.");
    }
    ProtectionDomain pDomain = clazz.getProtectionDomain();
    CodeSource cSource = pDomain.getCodeSource();
    if (cSource != null) {
      URL loc = cSource.getLocation();
      File file;
      try {
        file = new File(URLDecoder.decode(loc.getPath(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        getLogger().warn("Unsupported Encoding for URL: " + loc, e);
        file = new File(loc.getPath());
      }
      getLogger().debug("Found location <" + file.getPath() + "> for className <" + className + ">");
      return file.getPath();
    } else {
      throw new CompilerException("Cannot find the location of the requested className <" + className + "> in classpath.  Please add Gosu as a project dependency.");
    }
  }

}
