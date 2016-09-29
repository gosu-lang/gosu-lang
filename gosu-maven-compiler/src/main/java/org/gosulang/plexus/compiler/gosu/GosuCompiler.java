package org.gosulang.plexus.compiler.gosu;

import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.cli.CommandLineCompiler;
import gw.lang.gosuc.simple.ICompilerDriver;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;
import org.codehaus.plexus.compiler.AbstractCompiler;
import org.codehaus.plexus.compiler.CompilerConfiguration;
import org.codehaus.plexus.compiler.CompilerException;
import org.codehaus.plexus.compiler.CompilerMessage;
import org.codehaus.plexus.compiler.CompilerOutputStyle;
import org.codehaus.plexus.compiler.CompilerResult;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    File destinationDir = new File(config.getOutputLocation());

    if (!destinationDir.exists()) {
      destinationDir.mkdirs();
    }

    String[] sourceFiles = getSourceFiles(config);

    if ((sourceFiles == null) || (sourceFiles.length == 0)) {
      return new CompilerResult(); //defaults to 'success == true'
    }

    if ((getLogger() != null) && getLogger().isInfoEnabled()) {
      getLogger().info("Compiling " + sourceFiles.length + " " +
          "source file" + (sourceFiles.length == 1 ? "" : "s") +
          " to " + destinationDir.getAbsolutePath());
    }

    CompilerResult result;

    if (config.isFork()) {
      String executable = config.getExecutable();

      if (StringUtils.isEmpty(executable)) {
        try {
          executable = getJavaExecutable();
        } catch (IOException e) {
          getLogger().warn("Unable to autodetect 'java' path, using 'java' from the environment.");
          executable = "java";
        }
      }      

      config.setExecutable(executable);
      
      result = compileOutOfProcess(config);
    } else {
      result = compileInProcess(config);
    }

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
    classpath.addAll(GosucUtil.getJreJars());
    try {
      getLogger().info("Adding Gosu JARs to compiler classpath");
      classpath.addAll(GosucUtil.getGosuBootstrapJars());
    } catch(ClassNotFoundException cnfe) {
      throw new CompilerException("Unable to locate Gosu libraries in classpath.  Please add Gosu as a project dependency.", cnfe);
    }

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
  
  CompilerResult compileOutOfProcess(CompilerConfiguration config) throws CompilerException {
    Commandline cli = new Commandline();
    File workingDirectory = config.getWorkingDirectory() == null ? new File("") : config.getWorkingDirectory();
    cli.setWorkingDirectory(workingDirectory.getAbsolutePath());
    cli.setExecutable(config.getExecutable());

    //respect JAVA_OPTS, if it exists
    String JAVA_OPTS = System.getenv("JAVA_OPTS");
    if(JAVA_OPTS != null) {
      cli.addArguments(new String[] {JAVA_OPTS});
    }
    
    //compilerArgs - arguments to send to the forked JVM
    Set<String> compilerArgs = config.getCustomCompilerArgumentsAsMap().keySet();
    if(compilerArgs.size() > 0) {
      cli.addArguments(compilerArgs.toArray(new String[(compilerArgs.size())]));
    }

    if(Os.isFamily(Os.FAMILY_MAC)) {
      cli.addArguments(new String[] {"-Xdock:name=Gosuc"});
    }

    try {
      getLogger().info("Initializing gosuc compiler");
      cli.addArguments(new String[] {"-classpath", String.join(File.pathSeparator, GosucUtil.getGosuBootstrapJars())});
    } catch(ClassNotFoundException cnfe) {
      throw new CompilerException("Unable to locate Gosu libraries in classpath.  Please add Gosu as a project dependency.", cnfe);
    }

    cli.addArguments(new String[] {"gw.lang.gosuc.cli.CommandLineCompiler"});

    try {
      File argFile = createArgFile(config);
      cli.addArguments(new String[] { "@" + argFile.getCanonicalPath().replace(File.separatorChar, '/') });
    } catch (IOException e) {
      throw new CompilerException("Error creating argfile with gosuc arguments", e);
    }

    CommandLineUtils.StringStreamConsumer sysout = new CommandLineUtils.StringStreamConsumer();
    CommandLineUtils.StringStreamConsumer syserr = new CommandLineUtils.StringStreamConsumer();
    
    int exitCode;
    
    try {
      if(config.isVerbose()) {
        getLogger().info("Executing gosuc in external process with command: " + cli.toString());
      }
      exitCode = CommandLineUtils.executeCommandLine(cli, sysout, syserr);
    } catch (CommandLineException e) {
      throw new CompilerException("Error while executing the external compiler.", e);
    }

    List<CompilerMessage> messages = parseMessages(exitCode, sysout.getOutput());

    int warningCt = 0;
    int errorCt = 0;
    for(CompilerMessage message : messages) {
      switch(message.getKind()) {
        case WARNING:
          warningCt++;
          break;
        case ERROR:
          errorCt++;
          break;
      }
    }

    if(config.isShowWarnings()) {
      getLogger().info(String.format("gosuc completed with %d warnings and %d errors.", warningCt, errorCt));
    } else {
      getLogger().info(String.format("gosuc completed with %d errors. Warnings were disabled.", errorCt));
    }

    return new CompilerResult(exitCode == 0, messages);
  }

  private File createArgFile(CompilerConfiguration config) throws IOException {
    File tempFile;
    if ((getLogger() != null) && getLogger().isDebugEnabled()) {
      tempFile = File.createTempFile(CommandLineCompiler.class.getName(), "arguments", new File(config.getOutputLocation()));
    } else {
      tempFile = File.createTempFile(CommandLineCompiler.class.getName(), "arguments");
      tempFile.deleteOnExit();
    }

    List<String> fileOutput = new ArrayList<>();

    // The classpath used to initialize Gosu; CommandLineCompiler will supplement this with the JRE jars
    fileOutput.add("-classpath");
    fileOutput.add(String.join(File.pathSeparator, config.getClasspathEntries()));

    fileOutput.add("-d");
    fileOutput.add(config.getOutputLocation());

    fileOutput.add("-sourcepath");
    fileOutput.add(String.join(File.pathSeparator, config.getSourceLocations()));

    if(!config.isShowWarnings()) {
      fileOutput.add("-nowarn");
    }

    if(config.isVerbose()) {
      fileOutput.add("-verbose");
    }

    for(File sourceFile : config.getSourceFiles()) {
      fileOutput.add(sourceFile.getPath());
    }

    Files.write(tempFile.toPath(), fileOutput, StandardCharsets.UTF_8);

    return tempFile;
  }

  private List<CompilerMessage> parseMessages(int exitCode, String sysout) {
    List<CompilerMessage> messages = new ArrayList<>();

    // $1: warning|error
    final String soutPattern = "^.*:\\[\\d+,\\d+\\]\\s(warning|error):(?:.*\\n)?.*\\[line:\\d+\\scol:\\d+\\]\\sin$\\n(?:^line\\s+\\d+:.*\\n){1,3}";

    Pattern regex = Pattern.compile(soutPattern, Pattern.MULTILINE);
    Matcher regexMatcher = regex.matcher(sysout);
    while(regexMatcher.find()) {
      CompilerMessage.Kind kind = regexMatcher.group(1).equals("warning") ? CompilerMessage.Kind.WARNING : CompilerMessage.Kind.ERROR;
      messages.add(new CompilerMessage(regexMatcher.group(), kind));
    }

    return messages;
  }

  /**
   * Get the path of the java executable: try to find it depending the
   * OS or the <code>java.home</code> system property or the
   * <code>JAVA_HOME</code> environment variable.
   *
   * @return the absolute path of the java executable
   * @throws IOException
   *             if not found
   */
  private String getJavaExecutable() throws IOException {
    String javaCommand = "java" + (Os.isFamily(Os.FAMILY_WINDOWS) ? ".exe" : "");

    String javaHome = System.getProperty("java.home");
    File javaExe;
    if (Os.isName("AIX")) {
      javaExe = new File(javaHome + File.separator + ".." + File.separator + "sh", javaCommand);
    } else if (Os.isName("Mac OS X")) {
      javaExe = new File(javaHome + File.separator + "bin", javaCommand);
    } else {
      javaExe = new File(javaHome + File.separator + ".." + File.separator + "bin", javaCommand);
    }

    // ----------------------------------------------------------------------
    // Try to find javaExe from JAVA_HOME environment variable
    // ----------------------------------------------------------------------
    if (!javaExe.isFile()) {
      Properties env = CommandLineUtils.getSystemEnvVars();
      javaHome = env.getProperty("JAVA_HOME");
      if (StringUtils.isEmpty(javaHome)) {
        throw new IOException("The environment variable JAVA_HOME is not correctly set.");
      }
      if (!new File(javaHome).isDirectory()) {
        throw new IOException("The environment variable JAVA_HOME=" + javaHome
            + " doesn't exist or is not a valid directory.");
      }

      javaExe = new File(env.getProperty("JAVA_HOME") + File.separator + "bin", javaCommand);
    }

    if (!javaExe.isFile()) {
      throw new IOException("The java executable '" + javaExe
          + "' doesn't exist or is not a file. Verify the JAVA_HOME environment variable.");
    }

    return javaExe.getAbsolutePath();
  }

}
