/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.fs.IncludeModuleDirectory;
import gw.lang.parser.FileSource;
import gw.lang.parser.ISource;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IModule;
import gw.util.DynamicArray;
import gw.util.Pair;
import gw.util.StreamUtil;
import gw.util.cache.FqnCache;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
public class FileSystemGosuClassRepository implements IFileSystemGosuClassRepository
{
  private final Map<String, FqnCache> _missCaches = new HashMap<String, FqnCache>();
  public static final String RESOURCE_LOCATED_W_CLASSES = "gw/config/default.xml";

  private final IModule _module;

  // Source paths
  private List<ClassPathEntry> _sourcePath = new CopyOnWriteArrayList<ClassPathEntry>();
  // Excluded paths
  private Set<IDirectory> _excludedPath = new HashSet<IDirectory>();
  private String[] _extensions = new String[0];

  // Types and packages in the source paths
  private PackageToClassPathEntryTreeMap _rootNode;
  private Set<String> _allTypeNames;

  public FileSystemGosuClassRepository(IModule module)
  {
    _module = module;
    _extensions = GosuClassTypeLoader.ALL_EXTS;
  }

  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public IDirectory[] getSourcePath()
  {
    IDirectory[] sourcepath = new IDirectory[_sourcePath.size()];
    int i = 0;
    for( ClassPathEntry e : _sourcePath)
    {
      sourcepath[i++] = e.getPath();
    }
    return sourcepath;
  }

  @Override
  public void setSourcePath(IDirectory[] sourcePath)
  {
    if( areDifferent( sourcePath, _sourcePath) )
    {
      _sourcePath.clear();

      Set<String> extensions = new HashSet<String>();
      extensions.add(".java");
      extensions.add(".xsd");
      extensions.addAll(Arrays.asList(GosuClassTypeLoader.ALL_EXTS));

      // Scan for potential extensions
      for( IDirectory file : sourcePath )
      {
        _sourcePath.add( new ClassPathEntry( file, isTestFolder(file)) );
      }
      _extensions = extensions.toArray(new String[extensions.size()]);

      reset();
    }
  }

  @Override
  public IDirectory[] getExcludedPath() {
    return _excludedPath.toArray(new IDirectory[_excludedPath.size()]);
  }

  @Override
  public void setExcludedPath(IDirectory[] excludedPath) {
    _excludedPath = new HashSet<IDirectory>(Arrays.asList(excludedPath));
    reset();
  }

  @Override
  public ISourceFileHandle findClass(String strQualifiedClassName, String[] extensions)
  {
    IClassFileInfo fileInfo = findFileInfoOnDisk( strQualifiedClassName, extensions );
    return fileInfo != null ? fileInfo.getSourceFileHandle() : null;
  }

  @Override
  public URL findResource( String resourceName )
  {
    if( resourceName == null )
    {
      return null;
    }

    resourceName = resourceName.replace( '/', '.' );

    if( inMissCache( resourceName, _extensions) )
    {
      return null;
    }

    if( resourceName.endsWith( "." ) )
    {
      // Handle case where someone types "com.abc.Foo.". Otherwise it would be
      // treated as if the last dot was not there using the tokenizer.
      return null;
    }

    PackageToClassPathEntryTreeMap aPackage = getCachedPackage( resourceName );
    URL resource = aPackage == null ? null : aPackage.resolveToResource( resourceName );

    if( resource == null )
    {
      addToMissCache( resourceName, _extensions);
    }

    return resource;
  }

  @Override
  public Set<String> getAllTypeNames()
  {
    if (_allTypeNames == null) {
      Set<String> classNames = new HashSet<String>();
      for( ClassPathEntry path : _sourcePath)
      {
        addTypeNames(path.getPath(), path.getPath(), classNames, _extensions);
      }
      _allTypeNames = classNames;
    }
    return _allTypeNames;
  }

  @Override
  public Set<String> getAllTypeNames(String... extensions)
  {
    Set<String> enhancementNames = new HashSet<String>();
    for( ClassPathEntry path : _sourcePath)
    {
      addTypeNames(path.getPath(), path.getPath(), enhancementNames, extensions);
    }
    return enhancementNames;
  }

