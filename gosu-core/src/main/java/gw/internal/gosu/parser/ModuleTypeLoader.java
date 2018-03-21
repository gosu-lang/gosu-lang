/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IExtendedTypeLoader;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.reflect.IUninitializableTypeLoader;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.Pair;
import gw.util.Predicate;
import gw.util.cache.FqnCacheNode;
import gw.util.cache.WeakFqnCache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class ModuleTypeLoader implements ITypeLoaderStackInternal {
  private static final IType CACHE_MISS = ErrorType.getInstance( "cache-miss-type" );

  // The corresponding module
  private IModule _module;

  // Type loader data structures
  private List<ITypeLoader> _globalStack;
  private DefaultTypeLoader _defaultTypeLoader;
  private Map<String, ITypeLoader> _loadersByPrefix;

  // Type system caches
  private WeakFqnCache<IType> _typesByName;
  private Map<String, IType> _namespaceTypesByName; // A case-Sensitive map of names to namespace types

  //## todo: remove this pos
  private Map<String, IType> _typesByCaseInsensitiveName; // A case-Insensitive map of names to intrinsic types

  private ITypeRefFactory _typeRefFactory;

  public ModuleTypeLoader( IModule module, List<ITypeLoader> loaderStack )
  {
    _module = module;

    initMaps();
    _globalStack.addAll(loaderStack);
    _typeRefFactory = module.getModuleTypeLoader().getTypeRefFactory();

    for (ITypeLoader typeLoader : loaderStack) {
      List<String> handledPrefixes = typeLoader.getHandledPrefixes();
      for( int i = 0; i < handledPrefixes.size(); i++ )
      {
        String handledPrefix = handledPrefixes.get( i );
        _loadersByPrefix.put( handledPrefix, typeLoader );
      }
    }
  }

  private void initMaps() {
    _globalStack = new ArrayList<ITypeLoader>();
    _loadersByPrefix = new HashMap<String, ITypeLoader>();
    _typesByName = new WeakFqnCache<IType>();
    _namespaceTypesByName = new HashMap<String, IType>();
    _typesByCaseInsensitiveName = new HashMap<String, IType>();
  }

  public ModuleTypeLoader( IModule module, DefaultTypeLoader defaultTypeLoader)
  {
    _module = module;
    _defaultTypeLoader = defaultTypeLoader;
    _typeRefFactory = new TypeRefFactory();

    reset();
  }

  public void reset()
  {
    initMaps();
    _globalStack.add( _defaultTypeLoader );
    _typeRefFactory.clearCaches();
  }

  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public List<ITypeLoader> getTypeLoaders()
  {
    return new ArrayList<ITypeLoader>( _globalStack );
  }

  public void pushTypeLoader( ITypeLoader typeLoader )
  {
    TypeSystem.lock();
    try
    {
      clearErrorTypes();
      int position = -1;
      for( int i = 0; i < _globalStack.size(); i++ )
      {
        ITypeLoader loaderOnStack = _globalStack.get( i );
        if( loaderOnStack.getClass() == typeLoader.getClass() )
        {
          position = i;
        }
      }
      if( position == -1 )
      {
        _globalStack.add( 0, typeLoader );
      }
      else
      {
        _globalStack.set( position, typeLoader );
      }
      List<String> handledPrefixes = typeLoader.getHandledPrefixes();
      for( int i = 0; i < handledPrefixes.size(); i++ )
      {
        String handledPrefix = handledPrefixes.get( i );
        _loadersByPrefix.put( handledPrefix, typeLoader );
      }
      CommonServices.getEntityAccess().getLogger().debug("TypeLoader added: " + GosuClassUtil.getShortClassName(typeLoader.getClass()));
    }
    finally
    {
      TypeSystem.unlock();
    }
    CommonServices.getEntityAccess().getLogger().debug( "TypeLoader added: " + GosuClassUtil.getShortClassName( typeLoader.getClass() ) );
  }

  @Override
  public void clearErrorTypes()
  {
    TypeSystem.lock();
    try
    {
      removeMissesAndErrorsFromMainCache();
      removeMissesAndErrors( _typesByCaseInsensitiveName.values() );
      removeMissesAndErrors( _namespaceTypesByName.values() );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void removeMissesAndErrorsFromMainCache() {
    _typesByName.visitNodeDepthFirst(new Predicate<FqnCacheNode>() {
      public boolean evaluate(FqnCacheNode node) {
        WeakReference<IType> ref = (WeakReference<IType>) node.getUserData();
        if (ref != null) {
          IType type = ref.get();
          if (type == CACHE_MISS || type instanceof ErrorType) {
            node.delete();
          }
        }
        return true;
      }
    });
  }

  private void removeMissesAndErrors( Collection<IType> types ) {
    for( Iterator<IType> iterator = types.iterator(); iterator.hasNext(); )
    {
      IType type = iterator.next();
      if( type == null ||
          type instanceof ErrorType ||
          type == CACHE_MISS )
      {
        iterator.remove();
      }
    }
  }

  private void clearCaches()
  {
    _typesByName.clear();
    _namespaceTypesByName.clear();
    _typesByCaseInsensitiveName.clear();
  }

  public void removeTypeLoader( Class<? extends ITypeLoader> loaderType )
  {
    TypeSystem.lock();
    try
    {
      ITypeLoader typeLoader = getTypeLoader( loaderType );
      if( typeLoader != null )
      {
        _globalStack.remove( typeLoader );
        // Removing a type loader must trigger a type system reset
        refreshed();
        List<String> handledPrefixes = typeLoader.getHandledPrefixes();
        for( String handledPrefix : handledPrefixes )
        {
          _loadersByPrefix.remove( handledPrefix );
        }
        CommonServices.getEntityAccess().getLogger().debug("TypeLoader removed: " + GosuClassUtil.getShortClassName(typeLoader.getClass()));
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }


  public void clearFromCaches( RefreshRequest request)
  {
    TypeSystem.lock();
    try
    {
      for (String fullyQualifiedTypeName : request.types) {
        clearFromCaches(fullyQualifiedTypeName);
      }
      clearNamespaces(request); // Clear namespace types that might be affected.

      DefaultTypeLoader defaultTypeLoader = getTypeLoader(DefaultTypeLoader.class);
      if (defaultTypeLoader != null) {
        defaultTypeLoader.clearMisses();
      }

      // This is vital for runtime where Java types are redefined in via debugger
      JavaMethodCache.flushCaches();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void clearNamespaces(RefreshRequest request) {
    // Type could be added, removed or renamed.
    for (String fullyQualifiedTypeName : request.types) {
      int pos = fullyQualifiedTypeName.lastIndexOf('.');
      if (pos != -1) {
        String pkgName = fullyQualifiedTypeName.substring(0, pos);
        _namespaceTypesByName.remove(pkgName);
      }
    }
  }

  public Set<TypeName> getTypeNames(String namespace) {
    Set<TypeName> names = new HashSet<TypeName>();
    for (ITypeLoader loader : _globalStack) {
      names.addAll(loader.getTypeNames(namespace));
    }
    return names;
  }

  @Override
  public <T extends ITypeLoader> T getTypeLoader( Class<? extends T> loaderType )
  {
    TypeSystem.lock();
    try
    {
      for( ITypeLoader loader : _globalStack )
      {
        // Note, this MUST be equals(). It must be an exact match, not an assignable match.
        if( loader.getClass().equals( loaderType ) )
        {
          //noinspection unchecked
          return (T)loader;
        }
      }
      return null;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  @Override
  public INamespaceType getNamespaceType( String strNamespace )
  {
    if( !isValidNamespace( strNamespace ) )
    {
      return null;
    }

    // First, look for the type in the map by name
    IType foundType = _namespaceTypesByName.get(strNamespace);
    if( foundType == null )
    {
      TypeSystem.lock();
      try
      {
        // Look it up again, to make sure no concurrent write has happened
        foundType = _namespaceTypesByName.get( strNamespace );
        // If it's not found, then go ahead and try to load it from the type loader stacks
        if( foundType == null )
        {
          foundType = loadNamespaceAndCacheResult(strNamespace);
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    if( foundType == CACHE_MISS )
    {
      return null;
    }
    else if( foundType instanceof INamespaceType )
    {
      return (INamespaceType)foundType;
    }
    else
    {
      return null;
    }
  }

  private boolean isValidNamespace( String strNamespace )
  {
    // invalid if empty, starts with dot, or ends with dot
    return strNamespace.length() > 0 &&
           strNamespace.charAt( 0 ) != '.' &&
           strNamespace.charAt( strNamespace.length() - 1 ) != '.';
  }

  @Override
  public IType getIntrinsicTypeFromObject( Object object )
  {
    IType type = null;
    int iLoaders = _globalStack.size();
    for( int i = 0; i < iLoaders; i++ ) //!! don't use iterator here; concurrency issue; we don't want to lock either
    {
      ITypeLoader loader;
      try
      {
        loader = _globalStack.get( i );
      }
      catch( IndexOutOfBoundsException e )
      {
        break;
      }
      if( loader instanceof IExtendedTypeLoader )
      {
        if(loader instanceof IGosuObject) {
          if(GosuClassCompilingStack.getCompilingType(((IGosuObject)loader).getIntrinsicType().getName()) != null) {
            continue;
          }
        }
        type = ((IExtendedTypeLoader)loader).getIntrinsicTypeFromObject( object );
        if( type != null )
        {
          break;
        }
      }
    }
    if( type == null && _defaultTypeLoader != null)
    {
      type = _defaultTypeLoader.getIntrinsicTypeFromObject( object );
    }
    if( type instanceof IMetaType && !((IMetaType)type).isLiteral() )
    {
      // Ensure we return the "literal" version of the metatype; it has both the metatype features and the type's static features.
      type = MetaType.getLiteral( ((IMetaType)type).getType() );
    }
    return type;
  }

  public IType getTypeByFullNameIfValid( String fullyQualifiedName, boolean skipJava )
  {
    // strip off all trailing array brackets "[]"
    String fqnNoArrays = stripArrayBrackets(fullyQualifiedName);

    // First, look for the type in the map by name
    IType foundType = findInCache( fqnNoArrays );
    if( foundType == null )
    {
      TypeSystem.lock();
      try
      {
        // Look it up again, to make sure no concurrent write has happened
        foundType = findInCache( fqnNoArrays );

        // If still null, check in the case-insensitive map and add it to the case-sensitive version if necessary
        if( foundType == null )
        {
          foundType = findInCaseInsenstiveCache( fqnNoArrays );
          if( foundType != null )
          {
            _typesByName.add( fqnNoArrays, foundType );
          }
        }
        // If it's not found, then go ahead and try to load it from the type loader stacks
        if( foundType == null )
        {
          foundType = loadTypeAndCacheResult(fqnNoArrays, skipJava);
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    if( foundType == CACHE_MISS )
    {
      return null;
    }
    // restore arrays
    if( foundType != null )
    {
      int numArrays = (fullyQualifiedName.length() - fqnNoArrays.length()) / 2;
      for( int i = 0; i < numArrays; i++ )
      {
        foundType = foundType.getArrayType();
      }
    }
    return foundType;
  }

  private IType findInCache( String fqnNoArrays )
  {
    IType foundType = _typesByName.get(fqnNoArrays);
    if( foundType == null )
    {
      foundType = _typeRefFactory.get(fqnNoArrays);
    }
    if( foundType instanceof ITypeRef && ((ITypeRef)foundType)._shouldReload() )
    {
      // The proxied type is stale, force it to reload
      clearFromCaches(  fqnNoArrays );
      foundType = null;
    }
    if (TypeSystem.isDeleted(foundType)) {
      foundType = null;
    }
    return foundType;
  }

  private IType findInCaseInsenstiveCache( String fqnNoArrays )
  {
    IType foundType = _typesByCaseInsensitiveName.get( fqnNoArrays );
    if( foundType instanceof ITypeRef && ((ITypeRef)foundType)._shouldReload() )
    {
      // The proxied type is stale, force it to reload
      foundType = null;
    }
    return foundType;
  }

  /**
   * \
   *
   * @param name The name
   *
   * @return The result of stripping all trailing occurrences of array brackets ("[]")
   *         from <code>name</code>. Examples:
   *         <pre>
   *                         entity.Coverage => entity.Coverage
   *                         entity.Coverage[] => entity.Coverage
   *                         entity.Coverage[][][] => entity.Coverage
   *                         </pre>
   */
  static String stripArrayBrackets( String name )
  {
    if( name == null )
    {
      return "";
    }
    int checkPos = name.length();
    while( checkPos > 2 && name.charAt( checkPos - 2 ) == '[' && name.charAt( checkPos - 1 ) == ']' )
    {
      checkPos -= 2;
    }
    assert checkPos <= name.length();
    
    return checkPos == name.length() ? name : name.substring( 0, checkPos );
  }

  private IType loadTypeAndCacheResult(String fullyQualifiedName, boolean skipJava)
  {
    Pair<IType, ITypeLoader> pair;
    TypeSystem.pushModule( getModule() );
    try
    {
      pair = loadType(fullyQualifiedName, skipJava);
    }
    finally
    {
      TypeSystem.popModule( getModule() );
    }

    IType type;
    if( pair != null )
    {
      type = cacheType(fullyQualifiedName, pair);
    }
    else if( !skipJava )
    {
      type = cacheType(fullyQualifiedName, new Pair<IType, ITypeLoader>(CACHE_MISS, null));
    }
    else
    {
      // Type loading was initiated through class loader, so we skipped Java type loader. Don't cache -- type could
      // be valid Java type. Eventually, we will cache it.
      type = null;
    }

    return type;
  }

  private IType loadNamespaceAndCacheResult(String fullyQualifiedName)
  {
    IType type = loadNamespaceType(fullyQualifiedName);
    if( type != null )
    {
      type = cacheNamespace(fullyQualifiedName, type);
    }
    else
    {
      type = cacheNamespace(fullyQualifiedName, CACHE_MISS);
    }
    return type;
  }

  @Override
  public void refreshed()
  {
    TypeSystem.lock();
    try
    {
      clearCaches();
      for (ITypeLoader loader : _globalStack) {
        loader.refreshed();
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  private void clearFromCaches( String fullyQualifiedTypeName ) {
    _typesByName.remove(fullyQualifiedTypeName);
    _typesByCaseInsensitiveName.remove( fullyQualifiedTypeName );
    if(fullyQualifiedTypeName.endsWith(IClassPath.PLACEHOLDER_FOR_PACKAGE)) {
      _namespaceTypesByName.remove(fullyQualifiedTypeName.substring(0, fullyQualifiedTypeName.length() - IClassPath.PLACEHOLDER_FOR_PACKAGE.length() - 1));
    }
  }

  private Pair<IType, ITypeLoader> loadType(String fullyQualifiedName, boolean skipJava)
  {
    IType type = TypeLoaderAccess.instance().getDefaultType(fullyQualifiedName);
    if (type != null) {
      return new Pair<IType, ITypeLoader>(type, type.getTypeLoader());
    }

    int dotIdx = fullyQualifiedName.indexOf('.');
    if (dotIdx > 0) {
      if (fullyQualifiedName.indexOf('.', dotIdx + 1) < 0) {
        String prefix = fullyQualifiedName.substring(0, dotIdx);
        ITypeLoader typeLoader = _loadersByPrefix.get(prefix);
        if (typeLoader != null && typeLoader.isInited()) {
          return new Pair<IType, ITypeLoader>(typeLoader.getType(fullyQualifiedName), typeLoader);
        }
      }
    }

    for( ITypeLoader loader : _globalStack )
    {
      if(loader instanceof IGosuObject) {
        if(((IGosuObject)loader).getIntrinsicType().getName().equals(fullyQualifiedName) ||
            GosuClassCompilingStack.getCompilingType(((IGosuObject)loader).getIntrinsicType().getName()) != null) {
          continue;
        }
      }
      if( loader.handlesNonPrefixLoads() && loader.isInited() )
      {
        // Only look through loaders that can do non-prefix loading
        if (loader instanceof IDefaultTypeLoader && skipJava) {
          // Forbid going back into classloaders world (for example, we are loading Gosu type through URL handler)
          continue;
        }
        type = loader.getType( fullyQualifiedName );
        if( type != null )
        {
          return new Pair<IType, ITypeLoader>(type, loader);
        }
      }
    }

    return null;
  }

  private IType loadNamespaceType(String namespace)
  {
    for (int i = 0; i < _globalStack.size(); i++) {
      ITypeLoader loader = _globalStack.get( i );
      if(loader instanceof IGosuObject) {
        if(GosuClassCompilingStack.getCompilingType(((IGosuObject)loader).getIntrinsicType().getName()) != null) {
          continue;
        }
      }
      //noinspection SuspiciousMethodCalls
      if ( loader.hasNamespace( namespace ) || isProxyType( namespace, loader ) ) {
        return new NamespaceType( namespace, getModule() );
      }
    }
    return null;
  }

  private boolean isProxyType( String fullyQualifiedName, ITypeLoader loader )
  {
    if( (loader == _defaultTypeLoader ||
         loader.getClass() == GosuClassTypeLoader.class) &&
        IGosuClass.ProxyUtil.isProxyClass( fullyQualifiedName ) )
    {
      String minusProxy = fullyQualifiedName.substring( IGosuClass.PROXY_PREFIX.length()+1 );
      if( loader.hasNamespace(minusProxy) )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<ITypeLoader> getTypeLoaderStack()
  {
    return _globalStack;
  }

  /**
   * Adds the type to the cache.
   */
  private IType cacheType(String name, Pair<IType, ITypeLoader> pair)
  {
    if( pair != null )
    {
      IType type = pair.getFirst();
      // We have to make sure we aren't replacing an existing type so we obey the return from the put.
      IType oldType = _typesByName.get(name);
      if( oldType != null && oldType != CACHE_MISS )
      {
        return oldType;
      }
      _typesByName.add( name, type );
      ITypeLoader typeLoader = pair.getSecond();
      if( typeLoader != null && !typeLoader.isCaseSensitive() )
      {
        _typesByCaseInsensitiveName.put( name, type);
      }
    }
    return pair != null ? pair.getFirst() : null;
  }

  private IType cacheNamespace(String name, IType type)
  {
    if( type != null )
    {
      // We have to make sure we aren't replacing an existing type so we obey the return from the put.
      IType oldType = _namespaceTypesByName.put( name, type );
      if( oldType != null && oldType != CACHE_MISS )
      {
        _namespaceTypesByName.put( name, oldType );
        return oldType;
      }
    }
    return type;
  }

  @Override
  public ITypeRefFactory getTypeRefFactory()
  {
    return _typeRefFactory;
  }

  public void uninitializeTypeLoaders() {
    for (ITypeLoader typeLoader : _globalStack) {
      if (typeLoader instanceof IUninitializableTypeLoader) {
        ((IUninitializableTypeLoader)typeLoader).uninitialize();
      }
    }
  }

  public DefaultTypeLoader getDefaultTypeLoader() {
    return _defaultTypeLoader;
  }
  
  public String toString() {
    return _module.toString();
  }

  public void shutdown() {
    for (ITypeLoader typeLoader : _globalStack) {
      typeLoader.shutdown();
    }
  }

  public boolean refresh(IResource file, String typeName, RefreshKind refreshKind) {
    TypeSystem.pushModule(getModule());
    TypeSystem.lock();
    try {
      if (file instanceof IFile) {
        return refreshFile((IFile) file, typeName, refreshKind);
      } else if (file instanceof IDirectory) {
        return refreshDirectory((IDirectory) file, refreshKind);
      } else {
        throw new RuntimeException("Unknown resource: " + file);
      }
    } finally {
      TypeSystem.unlock();
      TypeSystem.popModule(getModule());
    }
  }

  private boolean refreshDirectory(IDirectory directory, RefreshKind kind) {
    boolean processed = false;

    // refresh the directory itself
    for (ITypeLoader typeLoader : _globalStack) {
      if (typeLoader.handlesDirectory(directory)) {
        String namespace = typeLoader.getNamespaceForDirectory(directory);
        if (namespace != null) {
          refreshNamespaceCaches(namespace, typeLoader, kind);
          typeLoader.refreshedNamespace(namespace, directory, kind);
        }
        processed = true;
      }
    }

    // refresh directory content
    for (IFile file : directory.listFiles()) {
      processed |= refreshFile(file, null, kind);
    }
    for (IDirectory dir : directory.listDirs()) {
      processed |= refreshDirectory(dir, kind);
    }

    return processed;
  }

  private void refreshNamespaceCaches( String namespace, ITypeLoader typeLoader, RefreshKind kind ) {
    if (kind == RefreshKind.CREATION) {
      _namespaceTypesByName.remove(namespace);
      clearErrorTypes();
    } else if (kind == RefreshKind.DELETION) {
      if( typeLoader.getClass() == GosuClassTypeLoader.class ) {
        IGosuClassRepository repository = ((GosuClassTypeLoader)typeLoader).getRepository();
        if( repository.hasNamespace( namespace ) == 1 )
        _namespaceTypesByName.put( namespace, CACHE_MISS );
      }
    }
  }

  private boolean refreshFile(IFile file, String typeName, RefreshKind kind) {
    boolean processed = false;

    for (ITypeLoader typeLoader : _globalStack) {
      if (typeLoader.handlesFile(file)) {
        String[] types;
        if (typeName != null) {
          types = new String[] {typeName};
        } else {
          types = typeLoader.getTypesForFile(file);
        }
        kind = typeLoader.refreshedFile(file, types, kind);
        if (types != null && types.length != 0) {
          RefreshRequest refreshRequest = new RefreshRequest(file, types, typeLoader, kind);
          TypeLoaderAccess.instance().refreshTypes(refreshRequest);
        }
        processed = true;
      }
    }
    return processed;
  }

  public IType getCachedType(String fqn) {
    IType foundType = findInCache( fqn );
    if (foundType == null) {
      foundType = findInCaseInsenstiveCache(fqn);
    }
    return foundType;
  }
}
