/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang;

import gw.config.CommonServices;
import gw.fs.IDirectory;
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
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.OSPlatform;
import gw.util.PathUtil;
import gw.util.StreamUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import gw.util.concurrent.LocklessLazyVar;
import manifold.util.JreUtil;
import manifold.util.NecessaryEvilUtil;
import manifold.util.ReflectUtil;
import sun.misc.URLClassPath;

public class Gosu
{
  private static final LocklessLazyVar<Class<?>> BUILTIN_CLASSLOADER =
    LocklessLazyVar.make( () -> ReflectUtil.type( "jdk.internal.loader.BuiltinClassLoader" ) );
  private static final LocklessLazyVar<Class<?>> URL_CLASS_PATH =
    LocklessLazyVar.make( () ->
                            JreUtil.isJava8()
                            ? ReflectUtil.type( "sun.misc.URLClassPath" )
                            : ReflectUtil.type( "jdk.internal.loader.URLClassPath" ) );
  /**
   * used as a virtual package e.g., for scratchpad
   */
  public static final String NOPACKAGE = "_nopackage_";
  public static final String GOSU_SCRATCHPAD_FQN = NOPACKAGE + ".GosuScratchpad";
  public static final String JAR_REPO_DIR = "JAR-REPO";     //!! if you change this, also change it in Launcher
  public static final String JAR_REPO_TXT = "jar-repo.txt"; //!! "
  public static final String FAILED = "  FAILED: ";
  public static final String SUCCESS = "  SUCCESS ";