  @Override
  public String getClassNameFromFile( IDirectory root, IFile file, String[] fileExts )
  {
    String strClassPath = root.getPath().getFileSystemPathString() + File.separatorChar;

    String strQualifiedClassName = file.getPath().getFileSystemPathString().substring( strClassPath.length() );
    if( !Util.isClassFileName( strQualifiedClassName, fileExts ) )
    {
      String strExts = "";
      for( String strExt : fileExts )
      {
        strExts += " " + strExt;
      }
      throw new IllegalArgumentException( file.getPath().getName() + " is not a legal Gosu class name. It does not end with [" + strExts.trim() + "]" );
    }
    strQualifiedClassName =
      strQualifiedClassName.substring( 0, strQualifiedClassName.lastIndexOf( '.' ) );
    strQualifiedClassName = strQualifiedClassName.replace( '/', '.' ).replace( '\\', '.' );
    if( strQualifiedClassName.startsWith( "." ) )
    {
      strQualifiedClassName = strQualifiedClassName.substring( 1 );
    }
    return strQualifiedClassName;
  }

  @Override
  public void typesRefreshed(RefreshRequest request) {
    synchronized (_missCaches) {
      if (request == null) {
        reset();
      } else {
        for (FqnCache cache : _missCaches.values()) {
          cache.remove(request.types);
        }

        if (request.kind == RefreshKind.CREATION) {
          for (String type : request.types) {
            // cannot make this call because we have no way of finding out the right package for inner types
//            addToPackageCache(type, request.file);
            if (_allTypeNames != null) {
              _allTypeNames.add(type);
            }
          }
        } else if (request.kind == RefreshKind.DELETION) {
          if (_allTypeNames != null) {
            for (String type : request.types) {
              _allTypeNames.remove(type);
            }
          }
        }
      }
    }
  }

  private void reset() {
    synchronized (_missCaches) {
      _missCaches.clear();
      _rootNode = null;
      _allTypeNames = null;
    }
  }

  private void addToPackageCache(String fqn, IResource file) {
    final ClassPathEntry classPathEntry = findClassPathEntry(file);
    if (_rootNode != null && classPathEntry != null) {
      PackageToClassPathEntryTreeMap node = _rootNode;
      while (fqn != null) {
        int i = fqn.indexOf('.');
        String segment = fqn.substring(0, i < 0 ? fqn.length() : i);
        PackageToClassPathEntryTreeMap child = node.getChild(segment);
        if (child != null) {
          child.addClassPathEntry(classPathEntry);
          node = child;
        } else {
          node = node.createChildForDir(classPathEntry, segment);
        }
        if (i < 0) {
          break;
        }
        fqn = i + 1 < fqn.length() ? fqn.substring(i + 1) : null;
      }
      for( FqnCache cache : _missCaches.values() ) {
        cache.remove( fqn );
      }
    }
  }

  private ClassPathEntry findClassPathEntry(IResource file) {
    for (ClassPathEntry classPathEntry : _sourcePath) {
      if (file.isDescendantOf(classPathEntry.getPath())) {
        return classPathEntry;
      }
    }
    return null;
  }

  private void removeFromPackageCache( String fqn, IDirectory dir ) {
    PackageToClassPathEntryTreeMap thePackage = getCachedPackageCorrectly(fqn);
    if (thePackage != null) {
      //## todo: the package could be split, we need to remove the directory
      // and only if its the last one them remove the package
      thePackage.delete( dir );
    }
  }

  private synchronized PackageToClassPathEntryTreeMap getCachedPackageCorrectly(String fullyQualifiedName) {
    if (_rootNode == null) {
      _rootNode = loadPackageRoots();
    }

    PackageToClassPathEntryTreeMap currNode = _rootNode;
    int iRelativeNameIndex = 0;
    while (iRelativeNameIndex != -1) {
      int iNextDot = fullyQualifiedName.indexOf('.', iRelativeNameIndex);
      String strRelativeName = fullyQualifiedName.substring(iRelativeNameIndex, iNextDot == -1 ? fullyQualifiedName.length() : iNextDot);
      iRelativeNameIndex = iNextDot == -1 ? -1 : iNextDot + 1;
      PackageToClassPathEntryTreeMap newNode = getChildPackage(currNode, strRelativeName);
      if (newNode == null) {
        return null;
      }
      currNode = newNode;
    }

    return currNode == _rootNode ? null : currNode;
  }

