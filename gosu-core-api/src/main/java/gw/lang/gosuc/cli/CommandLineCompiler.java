package gw.lang.gosuc.cli;

import gw.internal.ext.com.beust.jcommander.JCommander;
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

    IGosuCompiler gosuc = new GosuCompiler();

    List<String> sourcepath = Arrays.asList(options.getSourcepath().split(File.pathSeparator));

    List<String> classpath = new ArrayList<>();
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
    
    if(options.isCheckedArithmetic()) {
      System.setProperty("checkedArithmetic", "true");
    }

    gosuc.initializeGosu(sourcepath, classpath, options.getDestDir());

    final List<String> warnings = new ArrayList<>();
    final List<String> errors = new ArrayList<>();
    boolean thresholdExceeded = false;
    
    for(String file : options.getSourceFiles()) {
      try {
        if(options.isVerbose()) {
          System.out.println("gosuc: about to compile file: " + file);
        }
        SoutCompilerDriver driver = new SoutCompilerDriver();
        gosuc.compile(new File(file), driver);
        
        //output warnings/errors
        printErrorsAndWarnings(driver, options.isNoWarn());
        
        //collect & check thresholds
        warnings.addAll(driver.getWarnings());
        errors.addAll(driver.getErrors());
        
        //if exceed threshold then break
        if(errors.size() > options.getMaxErrs()) {
          System.out.println("\nError threshold exceeded; aborting compilation.");
          thresholdExceeded = true;
          break;
        }
        if(!options.isNoWarn() && warnings.size() > options.getMaxWarns()) {
          System.out.println("\nWarning threshold exceeded; aborting compilation.");
          thresholdExceeded = true;
          break;
        }        
      } catch (Exception e) {
          System.out.println("Error compiling " + file);
          e.printStackTrace();
      }
    }

    gosuc.unitializeGosu();

    //print summary
    boolean exitWithFailure = summarize(warnings, errors, options.isNoWarn()) || thresholdExceeded;

    System.exit(exitWithFailure ? 1 : 0);
  }

  /**
   * @param driver results of compiling a single file
   * @param isNoWarn true if warnings are disabled
   */
  private void printErrorsAndWarnings(SoutCompilerDriver driver, boolean isNoWarn) {
    if(!isNoWarn) {
      //noinspection Convert2streamapi
      for(String warningMsg : driver.getWarnings()) {
        System.out.println(warningMsg);
      }
    }
    if(driver.hasErrors()) {
      //noinspection Convert2streamapi
      for(String errorMsg : driver.getErrors()) {
        System.out.println(errorMsg);
      }
    }
  }

  /**
   * @param warnings List of warnings
   * @param errors List of errors
   * @param isNoWarn true if warnings are disabled
   * @return true if compilation resulted in errors, false otherwise
   */
  private boolean summarize(List<String> warnings, List<String> errors, boolean isNoWarn) {
    if(isNoWarn) {
      System.out.printf("\ngosuc completed with %d errors. Warnings were disabled.\n", errors.size());
    } else {
      System.out.printf("\ngosuc completed with %d warnings and %d errors.\n", warnings.size(), errors.size());
    }

    return errors.size() > 0;
  }
  
}
