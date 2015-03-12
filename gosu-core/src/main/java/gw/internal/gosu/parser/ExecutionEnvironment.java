/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.fs.IResource;
import gw.fs.IncludeModuleDirectory;
import gw.internal.gosu.module.DefaultSingleModule;
import gw.internal.gosu.module.GlobalModule;
import gw.internal.gosu.module.JreModule;
import gw.internal.gosu.module.Module;
import gw.fs.AdditionalDirectory;
import gw.lang.cli.SystemExitIgnoredException;
import gw.lang.gosuc.GosucModule;
import gw.lang.gosuc.GosucProject;
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
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.IProject;
import gw.util.GosuExceptionUtil;
import gw.util.ILogger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class ExecutionEnvironment implements IExecutionEnvironment
{
  private static final IProject DEFAULT_PROJECT = new DefaultSingleModuleRuntimeProject();
  private static final Map<Object, ExecutionEnvironment> INSTANCES = new WeakHashMap<Object, ExecutionEnvironment>();
  private static ExecutionEnvironment THE_ONE;
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
  private List<IModule> _modules;
  private IModule _defaultModule;
  private IModule _jreModule;
  private IModule _rootModule;
  private TypeSystemState _state = TypeSystemState.STOPPED;

  public static ExecutionEnvironment instance()
  {
    if( INSTANCES.size() == 1 )
    {
      return THE_ONE == null
             ? THE_ONE = INSTANCES.values().iterator().next()
             : THE_ONE;
    }

    IModule mod = INSTANCES.size() > 0 ? TypeSystem.getCurrentModule() : null;
    if( mod != null )
    {
      ExecutionEnvironment execEnv = (ExecutionEnvironment)mod.getExecutionEnvironment();
      if( execEnv == null )
      {
        throw new IllegalStateException( "Module, " + mod.getName() + ", has a null execution environment. This is bad." );
      }
      return execEnv;
    }

    if( INSTANCES.size() > 0 )
    {
      // Return first non-default project
      // Yes, this is a guess, but we need to guess for the case where we're running tests
      // and loading classes in lots of threads where the current module is not pushed
      for( ExecutionEnvironment execEnv: INSTANCES.values() )
      {
        if( execEnv.getProject() != DEFAULT_PROJECT &&
            !execEnv.getProject().isDisposed() )
        {
          return execEnv;
        }
      }
    }

    return instance( DEFAULT_PROJECT );
  }
  public static ExecutionEnvironment instance( IProject project )
  {
    if( project == null )
    {
      throw new IllegalStateException( "Project must not be null" );
    }

    if( project instanceof IExecutionEnvironment )
    {
      throw new RuntimeException( "Passed in ExecutionEnvironment as project" );
    }

    ExecutionEnvironment execEnv = INSTANCES.get( project );
    if( execEnv == null )
    {
      INSTANCES.put( project, execEnv = new ExecutionEnvironment( project ) );
    }

    return execEnv;
  }

  public static Collection<? extends IExecutionEnvironment> getAll()
  {
    return INSTANCES.values();
  }

  private ExecutionEnvironment( IProject project )
  {
    _project = project;
    _modules = new ArrayList<IModule>();
  }

  public IProject getProject()
  {
    return _project;
  }

  public List<? extends IModule> getModules() {
    return _modules;
  }

  public void initializeDefaultSingleModule( List<? extends GosuPathEntry> pathEntries ) {
    _state = TypeSystemState.STARTING;
    try {
      DefaultSingleModule singleModule = _defaultModule == null ? new DefaultSingleModule( this ) : (DefaultSingleModule)_defaultModule;
      List<IDirectory> allSources = new ArrayList<IDirectory>();
      List<IDirectory> allRoots = new ArrayList<IDirectory>();
      for( GosuPathEntry pathEntry : pathEntries )
      {
        IDirectory root = pathEntry.getRoot();
        allRoots.add(root);
        for( IDirectory dir: pathEntry.getSources() ) {
          IDirectory srcDir;
          if( root instanceof IncludeModuleDirectory ) {
            srcDir = new IncludeModuleDirectory( dir );
          }
          else if( root.isAdditional() ) {
            srcDir = new AdditionalDirectory( dir );
          }
          else {
            srcDir = dir;
          }
          allSources.add( srcDir );
        }
        allSources.addAll(pathEntry.getSources());
      }
      singleModule.configurePaths(createDefaultClassPath(), allSources);
      singleModule.setRoots(allRoots);
      _defaultModule = singleModule;
      _modules = new ArrayList<IModule>(Collections.singletonList(singleModule));

//      pushModule(singleModule); // Push and leave pushed (in this thread)
      singleModule.initializeTypeLoaders();
      CommonServices.getCoercionManager().init();

      startSneakyDebugThread();
    } finally {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeDefaultSingleModule() {
    _state = TypeSystemState.STOPPING;
    try {
      if (_defaultModule != null) {
        DefaultSingleModule m = (DefaultSingleModule) _defaultModule;
        m.getModuleTypeLoader().uninitializeTypeLoaders();
        m.getModuleTypeLoader().reset();
        m.setRoots(Collections.<IDirectory>emptyList());
        m.configurePaths(Collections.<IDirectory>emptyList(), Collections.<IDirectory>emptyList());
      }
      _modules.clear();

    } finally {
      _state = TypeSystemState.STOPPED;
    }
  }

  public void initializeMultipleModules(List<? extends IModule> modules) {
    _state = TypeSystemState.STARTING;
    try {
      // noinspection unchecked
      _defaultModule = null;
      _rootModule = null;
      _modules = (List<IModule>) modules;

      for (IModule module : modules) {
        TypeSystem.pushModule(module);
        try {
          ((Module) module).initializeTypeLoaders();
        } finally {
          TypeSystem.popModule(module);
        }
      }

      CommonServices.getCoercionManager().init();

      FrequentUsedJavaTypeCache.instance( this ).init();
    } finally {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeMultipleModules() {
    _state = TypeSystemState.STOPPING;
    try {
      TypeSystem.shutdown( this);

      for (IModule module : _modules) {
        ((Module) module).getModuleTypeLoader().uninitializeTypeLoaders();
      }

      _jreModule = null;
      _rootModule = null;

      _modules.clear();
    } finally {
      _state = TypeSystemState.STOPPED;
    }
  }

  public void addModule(IModule module) {
    checkForDuplicates(module.getName());
    // noinspection unchecked
    _modules.add(module);

    TypeSystem.pushModule(module);
    try {
      ((Module) module).initializeTypeLoaders();
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public void initializeCompiler(GosucModule gosucModule) {
    _state = TypeSystemState.STARTING;
    try {
//      DefaultPlatformHelper.DISABLE_COMPILE_TIME_ANNOTATION = true;

      DefaultSingleModule module = new DefaultSingleModule( this, gosucModule.getName() );
      module.setNativeModule(gosucModule);
      module.setRoots(GosucUtil.toDirectories(gosucModule.getContentRoots()));
      module.configurePaths(GosucUtil.toDirectories(gosucModule.getClasspath()), GosucUtil.toDirectories(gosucModule.getAllSourceRoots()));
      _defaultModule = module;
      _modules = new ArrayList<IModule>(Collections.singletonList(module));

      module.initializeTypeLoaders();
      CommonServices.getEntityAccess().init();

      FrequentUsedJavaTypeCache.instance( this ).init();
    } finally {
      _state = TypeSystemState.STARTED;
    }
  }

  public void uninitializeCompiler() {
    _state = TypeSystemState.STOPPING;
    try {
      if (_defaultModule != null) {
        GlobalModule m = (GlobalModule) _defaultModule;
        m.getModuleTypeLoader().uninitializeTypeLoaders();
        m.getModuleTypeLoader().reset();
        m.setRoots(Collections.<IDirectory>emptyList());
        m.configurePaths(Collections.<IDirectory>emptyList(), Collections.<IDirectory>emptyList());

        GosuClassPathThing.cleanup();
      }

      _jreModule = null;
    } finally {
      _state = TypeSystemState.STOPPED;
    }
  }

  void checkForDuplicates(String moduleName) {
    for (IModule m : getModules()) {
      if (m.getName().equals(moduleName)) {
        throw new RuntimeException("Module " + moduleName + " allready exists.");
      }
    }
  }

  public void removeModule(IModule module) {
    _modules.remove(module);
  }

  public IModule getModule(String strModuleName) {
    for (IModule m : _modules) {
      if (m.getName().equals(strModuleName)) {
        return m;
      }
    }
    if( !ExecutionMode.isIDE() && GLOBAL_MODULE_NAME.equals( strModuleName ) ) {
      return getGlobalModule();
    }
    return null;
  }

  public IModule getModule( IResource file ) {
    List<? extends IModule> modules = getModules();
    if (modules.size() == 1) {
      return modules.get(0); // single module
    }

    for ( IModule module : modules) {
      if (module != _rootModule) {
        if (isInModule(module, file)) {
          return module;
        }
      }
    }

    if (isInModule(_rootModule, file)) {
      return _rootModule;
    }

    return null;
  }

  private boolean isInModule(IModule module, IResource file) {
    for (IDirectory src : module.getSourcePath()) {
      if (file.equals(src) || file.isDescendantOf(src)) {
        return true;
      }
    }
    return false;
  }

  public IModule getModule( URL url ) {
    return getModule( CommonServices.getFileSystem().getIFile( url ) );
  }

  @Override
  public IModule createJreModule( )
  {
    _jreModule = new JreModule( this );
    return _jreModule;
  }

  /**
   * @return The module responsible for resolving JRE core classes e.g.,
   *         java.lang.* etc. Note in default single module environment this is
   *         the single module, otherwise this is the module create by calling createJreModule().
   *         This method will never return null but it will throw an NPE if the JRE module is null.
   */
  public IModule getJreModule() {
    if (_jreModule == null) {
      if (!ExecutionMode.isIDE()) {
        _jreModule = getGlobalModule();
      } else {
        throw new RuntimeException("The JRE module was not created. Please create it before trying to get it.");
      }
    }
    return _jreModule;
  }

  public IModule getGlobalModule() {
    if (_rootModule == null) {
      String moduleName = System.getProperty("GW_ROOT_MODULE");
      if (moduleName != null) {
        _rootModule = getModule(moduleName);
        if (_rootModule == null) {
          throw new RuntimeException("The specified root module '" + moduleName +"' does not exist.");
        }
      } else {
        _rootModule = findRootModule();
      }
    }
    return _rootModule;
  }

  public IModule findRootModule() {
    List<IModule> moduleRoots = new ArrayList<IModule>(_modules);
    for (IModule module : _modules) {
      for (Dependency d : module.getDependencies()) {
        moduleRoots.remove(d.getModule());
      }
    }
    return moduleRoots.size() > 0 ? moduleRoots.get(0) : null;
  }

  public TypeSystemState getState() {
    return _state;
  }

  public void renameModule(IModule module, String newName) {
    ((ExecutionEnvironment)module.getExecutionEnvironment()).checkForDuplicates(newName);
    module.setName(newName);
  }

  @Override
  public String makeGosucProjectFile( String projectClassName ) {
    try {
      Class prjClass = ILanguageLevel.Util.STANDARD_GOSU() ? GosucProject.class : Class.forName( "com.guidewire.pl.gosuc.PlGosucProject" );
      GosucProject gosucProject = (GosucProject)prjClass.getConstructor( IExecutionEnvironment.class ).newInstance( this );
      return gosucProject.write();
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public void shutdown() {
    for (IModule module : _modules) {
      module.getModuleTypeLoader().shutdown();
    }
    INSTANCES.clear();
    THE_ONE = null;
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
    public boolean isShadowMode() {
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
  private void startSneakyDebugThread() {
    if( !BytecodeOptions.JDWP_ENABLED.get() ) {
      return;
    }
    ContextSensitiveCodeRunner blah = new ContextSensitiveCodeRunner();
    Thread sneakyDebugThread =
        new Thread(
            new Runnable() {
              public synchronized void run() {
                long timestamp = ReloadClassesIndicator.timestamp();
                long now = 0;
                while (getState() != TypeSystemState.STOPPED) {
                  try {
                    wait(2000);
                    now = ReloadClassesIndicator.timestamp();
                    if (now > timestamp) {
                      String script = ReloadClassesIndicator.getScript();
                      if (script != null && script.length() > 0) {
                        runScript(script);
                      }
                      else {
                        refreshTypes();
                      }
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                  } finally {
                    timestamp = now;
                  }
                }
              }

              private void refreshTypes() {
                String[] types = ReloadClassesIndicator.changedTypes();
                System.out.println("Refreshing " + types.length + " types at " + new Date());
                if (types.length > 0) {
                  for (String name : types) {
                    IType type = TypeSystem.getByFullNameIfValid(name);
                    if (type != null) {
                      TypeSystem.refresh((ITypeRef) type);
                      // Also update enhancement index if type is an enhancement
                      if( type instanceof IGosuEnhancementInternal ) {
                        ((GosuClassTypeLoader)type.getTypeLoader()).getEnhancementIndex().addEntry(
                          ((IGosuEnhancementInternal)type).getEnhancedType(), (IGosuEnhancementInternal)type );
                      }
                    }
                  }
                }
                CommonServices.getEntityAccess().reloadedTypes(types);
              }

              private void runScript( String strScript ) {
                String[] result = evaluate(strScript);
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
                IGosuParser scriptParser = GosuParserFactory.createParser(strScript);

                try
                {
                  IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
                  ParserOptions options = new ParserOptions().withParser( scriptParser );
                  IParseResult parseResult = programParser.parseExpressionOrProgram( strScript, scriptParser.getSymbolTable(), options );
                  Object result = parseResult.getProgram().evaluate( null );
                  if( result != null )
                  {
                    System.out.println( "Return Value: " + CommonServices.getCoercionManager().convertValue( result, JavaTypes.STRING() ) );
                  }
                }
                catch( Exception e )
                {
                  boolean print = true;
                  Throwable t = e;
                  while( t != null )
                  {
                    if( t instanceof SystemExitIgnoredException)
                    {
                      print = false;
                    }
                    t = t.getCause();
                  }
                  if( print )
                  {
                    assert e != null;
                    e.printStackTrace();
                  }
                }
                return new String[]{null, null};
              }
            }, CLASS_REDEFINER_THREAD);
    sneakyDebugThread.setDaemon(true);
    sneakyDebugThread.start();
  }

  public static List<IDirectory> createDefaultClassPath( ) {
    List<String> vals = new ArrayList<String>();
    vals.add(System.getProperty("java.class.path", ""));
    vals.add(CommonServices.getEntityAccess().getWebServerPaths());
    vals.addAll(getJarsContainingSpecialClasses());
    vals.add(System.getProperty("sun.boot.class.path", ""));
    vals.add(System.getProperty("java.ext.dirs", ""));
    vals.add(CommonServices.getEntityAccess().getPluginRepositories().toString());

    return expand(vals);
  }

  private static List<IDirectory> expand( List<String> paths )
  {
    LinkedHashSet<IDirectory> expanded = new LinkedHashSet<IDirectory>();
    for( String path : paths )
    {
      for( String pathElement : path.split( File.pathSeparator ) )
      {
        if( pathElement.length() > 0 )
        {
          IDirectory resource = CommonServices.getFileSystem().getIDirectory(new File(pathElement));
          expanded.add(resource);
        }
      }
    }
    return new ArrayList<IDirectory>( expanded );
  }
  @Override
  public boolean isShadowingMode() {
    return _project.isShadowMode();
  }

  /**
   * This method is a hack to resolve "special" system-like classes provided by execution environment.
   * This is the replacement of old addSpecialJars() method
   */
  private static Set<String> getJarsContainingSpecialClasses() {
    Set<String> paths = new HashSet<String>();
    for (String className : SPECIAL_CLASSES) {
      getLogger().debug("Searching JAR that provides " + className + ".");
      Class<?> clazz;
      try {
        clazz = Class.forName(className);
      } catch (ClassNotFoundException e) {
        if( !ILanguageLevel.Util.STANDARD_GOSU() ) {
          getLogger().error("Class " + className
                  + " could not be found. Gosu code might fail to compile at runtime.");
        }
        continue;
      }
      CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
      if (codeSource == null) {
        if( !ILanguageLevel.Util.STANDARD_GOSU() ) {
          getLogger().error("Code source for " + clazz.getName()
                  + " is null. Gosu code might fail to compile at runtime.");
        }
        continue;
      }
      // url might be jar:<url>!/, e.g. jar:file:/gitmo/jboss-5.1.2/common/lib/servlet-api.jar!/
      // or vfszip:<url> on JBoss
      // or wsjar:<url> on WebSphere
      URL jarUrl = codeSource.getLocation();

      // in case of complex URL the path might be like this: "file:/gitmo/jboss-5.1.2/common/lib/servlet-api.jar!/"
      String path = jarUrl.getPath();

      // So removing optional "!/" suffix and "file:" prefix
      if (path.endsWith("/")) {
        path = path.substring(0, path.length() - 1);
      }
      if (path.endsWith("!")) {
        path = path.substring(0, path.length() - 1);
      }
      if (path.startsWith("file:")) {
        path = path.substring("file:".length());
      }

      // URLDecoder.decode() decodes string from application/x-www-form-urlencoded MIME format
      // while we need to decode from RFC2396 format.
      // I think the only difference between formats that application/x-www-form-urlencoded decodes "+"
      // to space while RFC2396 does not.
      // So before using URLDecoder.decode() encode "+" to its ASCII representation
      // that will be decoded back to "+" by URLDecoder.decode()
      path = path.replaceAll("\\+", "%2B");
      try {
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        if (new File(decodedPath).exists()) {
          paths.add(path);
        } else {
          getLogger().error("Could not extract filesystem path from the url " + jarUrl.getPath()
                  + ". Gosu code that requires classes from that JAR might fail to compile at runtime.");
        }
      } catch (UnsupportedEncodingException ex) {
        // impossible
        throw GosuExceptionUtil.forceThrow(ex);
      }
    }
    return paths;
  }

  private static ILogger getLogger() {
    return CommonServices.getEntityAccess().getLogger();
  }

}