  private synchronized PackageToClassPathEntryTreeMap getCachedPackage( String fullyQualifiedName )
  {
    if( _rootNode == null )
    {
      _rootNode = loadPackageRoots();
    }

    if( fullyQualifiedName.equals( "" ) )
    {
      return _rootNode;
    }

    PackageToClassPathEntryTreeMap currNode = _rootNode;
    int iRelativeNameIndex = 0;
    while( iRelativeNameIndex != -1 )
    {
      int iNextDot = fullyQualifiedName.indexOf( '.', iRelativeNameIndex );
      String strRelativeName = fullyQualifiedName.substring( iRelativeNameIndex, iNextDot == -1 ? fullyQualifiedName.length() : iNextDot );
      iRelativeNameIndex = iNextDot == -1 ? -1 : iNextDot + 1;
      PackageToClassPathEntryTreeMap newNode = getChildPackage( currNode, strRelativeName );
      if( newNode == null )
      {
        break;
      }
      currNode = newNode;
    }

    return currNode == _rootNode ? null : currNode;
  }

  private PackageToClassPathEntryTreeMap getChildPackage( PackageToClassPathEntryTreeMap parent, String strRelativeName )
  {
    PackageToClassPathEntryTreeMap child;
    if( parent == _rootNode && strRelativeName.equals( "Libraries" ) )
    {
      // Hack to support mixed case access to the "libraries" package.
      // Libaries used to be a global symbol with name "Libraries", so
      // there used to be a lot of access by that name.
      child = parent.getChild( "libraries" );
      if( child == null )
      {
        return null;
      }
    }
    else
    {
      child = parent.getChild( strRelativeName );
    }
    return child;
  }

  private PackageToClassPathEntryTreeMap loadPackageRoots()
  {
    PackageToClassPathEntryTreeMap root = new PackageToClassPathEntryTreeMap( null, "", _module );
    PackageToClassPathEntryTreeMap gw = root.createChildForDir( null, "gw" );
    gw.createChildForDir( null, "lang" );
    gw.createChildForDir( null, "util" );
    root.createChildForDir(null, IGosuProgram.PACKAGE);
    for( ClassPathEntry dir : _sourcePath)
    {
      root.addClassPathEntry( dir );
      processDirectory( root, dir, dir.getPath() );
    }
    return root;
  }

  private void processDirectory(PackageToClassPathEntryTreeMap node, IFileSystemGosuClassRepository.ClassPathEntry entry, IDirectory path) {
    if (_excludedPath.contains(path)) {
      return;
    }
    IDirectory entryPath = entry.getPath();
    if (entryPath.equals(path) || !CommonServices.getPlatformHelper().isPathIgnored(entryPath.relativePath(path))) {
      List<? extends IDirectory> dirs = path.listDirs();
      for (IDirectory dir : dirs) {
        if (isValidDirectory(dir)) {
          PackageToClassPathEntryTreeMap child = node.createChildForDir(entry, dir.getName());
          processDirectory(child, entry, dir);
        }
      }
    }
  }

  private boolean isValidDirectory(IDirectory dir) {
    return !dir.getName().equals("META-INF");
  }

  private ClassFileInfo findFileInfoOnDisk( String strQualifiedClassName, String[] extensions )
  {
    if( inMissCache( strQualifiedClassName, extensions ) )
    {
      return null;
    }

    if( strQualifiedClassName == null )
    {
      return null;
    }

    if( strQualifiedClassName.endsWith( "." ) )
    {
      // Handle case where someone types "com.abc.Foo.". Otherwise it would be
      // treated as if the last dot was not there using the tokenizer.
      return null;
    }

    ClassFileInfo info = null;
    String packageName = getPackageName(strQualifiedClassName);
    PackageToClassPathEntryTreeMap aPackage = getCachedPackage(packageName);
    if( aPackage != null )
    {
      info = aPackage.resolveToClassFileInfo( strQualifiedClassName, extensions );
    }

    if( info == null )
    {
      addToMissCache( strQualifiedClassName, extensions );
    }

    return info;
  }

  private String getPackageName(String strQualifiedClassName) {
    int i = strQualifiedClassName.lastIndexOf('.');
    return i < 0 ? "" : strQualifiedClassName.substring(0, i);
  }

