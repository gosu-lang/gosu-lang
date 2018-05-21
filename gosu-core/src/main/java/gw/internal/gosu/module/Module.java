/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.internal.gosu.dynamic.DynamicTypeLoader;
import gw.internal.gosu.parser.DefaultTypeLoader;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
import gw.internal.gosu.parser.GosuClass;
import gw.internal.gosu.parser.IModuleClassLoader;
import gw.internal.gosu.parser.ModuleClassLoader;
import gw.internal.gosu.parser.ModuleTypeLoader;
import gw.internal.gosu.parser.java.compiler.JavaStubGenerator;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.ILanguageLevel;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.SimpleTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.IJavaType;

import java.util.stream.Collectors;
import javax.tools.DiagnosticListener;
import manifold.api.fs.IFile;
import manifold.api.fs.IFileSystem;
import manifold.api.fs.cache.PathCache;
import manifold.api.host.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.INativeModule;
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
import javax.tools.JavaFileObject;
import manifold.api.fs.Extensions;
import manifold.api.fs.IDirectory;
import manifold.api.fs.jar.JarFileDirectoryImpl;
import manifold.api.type.ITypeManifold;
import manifold.internal.javac.GeneratedJavaStubFileObject;
import manifold.internal.javac.SourceJavaFileObject;
import manifold.internal.javac.SourceSupplier;

public class Module implements IModule
{
  private final IExecutionEnvironment _execEnv;
  private final LocklessLazyVar<PathCache> _pathCache;
  private String _strName;

  private List<Dependency> _dependencies = new ArrayList<>();
  private LocklessLazyVar<IModule[]> _traversalList;
  private ModuleTypeLoader _modTypeLoader;

  // Paths
  private List<IDirectory> _classpath = new ArrayList<>();
  private List<IDirectory> _backingSourcePath = new ArrayList<>();

  private INativeModule _nativeModule;
  private ClassLoader _moduleClassLoader;
  private ClassLoader _extensionsClassLoader;

  private final IFileSystemGosuClassRepository _fileRepository = new FileSystemGosuClassRepository(this);

  public Module(IExecutionEnvironment execEnv, String strName)
  {
    _execEnv = execEnv;
    _strName = strName;
    _pathCache = LocklessLazyVar.make( this::makePathCache );
    _traversalList = LocklessLazyVar.make( this::buildTraversalList );
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
    _dependencies = new ArrayList<>(newDeps);
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
  public List<IDirectory> getCollectiveSourcePath()
  {
    List<IDirectory> all = new ArrayList<>();
    all.addAll( getSourcePath() );

    for( Dependency d : getDependencies() )
    {
      if( d.isExported() )
      {
        all.addAll( d.getModule().getSourcePath() );
      }
    }
    return all;
  }

  @Override
  public List<IDirectory> getCollectiveJavaClassPath()
  {
    List<IDirectory> all = new ArrayList<>();
    //all.addAll( getJavaClassPath() );
    all.addAll( getCollectiveSourcePath() );
    return all;
  }

  @Override
  public IFileSystem getFileSystem()
  {
    return CommonServices.getFileSystem();
  }

  @Override
  public void setSourcePath( List<IDirectory> sourcePaths )
  {
    List<IDirectory> sources = new ArrayList<>(sourcePaths);

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
    //noinspection Convert2streamapi
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
  public List<IDirectory> getOutputPath()
  {
    return _nativeModule.getOutputPath();
  }

  @Override
  public IDirectory[] getExcludedPath()
  {
    return getFileRepository().getExcludedPath();
  }

  @Override
  public Set<ITypeManifold> getTypeManifolds()
  {
    Set<ITypeManifold> all = new HashSet<>();
    GosuClassTypeLoader gosuLoader = GosuClassTypeLoader.getDefaultClassLoader( this );
    if(gosuLoader == null) {
      throw new IllegalStateException("No instance of GosuClassTypeLoader found");
    }
//    String diagnosticMessage = String.format("*** GosuClassTypeLoader is: %s ***", gosuLoader.toString());
//    System.out.println(diagnosticMessage);
//    if(gosuLoader.getTypeManifolds() == null) {
//      throw new IllegalStateException("No TypeManifolds found for " + diagnosticMessage);
//    }
    all.addAll( gosuLoader.getTypeManifolds() );
    DefaultTypeLoader defaultLoader = DefaultTypeLoader.instance( this );
    all.addAll( defaultLoader.getTypeManifolds() );
    return all;
  }

  @Override
  public Set<ITypeManifold> findTypeManifoldsFor( String fqn )
  {
    Set<ITypeManifold> sps = new HashSet<>( 2 );
    for( ITypeManifold sp : getTypeManifolds() )
    {
      if( sp.isType( fqn ) )
      {
        sps.add( sp );
      }
    }
    return sps;
  }

  @Override
  public Set<ITypeManifold> findTypeManifoldsFor( IFile file )
  {
    Set<ITypeManifold> sps = new HashSet<>( 2 );
    for( ITypeManifold sp : getTypeManifolds() )
    {
      if( sp.handlesFile( file ) )
      {
        sps.add( sp );
      }
    }
    return sps;
  }

  @Override
  public PathCache getPathCache()
  {
    return _pathCache.get();
  }

  @Override
  public JavaFileObject produceFile( String fqn, DiagnosticListener<JavaFileObject> errorHandler )
  {
    IType type = TypeSystem.getByFullNameIfValid( fqn, this );
    JavaFileObject file = null;
    if( type != null && type instanceof IFileRepositoryBasedType && !GosuClass.ProxyUtil.isProxy( type ) && !isErrantTestArtifact( fqn ) )
    {
      if( !(type instanceof IJavaType) )
      {
        file = makeJavaStub( type );
      }
      else
      {
        ISourceFileHandle sourceFileHandle = ((IJavaType)type).getSourceFileHandle();
        if( sourceFileHandle != null )
        {
          Set<ITypeManifold> typeManifold = sourceFileHandle.getTypeManifolds();
          if( !typeManifold.isEmpty() )
          {
            // The source for this type is not on disk, but is instead generated on demand
            file = produceSource( (IFileRepositoryBasedType)type );
          }
          else if( ExecutionMode.isRuntime() && sourceFileHandle instanceof FileSystemGosuClassRepository.FileSystemSourceFileHandle )
          {
            // The Java source file is loaded dynamically at runtime
            file = new SourceJavaFileObject( sourceFileHandle.getFile().toURI() );
            ((SourceJavaFileObject)file).setFqn( fqn );
          }
        }
      }
    }
    return file;
  }
  private JavaFileObject produceSource( IFileRepositoryBasedType loadableType )
  {
    ISourceFileHandle sfh = loadableType.getSourceFileHandle();
    return new GeneratedJavaStubFileObject( loadableType.getName(), new SourceSupplier( sfh.getTypeManifolds(), () -> sfh.getSource().getSource() ) );
  }
  private JavaFileObject makeJavaStub( IType loadableType )
  {
    return new GeneratedJavaStubFileObject( loadableType.getName(), new SourceSupplier( null, () -> JavaStubGenerator.instance().genStub( loadableType ) ) );
  }
  private boolean isErrantTestArtifact( String fqn )
  {
    return fqn.contains( ".Errant_" );
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
  public void configurePaths( List<IDirectory> classpath, List<IDirectory> sourcePaths, List<IDirectory> backingSourcePaths )
  {
    // Maybe expand paths to include Class-Path attribute from Manifest...
    classpath = addFromManifestClassPath( classpath );
    sourcePaths = addFromManifestClassPath( sourcePaths );

    // Scan....
    List<IDirectory> sourceRoots = new ArrayList<>(sourcePaths);
    Set<String> extensions = new HashSet<>();
    scanPaths(classpath, extensions, sourceRoots);

    setSourcePath(sourceRoots);
    setJavaClassPath(classpath);
    setBackingSourcePath( backingSourcePaths );
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
                if( !newClasspath.contains( idir ) )
                {
                  newClasspath.add( idir );
                }
              }
            }
          }
        }
        catch( Throwable e )
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
  public List<IDirectory> getBackingSourcePath()
  {
    return _backingSourcePath;
  }
  @Override
  public void setBackingSourcePath( List<IDirectory> backingSourcePath ) {
    _backingSourcePath = backingSourcePath;
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
    initializeTypeManifolds();
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() ) {
      createExtensionTypeLoaders();
    }

