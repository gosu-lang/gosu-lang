/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.module.fs.FileSystemImpl;
import gw.lang.gosuc.Gosuc;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import gw.util.concurrent.LocklessLazyVar;
import manifold.util.JreUtil;
import manifold.util.ReflectUtil;

public class ClassPath implements IClassPath
{
  private static final LocklessLazyVar<Class<?>> BUILTIN_CLASSLOADER =
    LocklessLazyVar.make( () -> ReflectUtil.type( "jdk.internal.loader.BuiltinClassLoader" ) );

  private static final String CLASS_FILE_EXT = ".class";
  private IModule _module;
  private ClassPathFilter _filter;
  private FqnCache<Object> _cache = new FqnCache<Object>();
  private IFileSystem _fs;
  private boolean _bStableFiles;

  public ClassPath(IModule module, ClassPathFilter filter)
  {
    _module = module;
    _filter = filter;

    // Files are assumed stable outside an IDE
    _fs = CommonServices.getFileSystem();
    _bStableFiles = _fs instanceof FileSystemImpl;

    loadClasspathInfo();
  }

  public ArrayList<IDirectory> getPaths()
  {
    return new ArrayList<IDirectory>( _module.getJavaClassPath());
  }

  public boolean contains(String fqn) {
    return _cache.contains(fqn);
  }

  public IFile get( String fqn ) {
    FqnCacheNode<Object> node = _cache.getNode( fqn );
    if( node == null ) {
      return null;
    }
    Object value = node.getUserData();
    if( value instanceof IFile ) {
      if( _bStableFiles ) {
        // Files are assumed stable outside an IDE
        return (IFile)value;
      }
      try {
        // Lazily compute and cache the URL, it can be an expensive native file system call
        value = ((IFile)value).toURI().toURL();
        node.setUserData( value );
      }
      catch( MalformedURLException e ) {
        throw new RuntimeException( e );
      }
    }
    //## todo: it'd be better if ClassPath could listen to FS changes and not query the FS for every
    // Get the file *fresh* from the abstract file system
    return _fs.getIFile( (URL)value );
  }

  public Set<String> getFilteredClassNames() {
    return _cache.getFqns();
  }

  public boolean isEmpty() {
    return _cache.getRoot().isLeaf();
  }

  // ====================== PRIVATE ====================================

  private void loadClasspathInfo()
  {
    if( JreUtil.isJava8() )
    {
      loadClasspathInfo_Java8();
    }
    else
    {
      loadClasspathInfo_Java9();
    }
  }

  private void loadClasspathInfo_Java8()
  {
    List<IDirectory> javaClassPath = _module.getJavaClassPath();
    IDirectory[] paths = javaClassPath.toArray(new IDirectory[javaClassPath.size()]);
    for (int i = paths.length - 1; i >= 0; i--) {
      IDirectory path = paths[i];
      addClassNames(path, path, _filter);
    }
  }

  private void loadClasspathInfo_Java9()
  {
//    long mark = System.nanoTime();

    List<IDirectory> javaClassPath = new ArrayList<>( _module.getJavaClassPath() );
    addJreJars( javaClassPath );

    IDirectory[] paths = javaClassPath.toArray(new IDirectory[0]);
    for (int i = paths.length - 1; i >= 0; i--) {
      IDirectory path = paths[i];
      addClassNames(path, path, _filter);
    }

//    int duration = (int)((System.nanoTime() - mark) / 1000000);
//    String logFile = "c:\\temp\\loadClasspathInfo.log";
//    DebugLogUtil.log( logFile, "loadClasspathInfo(): " + duration, true );
//    DebugLogUtil.log( logFile,
//      "TRACE\n" + Arrays.toString(new RuntimeException().fillInStackTrace().getStackTrace()) + "\n\n==============\n\n", true );
  }

  private void addJreJars( List<IDirectory> javaClassPath )
  {
    if( _module == TypeSystem.getGlobalModule() )
    {
      List<String> jreJars = getJreJars();
      javaClassPath.addAll( jreJars.stream()
        .map( uri -> CommonServices.getFileSystem()
          .getIDirectory( Paths.get( URI.create( uri ) ) ) )
        .collect( Collectors.toList() ) );
    }
  }

  private void addClassNames(final IDirectory root, IDirectory dir, final ClassPathFilter filter) {
    for (IFile file : dir.listFiles()) {
      if( isClassFileName( file.getName() ) )
      {
        String strClassName = getClassNameFromFile( root, file );
        if( isValidClassName( strClassName ) )
        {
          putClassName( file, strClassName, filter );
        }
      }
    }
    for (IDirectory subDir : dir.listDirs()) {
      addClassNames(root, subDir, filter);
    }
  }

