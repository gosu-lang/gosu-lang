/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.jar.JarFileDirectoryImpl;
import gw.internal.gosu.dynamic.DynamicTypeLoader;
import gw.internal.gosu.parser.DefaultTypeLoader;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
import gw.internal.gosu.parser.IModuleClassLoader;
import gw.internal.gosu.parser.ModuleClassLoader;
import gw.internal.gosu.parser.ModuleTypeLoader;
import gw.internal.gosu.properties.PropertiesTypeLoader;
import gw.lang.parser.ILanguageLevel;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.INativeModule;
import gw.util.Extensions;
import gw.util.GosuExceptionUtil;
import gw.util.concurrent.LocklessLazyVar;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Module implements IModule
{
  private final IExecutionEnvironment _execEnv;
  private String _strName;

  private List<Dependency> _dependencies = new ArrayList<Dependency>();
  private LocklessLazyVar<IModule[]> _traversalList = new LocklessLazyVar<IModule[]>() {
    @Override
    protected IModule[] init() {
      return buildTraversalList();
    }
  };
  private ModuleTypeLoader _modTypeLoader;

  // Paths
  protected List<IDirectory> _classpath = new ArrayList<IDirectory>();

  private INativeModule _nativeModule;
  private ClassLoader _moduleClassLoader;
  private ClassLoader _extensionsClassLoader;

  private final IFileSystemGosuClassRepository _fileRepository = new FileSystemGosuClassRepository(this);

  public Module(IExecutionEnvironment execEnv, String strName)
  {
    _execEnv = execEnv;
    _strName = strName;
  }

  public final IExecutionEnvironment getExecutionEnvironment()
  {
    return _execEnv;
  }

  @Override
  public IFileSystemGosuClassRepository getFileRepository() {
    return _fileRepository;
  }

  @Override
  public void setDependencies(List<Dependency> newDeps) {
    _dependencies = new ArrayList<Dependency>(newDeps);
    _traversalList.clear();
  }

  @Override
  public List<Dependency> getDependencies()
  {
    return _dependencies;
  }

  @Override
  public void addDependency( Dependency d )
  {
    _dependencies.add(d);
    _traversalList.clear();
  }

  public void removeDependency( Dependency d )
  {
    _dependencies.remove(d);
    _traversalList.clear();
  }

  @Override
  public List<IDirectory> getSourcePath()
  {
    return Arrays.asList(_fileRepository.getSourcePath());
  }

  @Override
  public void setSourcePath( List<IDirectory> sourcePaths )
  {
    List<IDirectory> sources = new ArrayList<IDirectory>(sourcePaths);

    //## todo: Kill this so the classpath from the ClassLoaders is 1:1 with Modules i.e., why are we not copying these into the target classpath??!!
    sources.addAll(getAdditionalSourceRoots());

    _fileRepository.setSourcePath(sources.toArray(new IDirectory[sourcePaths.size()]));
  }

  @Override
  public List<IDirectory> getExcludedPaths() {
    return Arrays.asList(_fileRepository.getExcludedPath());
  }

  @Override
  public void setExcludedPaths(List<IDirectory> paths) {
    _fileRepository.setExcludedPath(paths.toArray(new IDirectory[paths.size()]));
  }

  @Override
  public ClassLoader getModuleClassLoader() {
    if (_moduleClassLoader == null) {
      _moduleClassLoader = ModuleClassLoader.create(this);
    }
    return _moduleClassLoader;
  }

  @Override
  public void disposeLoader() {
    if (_moduleClassLoader instanceof IModuleClassLoader) {
      ((IModuleClassLoader) _moduleClassLoader).dispose();
    }
    _moduleClassLoader = null;
  }

  private static void scanPaths(List<IDirectory> paths, Set<String> extensions, List<IDirectory> roots) {
    extensions.add(".java");
    extensions.add(".xsd");
    extensions.addAll(Arrays.asList(GosuClassTypeLoader.ALL_EXTS));
    for (IDirectory root : paths) {
      // roots without manifests are considered source roots
      if (!Extensions.containsManifest(root) || !Extensions.getExtensions(root, Extensions.CONTAINS_SOURCES).isEmpty() ||
              // Weblogic packages all WEB-INF/classes content into this JAR
              // http://middlewaremagic.com/weblogic/?p=408
              // http://www.coderanch.com/t/69641/BEA-Weblogic/wl-cls-gen-jar-coming
              // So we need to always treat it as containing sources
              root.getName().equals("_wl_cls_gen.jar")) {
        if( !roots.contains( root ) )
        {
          roots.add( root );
        }
      }
    }
  }

  @Override
  public IDirectory getOutputPath()
  {
    return _nativeModule.getOutputPath();
  }

  public ModuleTypeLoader getModuleTypeLoader()
  {
    return _modTypeLoader;
  }

  public void setModuleTypeLoader( ModuleTypeLoader modTypeLoader )
  {
    _modTypeLoader = modTypeLoader;
  }

  @Override
  public void configurePaths(List<IDirectory> classpath, List<IDirectory> sourcePaths)
  {
    // Maybe expand paths to include Class-Path attribute from Manifest...
    classpath = addFromManifestClassPath( classpath );
    sourcePaths = addFromManifestClassPath( sourcePaths );

    // Scan....
    List<IDirectory> sourceRoots = new ArrayList<IDirectory>(sourcePaths);
    Set<String> extensions = new HashSet<String>();
    scanPaths(classpath, extensions, sourceRoots);

    // FIXME: extensions...
    setSourcePath(sourceRoots);
    setJavaClassPath(classpath);
  }

  /**
   * <p>This will add items to the Gosu classpath, but only under very specific circumstances.
   * <p>If both of the following conditions are met:
   * <ul>
   *   <li>The JAR's manifest contains a Class-Path entry</li>
   *   <li>The Class-Path entry contains a space-delimited list of URIs</li>
   * </ul>
   * <p>Then the entries will be parsed and added to the Gosu classpath.
   * 
   * <p>This logic also handles strange libraries packaged pre-Maven such as xalan:xalan:2.4.1
   * 
   * <p>The xalan JAR above has a Class-Path attribute referencing the following:
   * <pre>
   *   Class-Path: xercesImpl.jar xml-apis.jar
   * </pre>
   * 
   * These unqualified references should have been resolved by the build tooling, and if we try to interfere and resolve
   * the references, we may cause classpath confusion. Therefore any Class-Path entry not resolvable to an absolute
   * path on disk (and, therefore, can be listed as a URL) will be skipped.
   * 
   * @see java.util.jar.Attributes.Name#CLASS_PATH
   * @param classpath The module's Java classpath
   * @return The original classpath, possibly with dependencies listed in JAR manifests Class-Path extracted and explicitly listed
   */
  private List<IDirectory> addFromManifestClassPath( List<IDirectory> classpath )
  {
    if( classpath == null )
    {
      return classpath;
    }

    ArrayList<IDirectory> newClasspath = new ArrayList<>();
    for( IDirectory root : classpath )
    {
      //add the root JAR itself first, preserving ordering
      if( !newClasspath.contains( root ) )
      {
        newClasspath.add( root );
      }
      if( root instanceof JarFileDirectoryImpl )
      {
        JarFile jarFile = ((JarFileDirectoryImpl)root).getJarFile();
        try
        {
          Manifest manifest = jarFile.getManifest();
          if( manifest != null )
          {
            Attributes man = manifest.getMainAttributes();
            String paths = man.getValue( Attributes.Name.CLASS_PATH );
            if( paths != null && !paths.isEmpty() )
            {
              // We found a Jar with a Class-Path listing.
              // Note sometimes happens when running from IntelliJ where the
              // classpath would otherwise make the command line to java.exe
              // too long.
              for( String j : paths.split( " " ) )
              {
                // Add each of the paths to our classpath
                URL url;
                try {
                  url = new URL( j );
                } catch (MalformedURLException e) {
                  //Class-Path contained an invalid URL, skip it
                  continue;
                }
                File dirOrJar = new File( url.toURI() );
                IDirectory idir = CommonServices.getFileSystem().getIDirectory( dirOrJar );
                if( !newClasspath.contains( root ) )
                {
                  newClasspath.add( idir );
                }
              }
            }
          }
        }
        catch( Exception e )
        {
          throw GosuExceptionUtil.forceThrow( e );
        }
      }
    }

    return newClasspath;
  }

  @Override
  public List<IDirectory> getJavaClassPath()
  {
    return _classpath;
  }

  @Override
  public void setJavaClassPath( List<IDirectory> classpath ) {
    _classpath = classpath;
  }

  @Override
  public String toString()
  {
    return _strName;
  }

  @Override
  public Object getNativeModule()
  {
    return _nativeModule != null ? _nativeModule.getNativeModule() : null;
  }

  @Override
  public void setNativeModule( INativeModule nativeModule )
  {
    _nativeModule = nativeModule;
  }

  public void initializeTypeLoaders() {
    maybeCreateModuleTypeLoader();
    createStandardTypeLoaders();
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() ) {
      createExtensionTypeLoaders();
    }

    // initialize all loaders
    List<ITypeLoader> loaders = getModuleTypeLoader().getTypeLoaders();
    for (int i = loaders.size() - 1; i >= 0; i--) {
      loaders.get(i).init();
    }
  }

  protected void createExtensionTypeLoaders() {
    createExtensionTypeloadersImpl();
  }

  protected void createExtensionTypeloadersImpl() {
    Set<String> typeLoaders = getExtensionTypeloaderNames();
    for( String additionalTypeLoader : typeLoaders) {
      try {
        createAndPushTypeLoader(_fileRepository, additionalTypeLoader);
      } catch (Throwable e) {
        System.err.println("==> WARNING: Cannot create extension typeloader " + additionalTypeLoader + ". " + e.getMessage());
//        e.printStackTrace(System.err);
        System.err.println("==> END WARNING.");
      }
    }
  }

  private Set<String> getExtensionTypeloaderNames() {
    Set<String> set = new HashSet<String>();
    for (IModule m : getModuleTraversalList()) {
      for (IDirectory dir : m.getJavaClassPath()) {
        Extensions.getExtensions(set, dir, "Gosu-Typeloaders");
      }
    }
    return set;
  }

  protected void createStandardTypeLoaders()
  {
    CommonServices.getTypeSystem().pushTypeLoader( this, new GosuClassTypeLoader( this, _fileRepository ) );
    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      CommonServices.getTypeSystem().pushTypeLoader( this, new PropertiesTypeLoader( this ) );
    }
    if( ILanguageLevel.Util.DYNAMIC_TYPE() ) {
      CommonServices.getTypeSystem().pushTypeLoader( this, new DynamicTypeLoader( this ) );
    }
  }

  protected void maybeCreateModuleTypeLoader() {
    if (getModuleTypeLoader() == null) {
      ModuleTypeLoader tla = new ModuleTypeLoader( this, new DefaultTypeLoader(this) );
      setModuleTypeLoader( tla );
    }
  }

  public final IModule[] getModuleTraversalList() {
    return _traversalList.get();
  }

  private IModule[] buildTraversalList() {
    // create default traversal list
    List<IModule> traversalList = new ArrayList<IModule>();
    traverse(this, traversalList);
    // make sure that the jre module is last
    IModule jreModule = getExecutionEnvironment().getJreModule();
    if (traversalList.remove(jreModule)) {
      traversalList.add(jreModule);
    }
    IModule globalModule = getExecutionEnvironment().getGlobalModule();
    if (this != globalModule) {
      traversalList.add(0, globalModule);
    }
    return traversalList.toArray( new IModule[traversalList.size()] );
  }


  protected void traverse(final IModule theModule, List<IModule> traversalList) {
    traversalList.add(theModule);
    for (Dependency dependency : theModule.getDependencies()) {
      IModule dependencyModule = dependency.getModule();

      // traverse all direct dependency and indirect exported dependencies
      if (!traversalList.contains(dependencyModule) &&
              (dependency.isExported() || theModule == this)) {
        traverse(dependencyModule, traversalList);
      }
    }
  }

  @Override
  public <T extends ITypeLoader> List<? extends T> getTypeLoaders(Class<T> typeLoaderClass) {
    List<T> results = new ArrayList<T>();
    if (_modTypeLoader == null) {
      return results;
    }
    for (ITypeLoader loader : getModuleTypeLoader().getTypeLoaderStack()) {
      if (typeLoaderClass.isInstance(loader)) {
        results.add(typeLoaderClass.cast(loader));
      }
    }
    return results;
  }

  private ITypeLoader createAndPushTypeLoader(IFileSystemGosuClassRepository classRepository, String className)
  {
    ITypeLoader typeLoader = null;
    try
    {
      Class loaderClass = getExtensionClassLoader().loadClass( className );
      CommonServices.getGosuInitializationHooks().beforeTypeLoaderCreation( loaderClass );

      Constructor constructor = getConstructor( loaderClass, IModule.class );
      if( constructor != null )
      {
        typeLoader = (ITypeLoader) constructor.newInstance( this );
      }
      else
      {
        constructor = getConstructor( loaderClass, IModule.class );
        if( constructor != null )
        {
          typeLoader = (ITypeLoader) constructor.newInstance( this );
        }
        else
        {
          if( constructor != null )
          {
            typeLoader = (ITypeLoader) constructor.newInstance( this );
          }
          else
          {
            constructor = getConstructor( loaderClass, IGosuClassRepository.class );
            if( constructor != null )
            {
              typeLoader = (ITypeLoader) constructor.newInstance( classRepository );
            }
            else
            {
              constructor = getConstructor( loaderClass );
              if( constructor != null )
              {
                typeLoader = (ITypeLoader) constructor.newInstance();
              }
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    if( typeLoader != null )
    {
      CommonServices.getTypeSystem().pushTypeLoader( this, typeLoader );
      CommonServices.getGosuInitializationHooks().afterTypeLoaderCreation();
    }
    else
    {
      throw new IllegalStateException(
        "TypeLoader class " + className + " must have one of the following constructor signatures:\n" +
        "  <init>()\n" +
        "  <init>(gw.lang.reflect.module.IModule)\n" +
        "  <init>(gw.lang.reflect.gs.IGosuClassRepository)\n" );
    }
    return typeLoader;
  }

  private ClassLoader getExtensionClassLoader() {
    if (_extensionsClassLoader == null) {
      _extensionsClassLoader = ExtensionClassLoader.create(getExtensionURLs());
    }
    return _extensionsClassLoader;
  }

  private URL[] getExtensionURLs() {
    List<URL> urls = new ArrayList<URL>();
    for (IModule m : getModuleTraversalList()) {
      for (IDirectory path : m.getJavaClassPath()) {
        try {
          urls.add(path.toURI().toURL());
        } catch (MalformedURLException e) {
          //ignore
        }
      }
    }
    return urls.toArray(new URL[urls.size()]);
  }

  private Constructor getConstructor( Class loaderClass, Class... argTypes )
  {
    try
    {
      return loaderClass.getConstructor( argTypes );
    }
    catch( NoSuchMethodException e )
    {
      return null;
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof IModule)) {
      return false;
    }
    IModule m = (IModule) o;
    return this.getName().equals(m.getName());
  }

  public int hashCode() {
    return _strName.hashCode();
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  @Override
  public void setName(String name) {
    _strName = name;
  }

  protected List<IDirectory> getAdditionalSourceRoots() {
    return Collections.emptyList();
  }

  /**
   * Singleton extension classloader. Used for loading typeloaders.
   */
  private static class ExtensionClassLoader extends URLClassLoader {
    private static final LocklessLazyVar<ExtensionClassLoader> INSTANCE
            = new LocklessLazyVar<ExtensionClassLoader>() {
      @Override
      protected ExtensionClassLoader init() {
        return new ExtensionClassLoader(ExtensionClassLoader.class.getClassLoader());
      }
    };

    static {
      TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
        @Override
        public void shutdown() {
          INSTANCE.clear();
        }
      });
    }

    public static ClassLoader create(URL[] urls) {
      ExtensionClassLoader loader = INSTANCE.get();
      for (URL url : urls) {
        loader.addURL(url);
      }
      return loader;
    }

    private ExtensionClassLoader(ClassLoader parent) {
      super(new URL[0], parent);
    }
  }

}
