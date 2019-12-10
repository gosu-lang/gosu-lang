/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.AdditionalDirectory;
import gw.fs.IDirectory;
import gw.fs.IncludeModuleDirectory;
import gw.internal.gosu.module.DefaultSingleModule;
import gw.internal.gosu.module.GlobalModule;
import gw.lang.Gosu;
import gw.lang.ProgramFileContext;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.GosucUtil;
import gw.lang.init.GosuPathEntry;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParseResult;
import gw.lang.parser.ParserOptions;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IProject;
import gw.util.GosuExceptionUtil;
import gw.util.OSPlatform;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import manifold.internal.runtime.Bootstrap;
import manifold.util.JreUtil;
import manifold.util.NecessaryEvilUtil;

public class ExecutionEnvironment implements IExecutionEnvironment
{
  private static ExecutionEnvironment INSTANCE;
  public static final String CLASS_REDEFINER_THREAD = "Gosu class redefiner";

  /**
   * "Special" java classes that will be used to locate "special" JARs which should be
   * included in the classpath. This is a replacement to old SPECIAL_FILES variable.
   */
  private static final List<String> SPECIAL_CLASSES = Arrays.asList(
    "javax.servlet.Servlet",
    "javax.servlet.http.HttpServletRequest"
  );

  private IProject _project;
  private DefaultSingleModule _module;
  private String[] _discretePackages;
  private TypeSystemState _state = TypeSystemState.STOPPED;

  public static ExecutionEnvironment instance()
  {
    if( INSTANCE == null || INSTANCE.getProject().isDisposed() )
    {
      INSTANCE = new ExecutionEnvironment( new DefaultSingleModuleRuntimeProject() );
    }
    return INSTANCE;
  }

  private ExecutionEnvironment( IProject project )
  {
    _project = project;
  }

  public IProject getProject()
  {
    return _project;
  }