  private boolean inMissCache(String strQualifiedClassName, String[] extensions)
  {
    synchronized( _missCaches )
    {
      // Note we check for TRUE because it can happen that a subordinate type like Foo<BadType> is a miss, while Foo is not a miss
      for (String extension : extensions) {
        Object value = getMissCacheForExtension(extension).get(strQualifiedClassName);
        if (value != Boolean.TRUE) {
          return false;
        }
      }

      return true;
    }
  }

  private FqnCache getMissCacheForExtension(String extension) {
    FqnCache cache = _missCaches.get(extension);
    if (cache == null) {
      cache = new FqnCache();
      _missCaches.put(extension, cache);
    }
    return cache;
  }

  private void addToMissCache(String strQualifiedClassName, String[] extensions)
  {
    synchronized( _missCaches )
    {
      for (String extension : extensions) {
        getMissCacheForExtension(extension).add(strQualifiedClassName, Boolean.TRUE);
      }
    }
  }

  private void addTypeNames( final IDirectory root, IDirectory path, final Set<String> classNames, final String[] fileExts )
  {
    DynamicArray<? extends IFile> iFiles = IDirectoryUtil.allContainedFilesExcludingIgnored(path);
    for (int i = 0; i < iFiles.size; i++) {
      IFile file = (IFile) iFiles.data[i];
      if (Util.isClassFileName(file.getName(), fileExts)) {
        String className = getClassNameFromFile(root, file, fileExts);
        classNames.add(className);
      }
    }
  }

  public static final class FileSystemSourceFileHandle implements ISourceFileHandle
  {
    ISource _source;
    boolean _isTestClass;
    private int _classPathLength;
    private ClassFileInfo _fileInfo;
    private int _iOffset;
    private int _iEnd;

    public FileSystemSourceFileHandle( ClassFileInfo fileInfo, boolean isTestClass )
    {
      _isTestClass = isTestClass;
      _fileInfo = fileInfo;
      _classPathLength = fileInfo.getClassPathLength();
    }

    @Override
    public ISource getSource()
    {
      if( _source == null )
      {
        _source = new FileSource(_fileInfo);
      }
      return _source;
    }

    @Override
    public String getParentType()
    {
      return null;
    }

    @Override
    public String getNamespace() {
      String namespace = _fileInfo.getFilePath().substring(_fileInfo.getEntry().getPath().toString().length() + 1);
      int fileSeparatorIndex = namespace.lastIndexOf(File.separatorChar);
      if (fileSeparatorIndex >= 0) {
        namespace = namespace.substring(0, fileSeparatorIndex);
        namespace = namespace.replace(File.separatorChar, '.');
      } else {
        namespace = "default";
      }
      return namespace;
    }

    @Override
    public String getFilePath()
    {
      return _fileInfo.getFilePath();
    }

    @Override
    public IFile getFile() {
      return _fileInfo.getFile();
    }

    @Override
    public boolean isTestClass()
    {
      return _isTestClass;
    }

    @Override
    public boolean isValid()
    {
      return true;
    }

    @Override
    public boolean isStandardPath()
    {
      return !_fileInfo.getEntry().getPath().isAdditional();
    }

    @Override
    public boolean isIncludeModulePath()
    {
      return _fileInfo.getEntry().getPath() instanceof IncludeModuleDirectory;
    }

    @Override
    public void cleanAfterCompile()
    {
      _source = null;
    }

    public ClassType getClassType()
    {
      String name = _fileInfo.getNonCanonicalFileName();
      return ClassType.getFromFileName(name);
    }

    @Override
    public String getTypeNamespace()
    {
      String path = _fileInfo.getFilePath().replace( '/', '.' ).replace( '\\', '.' );
      int startPos = _classPathLength;
      // this is a hack to support files inside jars in IJ
      if( path.charAt( startPos ) == '!' )
      {
        ++startPos;
      }
      if( path.charAt( startPos ) == '.' )
      {
        ++startPos;
      }
      int endPos = path.length() - (_fileInfo.getFileName().length() + 1);
      return endPos < startPos ? "" : path.substring( startPos, endPos );
    }

    @Override
    public String getRelativeName()
    {
      String name = _fileInfo.getFileName();
      return name.substring( 0, name.lastIndexOf( '.' ) );
    }

//    @Override
//    public IDirectory getParentFile()
//    {
//      return _fileInfo.getParentFile();
//    }
//
//    @Override
//    public ClassFileInfo getFileInfo()
//    {
//      return _fileInfo;
//    }

