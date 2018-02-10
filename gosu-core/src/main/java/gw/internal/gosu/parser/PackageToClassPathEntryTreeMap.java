/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.FileSystemGosuClassRepository.ClassFileInfo;
import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.module.IModule;
import gw.util.StringPool;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import manifold.api.fs.IDirectory;
import manifold.api.fs.IFile;
import manifold.api.type.TypeName;

/**
 */
class PackageToClassPathEntryTreeMap
{
  private String _strFullPackageName;
  private String _strRelativePackageName;
  private Map<String, PackageToClassPathEntryTreeMap> _children = new HashMap<String, PackageToClassPathEntryTreeMap>();
  private List<IFileSystemGosuClassRepository.ClassPathEntry> _classPathEntries = new ArrayList<IFileSystemGosuClassRepository.ClassPathEntry>();
  private PackageToClassPathEntryTreeMap _parent;
  private IModule _module;

  PackageToClassPathEntryTreeMap( PackageToClassPathEntryTreeMap parent, String packageName, IModule module )
  {
    _parent = parent;
    _module = module;
    _strRelativePackageName = packageName;
    if( parent != null && !parent._strFullPackageName.isEmpty() )
    {
      _strFullPackageName = parent._strFullPackageName + '.' + packageName;
    }
    else
    {
      _strFullPackageName = packageName;
    }
  }

  public void addClassPathEntry( IFileSystemGosuClassRepository.ClassPathEntry entry )
  {
    if( !_classPathEntries.contains( entry ) )
    {
      _classPathEntries.add( entry );
    }
  }

  public PackageToClassPathEntryTreeMap createChildForDir( IFileSystemGosuClassRepository.ClassPathEntry entry, String packageName )
  {
    PackageToClassPathEntryTreeMap packageTree = _children.get( packageName );
    if( packageTree == null )
    {
      packageTree = new PackageToClassPathEntryTreeMap( this, packageName, _module );
      _children.put( packageName, packageTree );
    }
    if(entry != null) {
      packageTree.addClassPathEntry( entry );
    }
    return packageTree;
  }

  public PackageToClassPathEntryTreeMap getChild( String relativePackage )
  {
    return _children.get( relativePackage );
  }

  public FileSystemGosuClassRepository.ClassFileInfo resolveToClassFileInfo(String strQualifiedClassName, String[] extensions) {
    if (strQualifiedClassName.length() <= _strFullPackageName.length()) {
      return null;
    }
    String remainingPart = _strFullPackageName.isEmpty()
                           ? strQualifiedClassName
                           : strQualifiedClassName.substring(_strFullPackageName.length() + 1);
    int dotIndex = remainingPart.indexOf('.');
    String fileName = remainingPart.substring(0, dotIndex == -1 ? remainingPart.length() : dotIndex);
    for (IFileSystemGosuClassRepository.ClassPathEntry classPathEntry : _classPathEntries) {
      FileSystemGosuClassRepository.ClassFileInfo info =
              getClassFileInfo(classPathEntry, fileName, dotIndex, remainingPart, extensions);
      if (info != null) {
        return info;
      }
    }
    return null;
  }

  public URL resolveToResource( String resourceName )
  {
    if( resourceName.length() <= _strFullPackageName.length() )
    {
      return null;
    }

    String remainingPart = resourceName.substring( _strFullPackageName.length() + 1 );
    for( IFileSystemGosuClassRepository.ClassPathEntry classPathEntry : _classPathEntries )
    {
      URL url = getResource( classPathEntry, remainingPart );
      if( url != null )
      {
        return url;
      }
    }
    return null;
  }

  private FileSystemGosuClassRepository.ClassFileInfo getClassFileInfo(
          IFileSystemGosuClassRepository.ClassPathEntry classPathEntry,
          String fileName, int dotIndex,
          String remainingPart,
          String[] extensions )
  {
    ClassFileInfo file = getFile( classPathEntry, fileName, extensions );
    if( file != null && dotIndex != -1 )
    {
      IType outerMostClass;
      try
      {
        outerMostClass = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( _strFullPackageName + "." + fileName );
      }
      catch( ClassNotFoundException e )
      {
        throw new RuntimeException( e );
      }
      List<String> innerClassParts = splitInnerClassPartsIntoList( dotIndex, remainingPart );
      if (!(outerMostClass instanceof IFileRepositoryBasedType)) {
        throw new RuntimeException("Type not a IFileRepositoryBasedType: " + outerMostClass);
      }
      return new FileSystemGosuClassRepository.ClassFileInfo( ((IFileRepositoryBasedType)outerMostClass).getSourceFileHandle(),
                                                                  ((IFileRepositoryBasedType) outerMostClass).getClassType(),
                                                                  _strFullPackageName + "." + fileName,
                                                                  innerClassParts,
                                                                  classPathEntry.isTestResource() );
    }
    else
    {
      return file;
    }
  }

