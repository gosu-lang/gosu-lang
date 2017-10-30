package gw.lang.gosuc.cli;

import gw.internal.ext.com.beust.jcommander.JCommander;
import gw.lang.Gosu;
import gw.lang.gosuc.GosucUtil;
import gw.lang.gosuc.simple.GosuCompiler;
import gw.lang.gosuc.simple.ICompilerDriver;
import gw.lang.gosuc.simple.IGosuCompiler;
import gw.lang.gosuc.simple.SoutCompilerDriver;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineCompiler
{
  public static final String COMPILE_EXCEPTION_MSG = "gosuc completed with errors";
  
  public static void main( String[] args ) {
    main( args, true );
  }
  
  public static void main( String[] args, boolean exitUponCompletion )
  {
    CommandLineOptions options = new CommandLineOptions();
    JCommander help = new JCommander( options, args );
    if( args.length == 0 || options.isHelp() )
    {
      //dump the summary when gosuc is called w/o any args
      help.setProgramName( "gosuc" );
      help.usage();
      System.out.println( "In addition, the @<filename> syntax may be used to read the above options and source files from a file" );
      System.exit( 0 );
    }
    else
    {
      SoutCompilerDriver driver = new SoutCompilerDriver( true, !options.isNoWarn() );
      boolean thresholdExceeded = invoke( options, driver );

      //print summary
      boolean erroneousResult = summarize( driver.getWarnings(), driver.getErrors(), options.isNoWarn() ) || thresholdExceeded;
      
      if(exitUponCompletion) {
        System.exit( erroneousResult ? 1 : 0 );
      } else {
        if(erroneousResult) {
          throw new RuntimeException(COMPILE_EXCEPTION_MSG);
        }
      }
    }
  }

  public static boolean invoke( CommandLineOptions options, SoutCompilerDriver driver )
  {
    return new CommandLineCompiler().execute( options, driver );
  }

  private boolean execute( CommandLineOptions options, ICompilerDriver driver )
  {
    if( options.isVersion() )
    {
      System.out.println( "gosuc " + Gosu.getVersion() );
      System.exit( 0 );
    }

    IGosuCompiler gosuc = new GosuCompiler();

    List<String> sourcepath = Arrays.asList( options.getSourcepath().split( File.pathSeparator ) );

    List<String> classpath = new ArrayList<>();
    classpath.addAll( Arrays.asList( options.getClasspath().split( File.pathSeparator ) ) );
    classpath.addAll( GosucUtil.getJreJars() );
    try
    {
      classpath.addAll( GosucUtil.getGosuBootstrapJars() );
    }
    catch( ClassNotFoundException cnfe )
    {
      throw new RuntimeException( "Unable to locate Gosu libraries in classpath.\n", cnfe );
    }

    if( options.isVerbose() )
    {
      System.out.println( "Initializing gosu compiler with classpath:" + classpath );
    }

    if( options.isCheckedArithmetic() )
    {
      System.setProperty( "checkedArithmetic", "true" );
    }

    gosuc.initializeGosu( sourcepath, classpath, options.getDestDir() );

    boolean thresholdExceeded = gosuc.compile( options, driver );

    gosuc.uninitializeGosu();
    
    return thresholdExceeded;
  }

  /**
   * @param warnings List of warnings
   * @param errors   List of errors
   * @param isNoWarn true if warnings are disabled
   *
   * @return true if compilation resulted in errors, false otherwise
   */
  private static boolean summarize( List<String> warnings, List<String> errors, boolean isNoWarn )
  {
    if( isNoWarn )
    {
      System.out.printf( "\ngosuc completed with %d errors. Warnings were disabled.\n", errors.size() );
    }
    else
    {
      System.out.printf( "\ngosuc completed with %d warnings and %d errors.\n", warnings.size(), errors.size() );
    }

    return errors.size() > 0;
  }

}