    @Override
    public void setOffset( int iOffset )
    {
      _iOffset = iOffset;
    }
    @Override
    public int getOffset()
    {
      return _iOffset;
    }

    @Override
    public void setEnd( int iEnd )
    {
      _iEnd = iEnd;
    }
    @Override
    public int getEnd()
    {
      return _iEnd;
    }

    @Override
    public String getFileName()
    {
      String strFile = this.getFilePath();
      int iIndex = strFile.lastIndexOf( File.separatorChar );
      if( iIndex >= 0 )
      {
        strFile = strFile.substring( iIndex + 1 );
      }
      return strFile;
    }

    @Override
    public String toString() {
      return _fileInfo.getFilePath();
    }
  }

  public static class ClassFileInfo implements IFileSystemGosuClassRepository.IClassFileInfo
  {
    private ClassPathEntry _entry;
    private ClassType _classType;
    private String _fileType;
    private List<String> _innerClassParts;
    private boolean _isTestClass;
    private IFile _file;

    public ClassFileInfo( ClassPathEntry entry, IFile file, boolean isTestClass )
    {
      _entry = entry;
      _file = file;
      _classType = _file.getExtension().equalsIgnoreCase("java") ? ClassType.JavaClass : ClassType.Class;
      _innerClassParts = Collections.emptyList();
      _isTestClass = isTestClass;
      _file = file;
    }

    public ClassFileInfo( ISourceFileHandle outerSfh, ClassType classType, String fileType, List<String> innerClassParts, boolean isTestClass )
    {
      _classType = classType;
      _fileType = fileType;
      _innerClassParts = innerClassParts;
      _isTestClass = isTestClass;
      _file = outerSfh.getFile();
    }

    @Override
    public IFile getFile() {
      return _file;
    }

    @Override
    public boolean hasInnerClass()
    {
      return _innerClassParts.size() > 0;
    }

    @Override
    public ISourceFileHandle getSourceFileHandle()
    {
      if( hasInnerClass() )
      {
        String enclosingType = _fileType;
        for( int i = 0; i < _innerClassParts.size() - 1; i++ )
        {
          enclosingType += "." + _innerClassParts.get( i );
        }
        return new InnerClassFileSystemSourceFileHandle(_classType, enclosingType, _innerClassParts.get( _innerClassParts.size() - 1 ), _isTestClass );
      }
      return new FileSystemSourceFileHandle( this, _isTestClass );
    }

    @Override
    public ClassPathEntry getEntry()
    {
      return _entry;
    }

    @Override
    public IDirectory getParentFile() {
      return _file.getParent();
    }

    @Override
    public Reader getReader() {
      try {
        return StreamUtil.getInputStreamReader(_file.openInputStream());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public String getFileName() {
      return _file.getName();
    }

    @Override
    public String getNonCanonicalFileName() {
      return _file.getName();
    }

    @Override
    public String getFilePath() {
      return _file.getPath().getFileSystemPathString();
    }

    @Override
    public int getClassPathLength() {
      return getEntry().getPath().getPath().getFileSystemPathString().length();
    }

    @Override
    public String getContent() {
      Reader reader = getReader();
      try
      {
        char[] buf = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
          int count = reader.read(buf);
          if (count < 0) {
            break;
          }
          stringBuilder.append(buf, 0, count);
        }

        char[] processedChars = new char[stringBuilder.length()];
        int j = 0;
        for (int i = 0; i < processedChars.length; i++) {
          char c1 = stringBuilder.charAt(i);
          if (i < processedChars.length - 1 && c1 == '\r' && stringBuilder.charAt(i + 1) == '\n') {
            processedChars[j] = '\n';
            i++;
          } else {
            processedChars[j] = c1;
          }
          j++;
        }

        String s = new String(processedChars, 0, j);
        return s;
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      } finally {
        try {
          if (reader != null) {
            reader.close();
          }
        } catch (IOException e) {}
      }
    }

    public String toString() {
      return _file.toString();
    }
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace, Set<String> extensions, ITypeLoader loader) {
    Set<TypeName> setNames = new HashSet<TypeName>();
    if (namespace == null) {
      for (String name : getAllTypeNames()) {
        setNames.add(new TypeName(name, loader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC));
      }
    } else {
      PackageToClassPathEntryTreeMap cachedPackage = getCachedPackageCorrectly(namespace);
      if (cachedPackage != null) {
        return cachedPackage.getTypeNames(extensions, loader);
      } else {
        return Collections.EMPTY_SET;
      }
    }
    return setNames;
  }