  private void putClassName( final IFile file, String strClassName, ClassPathFilter filter )
  {
    boolean bFiltered = filter != null && !filter.acceptClass( strClassName );
    if( bFiltered )
    {
      // We need to store packages so we can resolve them in the gosu parser
      strClassName = getPlaceholderClassNameForFilteredPackage( strClassName );
    }
    if( strClassName != null ) {
      _cache.add(strClassName, file);
    }
  }

  static private String getPlaceholderClassNameForFilteredPackage( String strClassName )
  {
    int iIndex = strClassName.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      return strClassName.substring( 0, iIndex+1 ) + PLACEHOLDER_FOR_PACKAGE;
    }
    return null;
  }

  private String getClassNameFromFile( IDirectory root, IFile file )
  {
    String strQualifiedClassName = root.relativePath(file);
    if( !isClassFileName( strQualifiedClassName ) )
    {
      throw new IllegalArgumentException(
        file.getPath() + " is not a legal Java class name. " +
        "It does not end with " + CLASS_FILE_EXT );
    }
    strQualifiedClassName =
      strQualifiedClassName.substring( 0, strQualifiedClassName.length() -
                                          CLASS_FILE_EXT.length() );
    return strQualifiedClassName.replace('/', '.');
  }

  private boolean isClassFileName( String strFileName )
  {
    return strFileName.toLowerCase().endsWith( ".class" );
  }

  private boolean isValidClassName( String strClassName )
  {
    if (strClassName.endsWith("package-info")) {
      return false;
    }
    // look for private or anonymous inner classes
    int index = strClassName.lastIndexOf('$');
    return !(
	    _filter.isIgnoreAnonymous() &&
            index >= 0 && index < strClassName.length() - 1 &&
                    Character.isDigit(strClassName.charAt(index + 1))
    );
  }

  public boolean hasNamespace(String namespace) {
    FqnCacheNode infoNode = _cache.getNode(namespace);
    return infoNode != null && !infoNode.isLeaf();
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    FqnCacheNode<?> node = _cache.getNode(namespace);
    IDefaultTypeLoader defaultTypeLoader = _module.getModuleTypeLoader().getDefaultTypeLoader();
    if (node != null) {
      Set<TypeName> names = new HashSet<TypeName>();
      for (FqnCacheNode<?> child : node.getChildren()) {
        if (child.isLeaf()) {
          names.add(new TypeName(namespace + "." + child.getName(), defaultTypeLoader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC));
        } else {
          names.add(new TypeName(child.getName(), defaultTypeLoader, TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC));
        }
      }
      return names;
    } else {
      return Collections.emptySet();
    }
  }

  @Override
  public String toString() {
    return _module.getName();
  }

  private static List<String> getJreJars() {
    List<String> paths = new ArrayList<>();
    ClassLoader cl = Gosuc.class.getClassLoader();
    Path modulesPath = Paths.get( URI.create( "jrt:/modules" ) );
    while( cl == null || !BUILTIN_CLASSLOADER.get().isAssignableFrom( cl.getClass() ) )
    {
      ClassLoader parent = cl.getParent();
      if( parent == null )
      {
        ReflectUtil.FieldRef field = ReflectUtil.field( cl.getClass(), "parent" );
        if( field != null )
        {
          parent = (ClassLoader)field.get( cl );
        }
        if( field == null || parent == null )
        {
          field = ReflectUtil.field( cl.getClass(), "parentClassLoader" );
        }
        if( field != null )
        {
          parent = (ClassLoader)field.get( cl );
        }
      }
      cl = parent;
    }
    while( cl != null && BUILTIN_CLASSLOADER.get().isAssignableFrom( cl.getClass() ) )
    {
      //noinspection unchecked
      Map<String, Object> nameToModule =
        (Map<String, Object>)ReflectUtil.field( cl, "nameToModule" ).get();
      nameToModule.values().stream()
        .filter( mr -> (boolean)ReflectUtil.method( ReflectUtil.method( mr, "location" ).invoke(), "isPresent" ).invoke() )
        .forEach( mr ->
          paths.add( modulesPath.resolve(
            Paths.get( (URI)ReflectUtil.method( ReflectUtil.method( mr, "location" ).invoke(), "get" ).invoke() ).toString().substring( 1 ) ).toUri().toString() ) );
      cl = (ClassLoader)ReflectUtil.field( cl, "parent" ).get();
    }
    return paths;
  }
}
