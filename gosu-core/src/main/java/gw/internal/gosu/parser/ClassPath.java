/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.module.fs.FileSystemImpl;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IFileSystem;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassPath implements IClassPath
{
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
    List<IDirectory> javaClassPath = _module.getJavaClassPath();
    IDirectory[] paths = javaClassPath.toArray(new IDirectory[javaClassPath.size()]);
    for (int i = paths.length - 1; i >= 0; i--) {
      IDirectory path = paths[i];
      addClassNames(path, path, _filter);
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
}