  @Override
  public int hasNamespace(String namespace) {
    PackageToClassPathEntryTreeMap cachedNamespace = getCachedPackageCorrectly( namespace );
    return cachedNamespace != null ? cachedNamespace.getSourceRootCount() : 0;
  }

  @Override
  public void namespaceRefreshed(String namespace, IDirectory dir, RefreshKind kind) {
    if (kind == RefreshKind.CREATION) {
      addToPackageCache(namespace, dir);
    } else if (kind == RefreshKind.DELETION) {
      removeFromPackageCache(namespace, dir);
    }
  }

  private boolean areDifferent(IDirectory[] cp1, List<ClassPathEntry> cp2) {
    if (cp1.length != cp2.size()) {
      return true;
    }
    for (int i = 0; i < cp1.length; i++) {
      if (!cp1[i].equals(cp2.get(i).getPath())) {
        return true;
      }
    }
    return false;
  }

  private static boolean isTestFolder(IDirectory file) {
    return file.toString().endsWith( "gtest" );
  }

  @Override
  public String getResourceName(URL url) {
    IFile file = CommonServices.getFileSystem().getIFile(url);
    String resourceName = _module.pathRelativeToRoot(file);
    if (resourceName == null) {
      throw new RuntimeException("Could not find resource " + url);
    }

    return resourceName;
  }

  public List<Pair<String, IFile>> findAllFilesByExtension(String extension) {
    List<Pair<String, IFile>> results = new ArrayList<Pair<String, IFile>>();

    for (IDirectory dir : _module.getRoots()) {
      IDirectory configDir = dir.dir(IModule.CONFIG_RESOURCE_PREFIX);
      if (configDir.exists()) {
        addAllLocalResourceFilesByExtensionInternal(IModule.CONFIG_RESOURCE_PREFIX, configDir, extension, results);
      }
    }

    for (IDirectory sourceEntry : _module.getSourcePath()) {
      if (sourceEntry.exists() && !sourceEntry.getName().equals(IModule.CONFIG_RESOURCE_PREFIX)) {
        addAllLocalResourceFilesByExtensionInternal("", sourceEntry, extension, results);
      }
    }
    return results;
  }

  private void addAllLocalResourceFilesByExtensionInternal(String relativePath, IDirectory dir, String extension, List<Pair<String, IFile>> results) {
    if (_excludedPath.contains(dir)) {
      return;
    }
    if (!CommonServices.getPlatformHelper().isPathIgnored(relativePath)) {
      for (IFile file : dir.listFiles()) {
        if (file.getName().endsWith(extension)) {
          String path = appendResourceNameToPath(relativePath, file.getName());
          results.add(new Pair<String, IFile>(path, file));
        }
      }
      for (IDirectory subdir : dir.listDirs()) {
        String path = appendResourceNameToPath(relativePath, subdir.getName());
        addAllLocalResourceFilesByExtensionInternal(path, subdir, extension, results);
      }
    }
  }

  private static String appendResourceNameToPath( String relativePath, String resourceName ) {
    String path;
    if ( relativePath.length() > 0 ) {
      path = relativePath + '/' + resourceName;
    }
    else {
      path = resourceName;
    }
    return path;
  }

  @Override
  public IFile findFirstFile(String resourceName) {
    if (resourceName.startsWith(IModule.CONFIG_RESOURCE_PREFIX) || resourceName.startsWith(IModule.CONFIG_RESOURCE_PREFIX_2)) {
      return findFirstFile(resourceName, _module.getRoots());
    } else {
      return findFirstFile(resourceName, _module.getSourcePath());
    }
  }

  private IFile findFirstFile(String resourceName, List<? extends IDirectory> searchPath) {
    for (IDirectory dir : searchPath) {
      IFile file = dir.file(resourceName);
      if (file != null && file.exists()) {
        return file;
      }
    }

    return null;
  }

  public String toString() {
    return _module.getName();
  }
}
