/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.CommonServices;
import gw.config.IGlobalLoaderProvider;
import gw.config.IMemoryMonitor;
import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.IGosuParser;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class Gosuc implements IGosuc {

  private GosucProject _project;
  private IModule _globalModule;
  private List<GosucModule> _allGosucModules;

  public Gosuc( String projectFile, ICustomParser custParser ) throws FileNotFoundException {
    File file = new File( projectFile );
    if( !file.isFile() ) {
      System.err.println( "The project file does not exist: " + file );
    }
    FileInputStream is = new FileInputStream( file );
    _project = GosucProjectParser.parse( new BufferedInputStream( is ), custParser );
    _allGosucModules = _project.getModules();
  }

  public Gosuc( ICustomParser custParser, String projectFileContent ) throws FileNotFoundException {
    _project = GosucProjectParser.parse( projectFileContent, custParser );
    _allGosucModules = _project.getModules();
  }

  public void initializeGosu() {
    CommonServices.getKernel().redefineService_Privileged( IGlobalLoaderProvider.class,
            new GosucGlobalLoaderProvider( _project.getGlobalLoaders() ) );
    IMemoryMonitor memoryMonitor = _project.getMemoryMonitor();
    if (memoryMonitor != null) {
      CommonServices.getKernel().redefineService_Privileged(IMemoryMonitor.class, memoryMonitor);
    }
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( _project );
    List<IModule> modules = defineModules( _project );
    modules.add( _globalModule );
    GosuInitialization.instance( execEnv ).initializeMultipleModules( modules );
    updateAllModuleClasspaths( _project );
    IModule module = execEnv.getModule( IExecutionEnvironment.GLOBAL_MODULE_NAME );
    TypeSystem.pushModule( module );
    try {
      Object o1 = IGosuParser.NaN;
      Object o2 = JavaTypes.DOUBLE();
    }
    finally {
      TypeSystem.popModule( module );
    }
    _project.startDependencies();
  }

  private List<IModule> defineModules( GosucProject project ) {
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( project );
    execEnv.createJreModule( );
    _globalModule = GosuShop.createGlobalModule(execEnv);
    _globalModule.configurePaths(Collections.<IDirectory>emptyList(), Collections.<IDirectory>emptyList());
    _globalModule.addDependency( new Dependency( execEnv.getJreModule(), true ) );

    List<IDirectory> allSourcePaths = new ArrayList<IDirectory>();
    List<IDirectory> allRoots = new ArrayList<IDirectory>();
    Map<String, IModule> modules = new HashMap<String, IModule>();
    List<IModule> allModules = new ArrayList<IModule>();
    for( GosucModule gosucModule : _allGosucModules ) {
      IModule module = defineModule( project, gosucModule );
      if( module != null ) {
        allSourcePaths.addAll( module.getSourcePath() );
        allRoots.addAll( module.getRoots() );
        modules.put( gosucModule.getName(), module );
        allModules.add( module );
      }
    }

    for( GosucModule gosucModule : _allGosucModules ) {
      IModule module = modules.get( gosucModule.getName() );
      for( GosucDependency dep : gosucModule.getDependencies() ) {
        IModule moduleDep = modules.get( dep.getModuleName() );
        if( moduleDep != null ) {
          module.addDependency( new Dependency( moduleDep, isExported( gosucModule, dep.getModuleName() ) ) );
        }
      }
    }

    addImplicitJreModuleDependency( project, allModules );
    allSourcePaths.addAll( execEnv.getJreModule().getSourcePath() );

    List<IModule> rootModules = findRootModules(allModules);
    for (IModule rootModule : rootModules) {
      _globalModule.addDependency(new Dependency(rootModule, true));
    }
    _globalModule.setSourcePath( allSourcePaths );
    _globalModule.setRoots(allRoots);
    return allModules;
  }

  public List<IModule> findRootModules(List<IModule> modules) {
    List<IModule> moduleRoots = new ArrayList<IModule>(modules);
    for (IModule module : modules) {
      for (Dependency d : module.getDependencies()) {
        moduleRoots.remove(d.getModule());
      }
    }
    return moduleRoots;
  }

  public IModule defineModule( GosucProject project, GosucModule gosucModule ) {
    IModule gosuModule = GosuShop.createModule( TypeSystem.getExecutionEnvironment( project ),
                                                gosucModule.getName() );
    List<IDirectory> sourceFolders = getSourceFolders( gosucModule );
    gosuModule.configurePaths(getClassPaths(gosucModule), sourceFolders);
    IDirectory sourceRoot = computeCommonRoot( sourceFolders );
    if( sourceRoot != null ) {
      gosuModule.setRoots(Collections.<IDirectory>singletonList(sourceRoot));
    }
    gosuModule.setNativeModule( gosucModule );
    gosuModule.setExcludedPaths(getExcludedFolders( gosucModule ));
    return gosuModule;
  }

  private IDirectory computeCommonRoot( List<IDirectory> sourceFolders ) {
    if( sourceFolders.isEmpty() ) {
      return null;
    }
    else if( sourceFolders.size() == 1 ) {
      return sourceFolders.get( 0 ).getParent();
    }
    else {
      String[] paths = new String[sourceFolders.size()];
      int minLength = Integer.MAX_VALUE;
      for( int i = 0; i < paths.length; i++ ) {
        paths[i] = sourceFolders.get( i ).getPath().getFileSystemPathString();
        if( paths[i].length() < minLength ) {
          minLength = paths[i].length();
        }
      }
      int charIndex;
      outer:
      for( charIndex = 0; charIndex < minLength; charIndex++ ) {
        char c0 = paths[0].charAt( charIndex );
        for( int i = 1; i < paths.length; i++ ) {
          if( paths[i].charAt( charIndex ) != c0 ) {
            break outer;
          }
        }
      }
      String dirName = paths[0].substring( 0, charIndex );
      File dir = new File( dirName );
      if( !dir.exists() ) {
        return null;
      }
      else {
        return CommonServices.getFileSystem().getIDirectory( dir );
      }
    }
  }

  private List<IDirectory> getSourceFolders( GosucModule gosucModule ) {
    List<IDirectory> sourceFolders = new ArrayList<IDirectory>();
    for( String path : gosucModule.getAllSourceRoots() ) {
      sourceFolders.add( GosucUtil.getDirectoryForPath( path ) );
    }
    return sourceFolders;
  }

  private List<IDirectory> getExcludedFolders( GosucModule gosucModule ) {
    List<IDirectory> excludedFolders = new ArrayList<IDirectory>();
    for( String path : gosucModule.getExcludedRoots() ) {
      excludedFolders.add(GosucUtil.getDirectoryForPath(path));
    }
    return excludedFolders;
  }

  public boolean isExported( GosucModule gosucModule, String childModuleName ) {
    for( GosucDependency dep : gosucModule.getDependencies() ) {
      if( dep.getModuleName().equals( childModuleName ) ) {
        return dep.isExported();
      }
      if( isExported( findGosucModule( dep.getModuleName() ), childModuleName ) ) {
        return true;
      }
    }
    return false;
  }

  private GosucModule findGosucModule( String moduleName ) {
    for( GosucModule mod : _allGosucModules ) {
      if( mod.getName().equals( moduleName ) ) {
        return mod;
      }
    }
    return null;
  }

  private void addImplicitJreModuleDependency( GosucProject project, List<IModule> modules ) {
    IJreModule jreModule = (IJreModule)TypeSystem.getExecutionEnvironment( project ).getJreModule();
    updateJreModuleWithProjectSdk( project, jreModule );
    for( IModule module : modules ) {
      module.addDependency( new Dependency( jreModule, true ) );
    }
    modules.add( jreModule );
  }

  protected void updateJreModuleWithProjectSdk( GosucProject project, IJreModule jreModule ) {
    GosucSdk projectSdk = project.getSdk();
    List<String> classFiles = projectSdk.getPaths();
    List<IDirectory> dirs = new ArrayList<IDirectory>();
    for (String path : classFiles) {
      dirs.add(GosucUtil.getDirectoryForPath(path));
    }
    jreModule.configurePaths(dirs, Collections.<IDirectory>emptyList());
    jreModule.setNativeSDK( projectSdk );
  }

  void updateAllModuleClasspaths( GosucProject project ) {
    final List<? extends IModule> modules = TypeSystem.getExecutionEnvironment( project ).getModules();
    List<GosucModule> gosucModules = new ArrayList<GosucModule>();
    for( IModule module : modules ) {
      GosucModule gosucModule = (GosucModule)module.getNativeModule();
      if( gosucModule != null ) {
        gosucModules.add( gosucModule );
      }
    }

    // FIXME-isd: why do we need to update classpaths again?
    Map<String, List<IDirectory>> classpathMap = createClassPathMap( gosucModules.toArray( new GosucModule[gosucModules.size()] ) );
    for( IModule module : modules ) {
      if( module.getNativeModule() != null ) {
        module.configurePaths(classpathMap.get(module.getName()), module.getSourcePath());
      }
    }
  }

  private Map<String, List<IDirectory>> createClassPathMap( GosucModule[] allGosucModules ) {
    Map<String, List<IDirectory>> classpathMap = new HashMap<String, List<IDirectory>>();

    for( GosucModule module : allGosucModules ) {
      String name = module.getName();
      List<IDirectory> classPaths = getClassPaths( module );
      classpathMap.put( name, classPaths );
    }

    // simplify classpaths
    for( GosucModule module : allGosucModules ) {
      List<IDirectory> referencedTotalClasspath = getReferencedTotalClasspath( module, classpathMap );
      if( referencedTotalClasspath != null ) {
        List<IDirectory> claspath = classpathMap.get( module.getName() );
        for( Iterator<IDirectory> i = claspath.iterator(); i.hasNext(); ) {
          IDirectory dir = i.next();
          if( referencedTotalClasspath.contains( dir ) ) {
            i.remove();
          }
        }
      }
    }

    return classpathMap;
  }


  private List<IDirectory> getReferencedTotalClasspath( GosucModule gosucModule, Map<String, List<IDirectory>> classpathMap ) {
    List<IDirectory> totalClasspath = new ArrayList<IDirectory>();
    List<GosucModule> referencedModules = getAllRequiredModules( gosucModule );
    for( GosucModule m : referencedModules ) {
      totalClasspath.addAll( classpathMap.get( m.getName() ) );
    }
    return totalClasspath;
  }


  public List<GosucModule> getAllRequiredModules( GosucModule gosucModule ) {
    Set<GosucModule> visitedProjects = new HashSet<GosucModule>();
    List<GosucModule> modules = new ArrayList<GosucModule>();
    getAllRequiredProjects( gosucModule, modules, visitedProjects );
    return modules;
  }

  private void getAllRequiredProjects( GosucModule gosucModule, List<GosucModule> gosucModuleList, Set<GosucModule> visitedModules ) {
    visitedModules.add( gosucModule );

    for( GosucDependency dep : gosucModule.getDependencies() ) {
      GosucModule depMod = findGosucModule( dep.getModuleName() );
      if( !visitedModules.contains( depMod ) ) {
        gosucModuleList.add( depMod );
        getAllRequiredProjects( depMod, gosucModuleList, visitedModules );
      }
    }
  }

  private static List<IDirectory> getClassPaths(GosucModule gosucModule) {
    List<IDirectory> paths = new ArrayList<IDirectory>();
    for( String path : gosucModule.getClasspath() ) {
      paths.add( GosucUtil.getDirectoryForPath( path ) );
    }
    return paths;
  }

  public List<IType> compile( String moduleName, List<String> types ) {
    IModule module = moduleName == null
                     ? TypeSystem.getGlobalModule()
                     : TypeSystem.getExecutionEnvironment().getModule( moduleName );
    return compile( module, types );
  }

  public List<IType> compile( IModule module, List<String> types ) {
    TypeSystem.pushModule( module );
    try {
      return new GosucCompiler().compile( _project, types );
    }
    finally {
      TypeSystem.popModule( module );
    }
  }

  // You can use this for testing by:
  // - From an IJ project use the 'Write Gosuc Project' command to write out the project file
  // - Then run this from IJ using 'classpath of module' setting to match the proper module for pl/pc/cc etc. (this is so the global loaders will be in the classpath)
  public static void main( String[] args ) throws FileNotFoundException {
    String error = GosucArg.parseArgs( args );
    if( error != null ) {
      System.err.println( error );
      return;
    }
    String strFile = GosucArg.PROJECT.getValue();
    Gosuc gosuc = new Gosuc( strFile, maybeGetCustomParser() );
    gosuc.initializeGosu();
    gosuc.compile( (String)null, Collections.singletonList( "-all" ) );
  }

  private static ICustomParser maybeGetCustomParser() {
    String cls = GosucArg.PARSER.getValue();
    if( cls != null ) {
      try {
        Class.forName( cls ).newInstance();
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    return null;
  }

}
