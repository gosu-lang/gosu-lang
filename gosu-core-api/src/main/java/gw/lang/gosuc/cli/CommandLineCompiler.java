package gw.lang.gosuc.cli;

import com.beust.jcommander.JCommander;
import gw.lang.Gosu;
import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.simple.GosuCompiler;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineCompiler {

  public static void main(String[] args) {
    CommandLineOptions options = new CommandLineOptions();
    JCommander help = new JCommander(options, args);
    if(args.length == 0 || options.isHelp()) {
      //dump the summary when gosuc is called w/o any args
      help.setProgramName("gosuc");
      help.usage();
      System.out.println("In addition, the @<filename> syntax may be used to read the above options and source files from a file");
      System.exit(0);
    } else {
      new CommandLineCompiler().execute(options);
    }
  }

  private void execute(CommandLineOptions options) {
    if(options.isVersion()) {
      System.out.println("gosuc " + Gosu.getVersion());
      System.exit(0);
    }

    SoutCompilerDriver driver = new SoutCompilerDriver();
    IGosuCompiler gosuc = new GosuCompiler();

    List<String> sourcepath = Arrays.asList(options.getSourcepath().split(File.pathSeparator));

    List<String> classpath = new ArrayList<>();
    //TODO should the gosu libs be required to be on the gosuc -classpath option?
    classpath.addAll(Arrays.asList(options.getClasspath().split(File.pathSeparator)));
    classpath.addAll(GosucUtil.getJreJars());
    try {
      classpath.addAll(GosucUtil.getGosuBootstrapJars());
    } catch(ClassNotFoundException cnfe) {
      throw new RuntimeException("Unable to locate Gosu libraries in classpath.\n", cnfe);
    }

    if(options.isVerbose()) {
      System.out.println("Initializing gosu compiler with classpath:" + classpath);
    }

    gosuc.initializeGosu(sourcepath, classpath, options.getDestDir());

    for(String file : options.getSourceFiles()) {
      try {
        if(options.isVerbose()) {
          System.out.println("gosuc: about to compile file: " + file);
        }
        gosuc.compile(new File(file), driver);
      } catch (Exception e) {
          System.out.println("Error compiling " + file);
          e.printStackTrace();
      }
    }

    gosuc.unitializeGosu();

    boolean hasErrors = printErrorsAndWarnings(driver, options.isNoWarn());

    System.exit(hasErrors ? 1 : 0);
  }

  /**
   * @param driver
   * @return true if driver had errors, false otherwise
   */
  private boolean printErrorsAndWarnings(SoutCompilerDriver driver, boolean isNoWarn) {
    boolean hasErrors = driver.hasErrors();
    List<String> warnings = driver.getWarnings();
    List<String> errors = driver.getErrors();

    for(String warningMsg : warnings) {
      if(!isNoWarn) {
        System.out.println(warningMsg);
      }
    }
    if(hasErrors) {
      for(String errorMsg : errors) {
        System.out.println(errorMsg);
      }
    }

    if(isNoWarn) {
      System.out.printf("\ngosuc completed with %d errors. Warnings were disabled.\n", errors.size());
    } else {
      System.out.printf("\ngosuc completed with %d warnings and %d errors.\n", warnings.size(), errors.size());
    }

    return hasErrors;
  }

}