  public static List<String> splitInnerClassPartsIntoList( int dotIndex, String remainingPart )
  {
    String[] parts = remainingPart.substring( dotIndex + 1 ).split("\\.");
    for( int i = 0; i < parts.length; i++ )
    {
      parts[i] = StringPool.get( parts[i] );
    }
    return Arrays.asList( parts );
  }

  private URL getResource( IFileSystemGosuClassRepository.ClassPathEntry root, String strFileName )
  {
    try
    {
      IFile possibleFile = getFile( root, strFileName);
      return possibleFile == null ? null : possibleFile.toURI().toURL();
    }  catch (IOException ex) {
        throw new RuntimeException(ex);
    }
  }

  private IFile getFile( IFileSystemGosuClassRepository.ClassPathEntry root, String strFileName )
  {
    IDirectory dir = getDir( root );
    return getFileMatchCase( dir, strFileName );
  }

  private ClassFileInfo getFile(
          IFileSystemGosuClassRepository.ClassPathEntry root,
          String strFileName, String[] extensions )
  {
    IDirectory dir = getDir( root );
    for( String ext : extensions )
    {
      IFile file = getFileMatchCase( dir, strFileName + ext );
      if( file != null )
      {
        return new FileSystemGosuClassRepository.ClassFileInfo( root, file, root.isTestResource() );
      }
    }
    return null;
  }

  private IFile getFileMatchCase( IDirectory dir, String strFileName ) {
    if (dir.hasChildFile(strFileName)) {
      IFile file = dir.file( strFileName );
      try
      {
        if( doesFileMatchCase( strFileName, file ) ) {
          return file;
        } else {
          return null;
        }
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    } else {
      return null;
    }
  }

  private boolean doesFileMatchCase( String strFileName, IFile file ) throws IOException
  {
    // This is primarily for Windows where the file system is case-insensitive
    return !file.isJavaFile() || file.toJavaFile().getCanonicalPath().endsWith( strFileName );
  }

  private IDirectory getDir(IFileSystemGosuClassRepository.ClassPathEntry root) {
    return _strFullPackageName.isEmpty()
            ? root.getPath()
            : root.getPath().dir(_strFullPackageName.replace('.', File.separatorChar));
  }

  public void delete( IDirectory dir ) {
    if( _classPathEntries.size() == 1 ) {
      _parent.removeChild(this);
    }
    else {
      for( IFileSystemGosuClassRepository.ClassPathEntry entry: _classPathEntries ) {
        if( dir.equals( entry.getPath() ) || dir.isDescendantOf( entry.getPath() ) ) {
          _classPathEntries.remove( entry );
          break;
        }
      }
    }
  }

  private void removeChild(PackageToClassPathEntryTreeMap pkg) {
    _children.remove(pkg._strRelativePackageName);
  }

  public PackageToClassPathEntryTreeMap getParent() {
    return _parent;
  }

  public int getSourceRootCount() {
    return _classPathEntries.size();
  }

  @Override
  public String toString() {
    return _strFullPackageName;
  }

  public Set<TypeName> getTypeNames( Set<String> extensions, ITypeLoader loader) {
    Set<TypeName> names = new HashSet<TypeName>();
    for (PackageToClassPathEntryTreeMap child : _children.values()) {
      String name = child._strFullPackageName;
      name = name.substring(name.lastIndexOf('.') + 1);
      names.add(new TypeName(name, loader, TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC));
    }
    for (IFileSystemGosuClassRepository.ClassPathEntry classPathEntry : _classPathEntries) {
      IDirectory entryPath = classPathEntry.getPath().dir(_strFullPackageName.replace('.', '/'));
      List<? extends IFile> files = entryPath.listFiles();
      for (IFile file : files) {
        String extension = "." + file.getExtension();
        if (extensions.contains(extension)) {
          String fqn = entryPath.relativePath(file).replace('/', '.');
          fqn = fqn.substring(0, fqn.lastIndexOf('.'));
          names.add(new TypeName(_strFullPackageName + "." + fqn, loader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC));
        }
      }
    }
    return names;
  }
}
