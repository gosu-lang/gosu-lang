/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.config.CommonServices;
import gw.lang.cli.CommandLineAccess;
import gw.lang.init.ClasspathToGosuPathEntryUtil;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.OSPlatform;
import gw.util.StreamUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Gosu
{
  private static List<File> _classpath;

  public static void main( String[] args )
  {
    new Gosu().start( args );
  }


  private int start( String[] args )
  {
    try
    {
      int ret;

      if( args.length == 0 )
      {
        showHelpAndQuit();
      }
      int i = 0;
      String cpValue = null;
      boolean cmdLineCP = args[0].equals( "-classpath" );
      if(cmdLineCP)
      {
        i = 2;
        if(i >= args.length)
        {
          showHelpAndQuit();
        }
        cpValue = args[1];
      }

      if(args[i].equals( "-e" )) {
        List<File> classpath = makeClasspath( cpValue, "", cmdLineCP );
        init(classpath);
        ret = runWithInlineScript(args[i+1], collectArgs(i+2, args));
      }
      else
      {
        File script = new File( args[i] );
        if( !script.isFile() || !script.exists() )
        {
          showHelpAndQuit();
        }
        if ( cpValue == null ) {
          cpValue = extractClassPathFromSrc( script.getAbsolutePath() );
        }
        List<File> classpath = makeClasspath( cpValue, script.getParent(), cmdLineCP );
        init(classpath);
        ret = runWithFile(script, collectArgs(i+1, args));
      }
      return ret;
    }
    catch( Throwable t )
    {
      t.printStackTrace( System.err );
      return 2;
    }
  }

  private List<String> collectArgs(int i, String[] args) {
    List<String> scriptArgs = new ArrayList<String>();

    while( i < args.length )
    {
      scriptArgs.add( args[i] );
      i++;
    }
    return scriptArgs;
  }

  private String extractClassPathFromSrc(String file) {
    BufferedReader br = null;
    String line;
    String ret = null;

    try
    {
      br = new BufferedReader( new FileReader(file) );
      while ( (line = br.readLine()).trim().isEmpty() ); //ignore
      if (line.startsWith("classpath"))
      {
        int b = line.indexOf('"');
        if (b != -1)
        {
          int e = line.indexOf('"', b+1);
          if (e != -1)
          {
            ret = line.substring(b + 1, e);
          }
        }
      }
    } catch (IOException e) {} //ignore
    finally
    {
      try
      {
        if (br != null)
          br.close();
      } catch (IOException ex) {} //ignore
    }
    return ret;
  }

  private static List<File> makeClasspath(String cpValue, String scriptRoot, boolean cmdLineCP)
  {
    ArrayList<File> cp = new ArrayList<File>();
    if ( cpValue != null ) {
      StringTokenizer st = new StringTokenizer( cpValue, ",", false );
      while( st.hasMoreTokens() )
      {
        String s = st.nextToken();
        if( ( s.contains( ":" ) && !OSPlatform.isWindows()) || s.contains( ";" ) )
        {
          System.err.println( "WARNING: The Gosu classpath argument should be comma separated to avoid system dependencies.\n" +
            "It appears you are passing in a system-dependent path delimiter" );
        }
        String pathname = cmdLineCP ?  s : scriptRoot + File.separatorChar + s;
        cp.add(new File(pathname));
      }
    }
    return cp;
  }

  // Note this is a giant hack, we need to instead get the type name from the psiClass
  private static String makeFqn( File file )
  {
    String path = file.getAbsolutePath();
    int srcIndex = path.indexOf( "src" + File.separatorChar );
    if (srcIndex >= 0) {
      String fqn = path.substring(srcIndex + 4).replace(File.separatorChar, '.');
      return fqn.substring(0, fqn.lastIndexOf('.'));
    } else { // the Gosu Scratchpad case
      String fqn = file.getName();
      fqn = "nopackage." + fqn.substring(0, fqn.lastIndexOf('.')).replace(" ", "");
      return fqn;
    }
  }


  public static void setClasspath( List<File> classpath )
  {
    removeDups( classpath );

    if( classpath.equals( _classpath ) )
    {
      return;
    }

    _classpath = classpath;
    ClassLoader loader = TypeSystem.getCurrentModule() == null
                         // Can be null if called before the exec environment is setup, so assume the future parent of the module loader is the plugin loader
                         ? CommonServices.getEntityAccess().getPluginClassLoader()
                         : TypeSystem.getGosuClassLoader().getActualLoader();
    if( loader instanceof URLClassLoader )
    {
      for( File entry : classpath )
      {
        try
        {
          Method addURL = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
          addURL.setAccessible( true );
          addURL.invoke( loader, entry.toURI().toURL() );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
    }

    reinitGosu( classpath );
    TypeSystem.refresh( true );
  }

  private static void reinitGosu( List<File> classpath )
  {
    try
    {
      GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).reinitializeRuntime( ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries( classpath ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }

  private static void removeDups( List<File> classpath )
  {
    for( int i = classpath.size()-1; i >= 0; i-- )
    {
      File f = classpath.get( i );
      classpath.remove( i );
      if( !classpath.contains( f ) )
      {
        classpath.add( i, f );
      }
    }
  }

  /**
   * Initializes Gosu using the classpath derived from the current classloader and system classpath.
   */
  public static void init()
  {
    init( null );
  }

  public static void init( List<File> classpath )
  {

    List<File> combined = new ArrayList<File>();
    combined.addAll( deriveClasspathFrom( Gosu.class ) );
    if( classpath != null )
    {
      combined.addAll( classpath );
    }
    setClasspath(combined);
  }

  public static boolean bootstrapGosuWhenInitiatedViaClassfile()
  {
    if( GosuInitialization.isAnythingInitialized() &&
        GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).isInitialized() )
    {
      return false;
    }
    init();
    return true;
  }

  static void showHelpAndQuit()
  {
    System.out.println("Usage:\n" +
      "    gosu [-classpath 'entry1,entry2...'] program.gsp [args...]\n" +
      "    gosu [-classpath 'entry1,entry2...'] -e 'inline script' [args...]\n\n");
    System.exit(1);
  }

  public static List<File> deriveClasspathFrom( Class clazz )
  {
    List<File> ll = new LinkedList<File>();
    ClassLoader loader = clazz.getClassLoader();
    while( loader != null )
    {
      if( loader instanceof URLClassLoader )
      {
        for( URL url : ((URLClassLoader)loader).getURLs() )
        {
          try
          {
            File file = new File( url.toURI() );
            if( file.exists() )
            {
              ll.add( file );
            }
          }
          catch( Exception e )
          {
            //ignore
          }
        }
      }
      loader = loader.getParent();
    }
    return ll;
  }

  public static GosuVersion getVersion()
  {
    InputStream in = Gosu.class.getClassLoader().getResourceAsStream(GosuVersion.RESOURCE_PATH);
    Reader reader = StreamUtil.getInputStreamReader(in);
    return GosuVersion.parse(reader);
  }

  private int runWithFile(File script, List<String> args) throws IOException, ParseResultsException
  {
    CommandLineAccess.setCurrentProgram(script);
    // set remaining arguments as arguments to the Gosu program
    CommandLineAccess.setRawArgs( args );
    byte[] bytes = StreamUtil.getContent( new BufferedInputStream(new FileInputStream(script)));
    String content = StreamUtil.toString( bytes );
    IFileContext ctx = null;
    ctx = new ProgramFileContext( script,  makeFqn( script ) );
    IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
    ParserOptions options = new ParserOptions().withFileContext( ctx );
    IParseResult result = programParser.parseExpressionOrProgram( content, new StandardSymbolTable( true ), options );
    IGosuProgram program = result.getProgram();
    Object ret = program.getProgramInstance().evaluate(null); // evaluate it
    IType expressionType = result.getType();
    if( expressionType != null && !JavaTypes.pVOID().equals(expressionType) )
    {
      GosuShop.print( ret );
    }
    return 0;
  }

  private int runWithInlineScript(String script, List<String> args) throws IOException, ParseResultsException
  {
    CommandLineAccess.setCurrentProgram(null);
    // set remaining arguments as arguments to the Gosu program
    CommandLineAccess.setRawArgs( args );
    IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
    IParseResult result = programParser.parseExpressionOrProgram( script, new StandardSymbolTable( true ), new ParserOptions() );
    IGosuProgram program = result.getProgram();
    Object ret = program.getProgramInstance().evaluate(null); // evaluate it
    IType expressionType = result.getType();
    if( expressionType != null && !JavaTypes.pVOID().equals(expressionType) )
    {
      GosuShop.print( ret );
    }
    return 0;
  }
}
