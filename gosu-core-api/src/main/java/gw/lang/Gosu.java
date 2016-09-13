/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.config.CommonServices;
import gw.lang.gosuc.GosucUtil;
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
import sun.misc.URLClassPath;

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
  /**
   * used as a virtual package e.g., for scratchpad
   */
  public static final String NOPACKAGE = "_nopackage_";
  public static final String GOSU_SCRATCHPAD_FQN = NOPACKAGE + ".GosuScratchpad";
  public static final String JAR_REPO_DIR = "JAR-REPO";     //!! if you change this, also change it in Launcher
  public static final String JAR_REPO_TXT = "jar-repo.txt"; //!! "

  private static List<File> _classpath;
  private static File _script;
  private static List<String> _rawArgs;

  public static void main( String[] args )
  {
    start( args );
  }

  private static void checkArgsLength( int i, int length )
  {
    if( i >= length )
    {
      showHelpAndQuit();
    }
  }

  private static void start( String[] args )
  {
    try
    {
      if( args.length == 0 )
      {
        launchEditor();
        return;
      }
      int i = 0;
      checkArgsLength( i, args.length );
      String cpValue = null;
      boolean cmdLineCP = false;
      if( args[i].equals( "-checkedArithmetic" ) )
      {
        i++;
        checkArgsLength( i, args.length );
        System.setProperty( "checkedArithmetic", "true" );
      }

      if( args[i].equals( "-classpath" ) )
      {
        cmdLineCP = true;
        i += 2;
        checkArgsLength( i, args.length );
        cpValue = args[i - 1];
      }

      switch( args[i] )
      {
        case "-fqn":
        {
          List<File> classpath = makeClasspath( cpValue, "", cmdLineCP );
          init( classpath );
          runWithType( args[i + 1], collectArgs( i + 2, args ) );
          break;
        }

        case "-e":
        {
          List<File> classpath = makeClasspath( cpValue, "", cmdLineCP );
          init( classpath );
          runWithInlineScript( args[i + 1], collectArgs( i + 2, args ) );
          break;
        }

        default:
        {
          File script = new File( args[i] );
          if( !script.isFile() || !script.exists() )
          {
            showHelpAndQuit();
          }
          if( cpValue == null )
          {
            cpValue = extractClassPathFromSrc( script.getAbsolutePath() );
          }
          List<File> classpath = makeClasspath( cpValue, script.getAbsoluteFile().getParent(), cmdLineCP );
          init( classpath );
          runWithFile( script, collectArgs( i + 1, args ) );
          break;
        }
      }
    }
    catch( Throwable t )
    {
      t.printStackTrace( System.err );
    }
  }

  private static void launchEditor() throws Exception
  {
    Class<?> cls = Class.forName( "editor.RunMe" );
    Method m = cls.getMethod( "launchEditor" );
    m.invoke( null );
  }

  private static List<String> collectArgs( int i, String[] args )
  {
    List<String> scriptArgs = new ArrayList<>();
    if( args != null )
    {
      while( i < args.length )
      {
        scriptArgs.add( args[i] );
        i++;
      }
    }
    return scriptArgs;
  }

  private static String extractClassPathFromSrc( String file )
  {
    BufferedReader br = null;
    String line;
    String ret = null;

    try
    {
      br = new BufferedReader( new FileReader( file ) );
      //noinspection StatementWithEmptyBody
      while( (line = br.readLine()).trim().isEmpty() )
      {
        ; //ignore
      }
      if( line.startsWith( "classpath" ) )
      {
        int b = line.indexOf( '"' );
        if( b != -1 )
        {
          int e = line.indexOf( '"', b + 1 );
          if( e != -1 )
          {
            ret = line.substring( b + 1, e );
          }
        }
      }
    }
    catch( IOException e )
    { //ignore
    }
    finally
    {
      try
      {
        if( br != null )
        {
          br.close();
        }
      }
      catch( IOException ex )
      { //ignore
      }
    }
    return ret;
  }

  private static List<File> makeClasspath( String cpValue, String scriptRoot, boolean cmdLineCP )
  {
    ArrayList<File> cp = new ArrayList<File>();
    if( cpValue != null )
    {
      StringTokenizer st = new StringTokenizer( cpValue, ",", false );
      while( st.hasMoreTokens() )
      {
        String s = st.nextToken();
        if( (s.contains( ":" ) && !OSPlatform.isWindows()) || s.contains( ";" ) )
        {
          for( StringTokenizer sysTok = new StringTokenizer( cpValue, File.pathSeparator, false ); sysTok.hasMoreTokens(); )
          {
            s = sysTok.nextToken();
            String pathname = cmdLineCP
                              ? s
                              : scriptRoot + File.separatorChar + s;
            cp.add( new File( pathname ) );
          }
        }
        else
        {
          String pathname = cmdLineCP ? s : scriptRoot + File.separatorChar + s;
          cp.add( new File( pathname ) );
        }
      }
    }
    return cp;
  }

  // Note this is a giant hack, we need to instead get the type name from the psiClass
  private static String makeFqn( File file )
  {
    String path = file.getAbsolutePath();
    int srcIndex = path.indexOf( "src" + File.separatorChar );
    if( srcIndex >= 0 )
    {
      String fqn = path.substring( srcIndex + 4 ).replace( File.separatorChar, '.' );
      return fqn.substring( 0, fqn.lastIndexOf( '.' ) );
    }
    else
    { // the Gosu Scratchpad case
      String fqn = file.getName();
      fqn = NOPACKAGE + '.' + fqn.substring( 0, fqn.lastIndexOf( '.' ) ).replace( " ", "" );
      return fqn;
    }
  }


  public static void setClasspath( List<File> classpath )
  {
    classpath = new ArrayList<>( classpath );
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
          //## todo:
          //## Call URL.set( xxx ) so we can set the overwrite the previous ../src path, otherwise these just accumulate with every call to setClasspath() (this method)

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

  public static List<File> getClasspath()
  {
    return _classpath;
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
    for( int i = classpath.size() - 1; i >= 0; i-- )
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
    List<File> combined = new ArrayList<>();
    if( classpath != null )
    {
      combined.addAll( classpath );
    }
    combined.addAll( deriveClasspathFrom( Gosu.class ) );
    setClasspath( combined );
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
    System.out.println( "Gosu version: " + getVersion() +
                        "\nUsage:\n" +
                        "    gosu [-checkedArithmetic] [-classpath 'entry1,entry2...'] program.gsp [args...]\n" +
                        "    gosu [-checkedArithmetic] [-classpath 'entry1,entry2...'] -e 'inline script' [args...]\n" );
    System.exit( 1 );
  }

  public static List<File> deriveClasspathFrom( Class clazz )
  {
    List<File> ll = new LinkedList<>();
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
    addBootstrapClasses( ll );
    return ll;
  }

  private static void addBootstrapClasses( List<File> ll )
  {
    try
    {
      Method m;
      try
      {
        m = ClassLoader.class.getDeclaredMethod( "getBootstrapClassPath" );
      }
      catch( NoSuchMethodException nsme )
      {
        // The VM that does not define getBootstrapClassPath() seems to be the IBM VM (v. 8).
        getBootstrapForIbm( ll );
        return;
      }
      m.setAccessible( true );
      URLClassPath bootstrapClassPath = (URLClassPath)m.invoke( null );
      for( URL url : bootstrapClassPath.getURLs() )
      {
        try
        {
          File file = new File( url.toURI() );
          if( file.exists() && !ll.contains( file ) )
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
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private static void getBootstrapForIbm( List<File> ll )
  {
    List<String> ibmClasspath = GosucUtil.getJreJars();
    ibmClasspath.stream().forEach( e -> ll.add( new File( e ) ) );
  }

  public static GosuVersion getVersion()
  {
    InputStream in = Gosu.class.getClassLoader().getResourceAsStream( GosuVersion.RESOURCE_PATH );
    if( in == null )
    {
      return new GosuVersion( 0, 0 );
    }
    Reader reader = StreamUtil.getInputStreamReader( in );
    return GosuVersion.parse( reader );
  }

  public static File getCurrentProgram()
  {
    return _script;
  }

  public static List<String> getRawArgs()
  {
    return _rawArgs;
  }

  public static void setRawArgs( String[] args )
  {
    _rawArgs = collectArgs( 1, args );
  }

  private static int runWithType( String fqn, List<String> args ) throws IOException, ParseResultsException
  {
     // set remaining arguments as arguments to the Gosu program
    _rawArgs = args;
    IGosuProgram program = (IGosuProgram)TypeSystem.getByFullName( fqn );
    program.getProgramInstance().evaluate( null );
    return 0;
  }

  private static void runWithFile( File script, List<String> args ) throws IOException, ParseResultsException
  {
    _script = script;
    // set remaining arguments as arguments to the Gosu program
    _rawArgs = args;
    byte[] bytes = StreamUtil.getContent( new BufferedInputStream( new FileInputStream( script ) ) );
    String content = StreamUtil.toString( bytes );
    IFileContext ctx = new ProgramFileContext( script, makeFqn( script ) );
    IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
    ParserOptions options = new ParserOptions().withFileContext( ctx );
    IParseResult result = programParser.parseExpressionOrProgram( content, new StandardSymbolTable( true ), options );
    IGosuProgram program = result.getProgram();
    Object ret = program.getProgramInstance().evaluate( null ); // evaluate it
    IType expressionType = result.getType();
    if( expressionType != null && !JavaTypes.pVOID().equals( expressionType ) )
    {
      GosuShop.print( ret );
    }
  }

  private static void runWithInlineScript( String script, List<String> args ) throws IOException, ParseResultsException
  {
    _script = null;
    // set remaining arguments as arguments to the Gosu program
    _rawArgs = args;
    IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
    IParseResult result = programParser.parseExpressionOrProgram( script, new StandardSymbolTable( true ), new ParserOptions() );
    IGosuProgram program = result.getProgram();
    Object ret = program.getProgramInstance().evaluate( null ); // evaluate it
    IType expressionType = result.getType();
    if( expressionType != null && !JavaTypes.pVOID().equals( expressionType ) )
    {
      GosuShop.print( ret );
    }
  }
}