  public void initializeDefaultSingleModule( List<? extends GosuPathEntry> pathEntries, List<IDirectory> backingSourceEntries, String... discretePackages )
  {
    _state = TypeSystemState.STARTING;
    try
    {
      NecessaryEvilUtil.bypassJava9Security();
      DefaultSingleModule singleModule = _module == null ? new DefaultSingleModule( this ) : _module;
      List<IDirectory> allSources = new ArrayList<>();
      for( GosuPathEntry pathEntry: pathEntries )
      {
        IDirectory root = pathEntry.getRoot();
        for( IDirectory dir: pathEntry.getSources() )
        {
          IDirectory srcDir;
          if( root instanceof IncludeModuleDirectory )
          {
            srcDir = new IncludeModuleDirectory( dir );
          }
          else if( root.isAdditional() )
          {
            srcDir = new AdditionalDirectory( dir );
          }
          else
          {
            srcDir = dir;
          }
          allSources.add( srcDir );
        }
        allSources.addAll( pathEntry.getSources() );
      }
      singleModule.configurePaths( createDefaultClassPath(), allSources, backingSourceEntries );
      setDiscretePackages( discretePackages );
      _module = singleModule;
      singleModule.initializeTypeLoaders();
      CommonServices.getCoercionManager().init();

      startSneakyDebugThread();
    }
    finally
    {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeDefaultSingleModule()
  {
    _state = TypeSystemState.STOPPING;
    try
    {
      if( _module != null )
      {
        DefaultSingleModule m = _module;
        m.getModuleTypeLoader().uninitializeTypeLoaders();
        m.getModuleTypeLoader().reset();
        m.configurePaths( Collections.emptyList(), Collections.emptyList(), Collections.emptyList() );
      }

    }
    finally
    {
      _state = TypeSystemState.STOPPED;
    }
  }

  public void initializeCompiler( GosucModule gosucModule )
  {
    _state = TypeSystemState.STARTING;
    try
    {
//      DefaultPlatformHelper.DISABLE_COMPILE_TIME_ANNOTATION = true;

      DefaultSingleModule module = new DefaultSingleModule( this, gosucModule.getName() );
      module.setNativeModule( gosucModule );
      module.configurePaths( GosucUtil.toDirectories( gosucModule.getClasspath() ),
        GosucUtil.toDirectories( gosucModule.getAllSourceRoots() ),
        GosucUtil.toDirectories( gosucModule.getBackingSourcePath() ) );
      _module = module;
      module.initializeTypeLoaders();
      CommonServices.getEntityAccess().init();

      FrequentUsedJavaTypeCache.instance().init();
    }
    finally
    {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeCompiler()
  {
    _state = TypeSystemState.STOPPING;
    try
    {
      if( _module != null )
      {
        GlobalModule m = _module;
        m.getModuleTypeLoader().uninitializeTypeLoaders();
        m.getModuleTypeLoader().reset();
        m.configurePaths( Collections.emptyList(), Collections.emptyList(), Collections.emptyList() );

        Bootstrap.cleanup();
      }
    }
    finally
    {
      _state = TypeSystemState.STOPPED;
    }
  }

  public void initializeSimpleIde( GosucModule gosucModule )
  {
    _state = TypeSystemState.STARTING;
    try
    {
      DefaultSingleModule module = _module == null ? new DefaultSingleModule( this ) : _module;
      module.setNativeModule( gosucModule );
      module.configurePaths( GosucUtil.toDirectories( gosucModule.getClasspath() ),
        GosucUtil.toDirectories( gosucModule.getAllSourceRoots() ),
        GosucUtil.toDirectories( gosucModule.getBackingSourcePath() ) );
      _module = module;
      module.initializeTypeLoaders();
      CommonServices.getCoercionManager().init();
    }
    finally
    {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeSimpleIde()
  {
    _state = TypeSystemState.STOPPING;
    try
    {
      if( _module != null )
      {
        DefaultSingleModule m = _module;
        m.getModuleTypeLoader().uninitializeTypeLoaders();
        m.getModuleTypeLoader().reset();
        m.configurePaths( Collections.emptyList(), Collections.emptyList(), Collections.emptyList() );
      }
    }
    finally
    {
      _state = TypeSystemState.STOPPED;
    }
  }

  public String[] getDiscretePackages()
  {
    return _discretePackages;
  }

  public void setDiscretePackages( String[] discretePackages )
  {
    List<String> packages = new ArrayList<>();
    String fromCmdLine = System.getProperty( "unloadable.packages" );
    if( fromCmdLine != null && !fromCmdLine.isEmpty() )
    {
      packages.addAll( Arrays.asList( fromCmdLine.split( "," ) ) );
    }
    if( discretePackages != null )
    {
      packages.addAll( Arrays.asList( discretePackages ) );
    }
    _discretePackages = packages.toArray( new String[0] );

    for( int i = 0; i < _discretePackages.length; i++ )
    {
      for( int j = i + 1; j < _discretePackages.length; i++ )
      {
        if( _discretePackages[i].startsWith( _discretePackages[j] ) ||
            _discretePackages[j].startsWith( _discretePackages[i] ) )
        {
          throw new IllegalStateException( "Unloadable packages overlap: " + _discretePackages[i] + ", " + _discretePackages[j] );
        }
      }
    }
  }

  public DefaultSingleModule getModule()
  {
    return _module;
  }

  public TypeSystemState getState()
  {
    return _state;
  }

  public void shutdown()
  {
    _module.getModuleTypeLoader().shutdown();
  }

  private static class DefaultSingleModuleRuntimeProject implements IProject
  {
    @Override
    public String getName()
    {
      return getClass().getSimpleName();
    }

    @Override
    public Object getNativeProject()
    {
      return this;
    }

    @Override
    public boolean isDisposed()
    {
      return false;
    }

    @Override
    public String toString()
    {
      return "Default Single Runtime Execution Environment";
    }

    @Override
    public boolean isHeadless()
    {
      return false;
    }

    @Override
    public boolean isShadowMode()
    {
      return false;
    }
  }

  /**
   * Detect whether or not the jdwp agent is alive in this process, if so start
   * a thread that wakes up every N seconds and checks to see if the ReloadClassesIndicator
   * Java class has been redefined by a debugger.  If so, it reloads Gosu classes
   * that have changed.
   * <p>
   * Why, you ask?  Well since Gosu classes are not compiled to disk, the IDE hosting
   * Gosu can't simply send the bytes in a conventional JDI redefineClasses() call.
   * Yet it somehow needs to at least inform Gosu's type system in the target process
   * that Gosu classes have changed.  The JVMTI doesn't offer much help; there's no
   * way to field an arbitrary call from the JDWP client, or for the client to send an
   * arbitrary message.  Nor is it possible to leverage the JVMTI's ability to handle
   * method invocation etc. because the target thread must be suspended at a
   * breakpoint, which is not necessarily the case during compilation, and certainly
   * isn't the case for a thread dedicated to fielding such a call.  What to do?
   * <p>
   * We can leverage redefineClasses() after all.  The idea is for the IDE compiler
   * to redefine a class (via asm) designated as the "ReloadClassIndicator".  This class lives
   * inside Gosu's type system.  It has a single method: public static long timestamp()
   * and returns a literal value.  If the target process is being debugged (jdwp
   * agent detection), a thread in the target process starts immediately and waits a
   * few seconds before calling the timestamp() method, it does this in a forever loop.
   * If the timestamp value changes, we assume the IDE redefined the class with a new
   * value to indicate classes have changed.  In turn we find and reload changed
   * classes.  What could be more straight forward?
   * <p>
   * An alternative approach would be for the IDE to establish an additional line
   * of communication with the target process e.g., socket, memory, whatever.  But
   * that is messy (requires config on user's end) and error prone.  One debug
   * socket is plenty.
   * <p>
   * Improvements to this strategy include supplying not only an indication that stuff
   * has changed, but also the names of the classes that have changed.  This would
   * releive the target process from having to keep track timestamps on all loaded
   * classes. This could be implemented by having the class return an array of names.
   * An even better improvement would be to include not just the names, but also the
   * source of the classes.  This would enable the debuger to modify in memory the classes
   * during a remote debugging session.
   */

  private int counter;

  private synchronized int getCounter()
  {
    return counter++;
  }

  private void startSneakyDebugThread()
  {
    //noinspection ConstantConditions
    if( !BytecodeOptions.JDWP_ENABLED.get() )
    {
      return;
    }
    Thread sneakyDebugThread =
      new Thread(
        new Runnable()
        {
          public synchronized void run()
          {
            long timestamp = ReloadClassesIndicator.timestamp();
            long now = 0;
            while( getState() != TypeSystemState.STOPPED )
            {
              try
              {
                wait( 2000 );
                now = ReloadClassesIndicator.timestamp();
                if( now > timestamp )
                {
                  String script = ReloadClassesIndicator.getScript();
                  if( script != null && script.length() > 0 )
                  {
                    runScript( script );
                  }
                  else
                  {
                    refreshTypes();
                  }
                }
              }
              catch( Exception e )
              {
                e.printStackTrace();
              }
              finally
              {
                timestamp = now;
              }
            }
          }

          private void refreshTypes()
          {
            String[] types = ReloadClassesIndicator.changedTypes();
            System.out.println( "Refreshing " + types.length + " types at " + new Date() );
            if( types.length > 0 )
            {
              for( String name: types )
              {
                IType type = TypeSystem.getByFullNameIfValid( name );
                if( type != null )
                {
                  TypeSystem.refresh( (ITypeRef)type );
                  // Also update enhancement index if type is an enhancement
                  if( type instanceof IGosuEnhancementInternal )
                  {
                    ((GosuClassTypeLoader)type.getTypeLoader()).getEnhancementIndex().addEntry(
                      ((IGosuEnhancementInternal)type).getEnhancedType(), (IGosuEnhancementInternal)type );
                  }
                }
              }
            }
            CommonServices.getEntityAccess().reloadedTypes( types );
          }

          private void runScript( String strScript )
          {
            String[] result = evaluate( strScript );
            if( result[0] != null && result[0].length() > 0 )
            {
              System.out.print( result[0] );
            }
            if( result[1] != null && result[1].length() > 0 )
            {
              System.err.print( result[1] );
            }
          }

          public String[] evaluate( String strScript )
          {
            IGosuParser scriptParser = GosuParserFactory.createParser( strScript );

            try
            {
              IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
              String qualifiedName = ReloadClassesIndicator.getQualifiedName();
              if( qualifiedName == null )
              {
                qualifiedName = Gosu.GOSU_SCRATCHPAD_FQN + getCounter();
              }
              ParserOptions options = new ParserOptions()
                .withParser( scriptParser )
                .asThrowawayProgram()
                .withFileContext( new ProgramFileContext( null, qualifiedName ) );
              IParseResult parseResult = programParser.parseExpressionOrProgram( strScript, scriptParser.getSymbolTable(), options );
              Object result = parseResult.getProgram().evaluate( null );
              if( result != null )
              {
                System.out.println( "Return Value: " + CommonServices.getCoercionManager().convertValue( result, JavaTypes.STRING() ) );
              }
            }
            catch( Exception e )
            {
              Throwable t = e;
              while( t != null )
              {
                t = t.getCause();
              }
              e.printStackTrace();
            }
            return new String[]{null, null};
          }
        }, CLASS_REDEFINER_THREAD );
    sneakyDebugThread.setDaemon( true );
    sneakyDebugThread.start();
  }

  public static List<IDirectory> createDefaultClassPath()
  {
    List<String> vals = new ArrayList<>();
    vals.add( CommonServices.getEntityAccess().getPluginRepositories().toString() );
    vals.add( removeAllQuotes( System.getProperty( "java.class.path", "" ) ) );
    vals.add( CommonServices.getEntityAccess().getWebServerPaths() );
    vals.addAll( getJarsContainingSpecialClasses() );
    if( JreUtil.isJava8() )
    {
      vals.add( System.getProperty( "sun.boot.class.path", "" ) );
      vals.add( System.getProperty( "java.ext.dirs", "" ) );
    }
    List<IDirectory> dirs = expand( vals );
    return dirs;
  }

  private static String removeAllQuotes( String classpath )
  {
    return classpath.replace( "\"", "" );
  }

  private static List<IDirectory> expand( List<String> paths )
  {
    LinkedHashSet<IDirectory> expanded = new LinkedHashSet<>();
    for( String path: paths )
    {
      for( String pathElement: path.split( File.pathSeparator ) )
      {
        if( OSPlatform.isWindows() )
        {
          // correct paths with illegal leading separator e.g., "\C:\foo\bar"
          pathElement = pathElement.replace( '/', File.separatorChar );
          if( pathElement.startsWith( File.separator ) )
          {
            pathElement = pathElement.substring( 1 );
          }
        }

        if( pathElement.length() > 0 )
        {
          Path filePath = Paths.get( pathElement );
          IDirectory resource = CommonServices.getFileSystem().getIDirectory( filePath );
          expanded.add( resource );
        }
      }
    }
    return new ArrayList<>( expanded );
  }

  @Override
  public boolean isShadowingMode()
  {
    return _project.isShadowMode();
  }

  /**
   * This method is a hack to resolve "special" system-like classes provided by execution environment.
   * This is the replacement of old addSpecialJars() method
   */
  private static Set<String> getJarsContainingSpecialClasses()
  {
    Set<String> paths = new HashSet<>();
    for( String className: SPECIAL_CLASSES )
    {
      //getLogger().debug("Searching JAR that provides " + className + ".");
      Class<?> clazz;
      try
      {
        clazz = Class.forName( className );
      }
      catch( ClassNotFoundException e )
      {
        if( !ILanguageLevel.Util.STANDARD_GOSU() )
        {
          System.err.println( "Class " + className
                              + " could not be found. Gosu code might fail to compile at runtime." );
        }
        continue;
      }
      CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
      if( codeSource == null )
      {
        if( !ILanguageLevel.Util.STANDARD_GOSU() )
        {
          System.err.println( "Code source for " + clazz.getName()
                              + " is null. Gosu code might fail to compile at runtime." );
        }
        continue;
      }
      // url might be jar:<url>!/, e.g. jar:file:/gitmo/jboss-5.1.2/common/lib/servlet-api.jar!/
      // or vfszip:<url> on JBoss
      // or wsjar:<url> on WebSphere
      URL jarUrl = codeSource.getLocation();

      String path;
      if( "file".equals( jarUrl.getProtocol() ) )
      {
        try
        {
          path = new File( jarUrl.toURI() ).getAbsolutePath();
        }
        catch( URISyntaxException e )
        {
          throw new RuntimeException( e );
        }
      }
      else
      {
        // in case of complex URL the path might be like this: "file:/gitmo/jboss-5.1.2/common/lib/servlet-api.jar!/"
        path = jarUrl.getPath();

        // So removing optional "!/" suffix and "file:" prefix
        if( path.endsWith( "/" ) )
        {
          path = path.substring( 0, path.length() - 1 );
        }
        if( path.endsWith( "!" ) )
        {
          path = path.substring( 0, path.length() - 1 );
        }
        if( path.startsWith( "file:" ) )
        {
          path = path.substring( "file:".length() );
        }
      }

      // URLDecoder.decode() decodes string from application/x-www-form-urlencoded MIME format
      // while we need to decode from RFC2396 format.
      // I think the only difference between formats that application/x-www-form-urlencoded decodes "+"
      // to space while RFC2396 does not.
      // So before using URLDecoder.decode() encode "+" to its ASCII representation
      // that will be decoded back to "+" by URLDecoder.decode()
      path = path.replaceAll( "\\+", "%2B" );
      try
      {
        String decodedPath = URLDecoder.decode( path, "UTF-8" );
        if( new File( decodedPath ).exists() )
        {
          paths.add( path );
        }
        else
        {
          System.err.println( "Could not extract filesystem path from the url " + jarUrl.getPath()
                              + ". Gosu code that requires classes from that JAR might fail to compile at runtime." );
        }
      }
      catch( UnsupportedEncodingException ex )
      {
        // impossible
        throw GosuExceptionUtil.forceThrow( ex );
      }
    }
    return paths;
  }
}