  private static List<IDirectory> _classpath;
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
          List<IDirectory> classpath = makeClasspath( cpValue, "", cmdLineCP );
          init( classpath );
          runWithType( args[i + 1], collectArgs( i + 2, args ) );
          break;
        }

        case "-e":
        {
          List<IDirectory> classpath = makeClasspath( cpValue, "", cmdLineCP );
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
          List<IDirectory> classpath = makeClasspath( cpValue, script.getAbsoluteFile().getParent(), cmdLineCP );
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

  private static List<IDirectory> makeClasspath( String cpValue, String scriptRoot, boolean cmdLineCP )
  {
    ArrayList<IDirectory> cp = new ArrayList<>();
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
            cp.add( CommonServices.getFileSystem().getIDirectory( new File( pathname ) ) );
          }
        }
        else
        {
          String pathname = cmdLineCP ? s : scriptRoot + File.separatorChar + s;
          cp.add( CommonServices.getFileSystem().getIDirectory( new File( pathname ) ) );
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


  public static void setClasspath( List<IDirectory> classpath )
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
      Method addURL = getAddUrlMethod();
      for( IDirectory entry : classpath )
      {
        try
        {
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

  private static Method getAddUrlMethod()
  {
    Method addURL;
    try
    {
      addURL = URLClassLoader.class.getDeclaredMethod( "addURL", URL.class );
      addURL.setAccessible( true );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return addURL;
  }

  public static List<IDirectory> getClasspath()
  {
    return _classpath;
  }

  private static void reinitGosu( List<IDirectory> classpath )
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

  private static void removeDups( List<IDirectory> classpath )
  {
    for( int i = classpath.size() - 1; i >= 0; i-- )
    {
      IDirectory f = classpath.get( i );
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

  public static void init( List<IDirectory> classpath )
  {
    NecessaryEvilUtil.bypassJava9Security();
    List<IDirectory> combined = new ArrayList<>();
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

  public static List<IDirectory> deriveClasspathFrom( Class clazz )
  {
    if( JreUtil.isJava8() )
    {
      return deriveClasspathFrom_Java8( clazz );
    }
    return deriveClasspathFrom_Java9( clazz );
  }

  public static List<IDirectory> deriveClasspathFrom_Java8( Class clazz )
  {
    List<IDirectory> ll = new LinkedList<>();
    ClassLoader loader = clazz.getClassLoader();
    while( loader != null )
    {
      if( loader instanceof URLClassLoader )
      {
        for( URL url : ((URLClassLoader)loader).getURLs() )
        {
          try
          {
            IDirectory file = CommonServices.getFileSystem().getIDirectory( Paths.get( url.toURI() ) );
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
  public static List<IDirectory> deriveClasspathFrom_Java9( Class clazz )
  {
    List<IDirectory> ll = new ArrayList<>();
    ClassLoader loader = clazz.getClassLoader();
    if( loader != null && BUILTIN_CLASSLOADER.get().isAssignableFrom( loader.getClass() ) )
    {
      URLClassPath ucp = (URLClassPath)manifold.util.ReflectUtil.field( loader, "ucp" ).get();
      if( ucp != null )
      {
        for( URL url: ucp.getURLs() )
        {
          try
          {
            IDirectory file = CommonServices.getFileSystem().getIDirectory( Paths.get( url.toURI() ) );
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
    }
    return ll;
  }

  private static void addBootstrapClasses( List<IDirectory> ll )
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
          IDirectory file = CommonServices.getFileSystem().getIDirectory( Paths.get( url.toURI() ) );
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

  private static void getBootstrapForIbm( List<IDirectory> ll )
  {
    List<String> ibmClasspath = GosucUtil.getJreJars();
    ibmClasspath.forEach( e -> ll.add( CommonServices.getFileSystem().getIDirectory( Paths.get( e ) ) ) );
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
    _rawArgs = collectArgs( 0, args );
  }

  private static int runWithType( String fqn, List<String> args ) throws Exception
  {
    // set remaining arguments as arguments to the Gosu program
    _rawArgs = args;
    IType type = TypeSystem.getByFullName( fqn );
    if( type instanceof IGosuProgram )
    {
      Object result = ((IGosuProgram)type).getProgramInstance().evaluate( null );
      if( result != null )
      {
        System.out.println( result );
      }
    }
    else
    {
      IMethodInfo mainMethod = hasStaticMain( type );
      if( mainMethod != null )
      {
        gw.lang.reflect.ReflectUtil.invokeStaticMethod( type.getName(), "main", new Object[]{new String[]{}} );
      }
      else if( type instanceof IGosuClass )
      {
        runTest( (IGosuClass)type );
      }
      else
      {
        throw new UnsupportedOperationException( "Don't know how to run: " + fqn );
      }
    }
    return 0;
  }

  public static void runTest( IGosuClass gsType ) throws Exception
  {
    Class cls = gsType.getBackingClass();
    runNamedOrAnnotatedMethod( cls.newInstance(), "beforeClass", "org.junit.BeforeClass" );
    for( Method m : cls.getMethods() )
    {
      if( isTestMethod( m ) )
      {
        Object instance = cls.newInstance();
        runNamedOrAnnotatedMethod( instance, "beforeMethod", "org.junit.Before" );
        try
        {
          System.out.println( "- " + m.getName() );
          m.invoke( instance );
          System.out.println( SUCCESS );
        }
        catch( InvocationTargetException e )
        {
          //noinspection ThrowableResultOfMethodCallIgnored
          Throwable cause = GosuExceptionUtil.findExceptionCause( e );
          if( cause instanceof AssertionError )
          {
            System.out.println( FAILED + cause.getClass().getSimpleName() + " : " + cause.getMessage() );
            String lines = findPertinentLines( gsType, cause );
            System.out.println( lines );
          }
          else
          {
            throw GosuExceptionUtil.forceThrow( cause );
          }
        }
        finally
        {
          runNamedOrAnnotatedMethod( instance, "afterMethod", "org.junit.After" );
          System.out.println();
        }
      }
    }
    runNamedOrAnnotatedMethod( cls.newInstance(), "afterClass", "org.junit.AfterClass" );
    System.out.println();
  }

  private static boolean isTestMethod( Method m ) throws Exception
  {
    int modifiers = m.getModifiers();
    return Modifier.isPublic( modifiers ) &&
           (m.getName().startsWith( "test" ) || hasAnnotation( m, "org.junit.Test" )) &&
           m.getParameters().length == 0;
  }

  private static void runNamedOrAnnotatedMethod( Object instance, String methodName, String annoName ) throws Exception
  {
    for( Method m : instance.getClass().getMethods() )
    {
      if( m.getName().equals( methodName ) )
      {
        m.invoke( instance );
        return;
      }
      for( Annotation anno : m.getAnnotations() )
      {
        if( anno.annotationType().getName().equals( annoName ) )
        {
          m.invoke( instance );
          return;
        }
      }
    }
  }

  private static boolean hasAnnotation( Method m, String name ) throws Exception
  {
    for( Annotation anno : m.getAnnotations() )
    {
      if( anno.annotationType().getName().equals( name ) )
      {
        return true;
      }
    }
    return false;
  }

  private static IMethodInfo hasStaticMain( IType type )
  {
    IMethodInfo main = type.getTypeInfo().getMethod( "main", JavaTypes.STRING().getArrayType() );
    if( main != null && main.isStatic() && main.getReturnType() == JavaTypes.pVOID() )
    {
      return main;
    }
    return null;
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

  private static String findPertinentLines( IGosuClass gsType, Throwable cause )
  {
    StringBuilder sb = new StringBuilder();
    StackTraceElement[] trace = cause.getStackTrace();
    for( int i = 0; i < trace.length; i++ )
    {
      StackTraceElement elem = trace[i];
      if( elem.getClassName().equals( gsType.getName() ) )
      {
        sb.append( "     at " ).append( elem.toString() ).append( "\n" );
      }
    }
    return sb.toString();
  }

  public static List<String> findJreSources()
  {
    List<String> sources = new ArrayList<>();
    Arrays.asList( "src.zip", "javafx-src.zip" ).forEach( fileName -> findJreSourcePath( sources, fileName ) );
    return sources;
  }

  private static void findJreSourcePath( List<String> sources, String archiveName )
  {
    String javaHomePath = System.getProperty( "java.home" );
    Path javaDir = PathUtil.create( javaHomePath );
    if( PathUtil.getName( javaDir ).equalsIgnoreCase( "jre" ) )
    {
      javaDir = javaDir.getParent();
    }
    Path srcZip = PathUtil.create( javaDir, archiveName );
    if( PathUtil.isFile( srcZip ) )
    {
      sources.add( srcZip.toString() );
    }
    else
    {
      String javaDirName = PathUtil.getName( javaDir );
      if( javaDirName.startsWith( "jre" ) )
      {
        javaDirName = javaDirName.replace( "jre", "jdk" );
        Path jdkSrc = PathUtil.create( javaDirName, archiveName );
        if( PathUtil.isFile( jdkSrc ) )
        {
          sources.add( jdkSrc.toString() );
        }
      }
    }
  }
}