    // initialize all loaders
    List<ITypeLoader> loaders = getModuleTypeLoader().getTypeLoaders();
    for (int i = loaders.size() - 1; i >= 0; i--) {
      loaders.get(i).init();
    }
  }

  private void initializeTypeManifolds()
  {
    for( ITypeLoader loader: getModuleTypeLoader().getTypeLoaderStack() )
    {
      if( loader instanceof SimpleTypeLoader)
      {
        ((SimpleTypeLoader)loader).initializeTypeManifolds();
      }
    }
  }

  protected void createExtensionTypeLoaders() {
    createExtensionTypeloadersImpl();
  }

  protected void createExtensionTypeloadersImpl()
  {
    Set<String> typeLoaders = new HashSet<>();
    findExtensionClasses( typeLoaders );
    for( String additionalTypeLoader: typeLoaders )
    {
      try
      {
        createAndPushTypeLoader( _fileRepository, additionalTypeLoader );
      }
      catch( Throwable e )
      {
        System.err.println( "==> WARNING: Cannot create extension typeloader " + additionalTypeLoader + ". " + e.getMessage() );
//        e.printStackTrace(System.err);
        System.err.println( "==> END WARNING." );
      }
    }
  }

  private void findExtensionClasses( Set<String> typeLoaders )
  {
    for( IModule m : getModuleTraversalList() )
    {
      for( IDirectory dir: m.getJavaClassPath() )
      {
        Extensions.getExtensions( typeLoaders, dir, "Gosu-Typeloaders" );
      }
    }
  }

  protected void createStandardTypeLoaders()
  {
    CommonServices.getTypeSystem().pushTypeLoader( this, new GosuClassTypeLoader( this, _fileRepository ) );
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
    List<IModule> traversalList = new ArrayList<>();
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
      IModule dependencyModule = (IModule)dependency.getModule();

      // traverse all direct dependency and indirect exported dependencies
      if (!traversalList.contains(dependencyModule) &&
              (dependency.isExported() || theModule == this)) {
        traverse(dependencyModule, traversalList);
      }
    }
  }

  @Override
  public <T extends ITypeLoader> List<? extends T> getTypeLoaders(Class<T> typeLoaderClass) {
    List<T> results = new ArrayList<>();
    if (_modTypeLoader == null) {
      return results;
    }
    //noinspection Convert2streamapi
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
    List<URL> urls = new ArrayList<>();
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

  private Constructor getConstructor( Class<?> loaderClass, Class... argTypes )
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
      TypeSystem.addShutdownListener( INSTANCE::clear );
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

  private PathCache makePathCache()
  {
    return new PathCache( this, this::makeModuleSourcePath, _pathCache::clear );
  }

  private List<IDirectory> makeModuleSourcePath()
  {
    return getSourcePath().stream()
      .filter( dir -> Arrays.stream( getExcludedPath() )
        .noneMatch( excludeDir -> excludeDir.equals( dir ) ) )
      .collect( Collectors.toList() );
  }
}
