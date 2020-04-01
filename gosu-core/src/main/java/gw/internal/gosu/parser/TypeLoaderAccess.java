/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.BaseService;
import gw.config.CommonServices;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.internal.gosu.compiler.SingleServingGosuClassLoader;
import gw.internal.gosu.module.Module;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.ICompoundType;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.ITypeRefFactory;
import gw.lang.reflect.ITypeSystem;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.NoReferenceFoundException;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.ITypeLoaderStack;
import gw.util.GosuExceptionUtil;
import gw.util.IdentitySet;
import gw.util.concurrent.LockingLazyVar;
import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
public class TypeLoaderAccess extends BaseService implements ITypeSystem
{
  private Map<IType, IType> _boxToPrimitiveMap;
  private Map<IType, IType> _primitiveToBoxMap;
  public static LockingLazyVar<Map<String, IJavaType>> PRIMITIVE_TYPES_BY_NAME = new LockingLazyVar<Map<String, IJavaType>>() {
    protected Map<String, IJavaType> init() {
      HashMap<String, IJavaType> returnMap = new HashMap<>( 9 );
      returnMap.put("byte", JavaTypes.pBYTE());
      returnMap.put("char", JavaTypes.pCHAR());
      returnMap.put("double", JavaTypes.pDOUBLE());
      returnMap.put("float", JavaTypes.pFLOAT());
      returnMap.put("int", JavaTypes.pINT());
      returnMap.put("long", JavaTypes.pLONG());
      returnMap.put("short", JavaTypes.pSHORT());
      returnMap.put("boolean", JavaTypes.pBOOLEAN());
      returnMap.put("void", JavaTypes.pVOID());
      return returnMap;
    }
  };
  private static final Map<CharSequence, TypeGetter> EMPTY_DEFAULT_TYPES = new HashMap<>();

  // This integer is incremented every time the typesystem is flushed so that a type can know if it
  // is curent or not
  private int _iRefreshChecksum = 0;
  private int _iSingleRefreshChecksum = 0;

  //------------------------------------------------------------
  // Type system caches
  //------------------------------------------------------------
  private Map<CharSequence, TypeGetter> _defaultTypes;
  private boolean _defaultTypesIniting = false;

  //A weak cache of listeners who will be notified when typesystem events occur
  private final CopyOnWriteArrayList<WeakReference<ITypeLoaderListener>> _listeners;
  private final List<TypeSystemShutdownListener> _shutdownListeners = new ArrayList<>();
  private ExecutionEnvironment _env;
  private Module _module;
  private ModuleTypeLoader _moduleTypeLoader;

  public static TypeLoaderAccess instance()
  {
    return (TypeLoaderAccess) CommonServices.getTypeSystem();
  }

  public Module getModule()
  {
    return _module == null ? _module = (Module)getExecutionEnvironment().getModule() : _module;
  }

  public ModuleTypeLoader getModuleTypeLoader()
  {
    return _moduleTypeLoader == null ? _moduleTypeLoader = getModule().getModuleTypeLoader() : _moduleTypeLoader;
  }
  
  public TypeLoaderAccess()
  {
    _listeners = new CopyOnWriteArrayList<>();
  }

  public IType getDefaultType(String name) {
    TypeGetter getter = getDefaultTypes().get(name);
    return getter != null ? getter.get() : null;
  }

  public boolean isParameterizedWith( IType type, ITypeVariableType... typeVars )
  {
    if( typeVars == null )
    {
      return false;
    }
    for( ITypeVariableType tv: typeVars )
    {
      if( type.equals( tv ) || TypeLord.isParameterizedWith( type, (TypeVariableType)tv) )
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public IType getCompoundType( Set<IType> types )
  {
    return CompoundType.get( types );
  }

  Map<CharSequence, TypeGetter> getDefaultTypes()
  {
    if (_defaultTypesIniting) {
      return EMPTY_DEFAULT_TYPES;
    }

    if (_defaultTypes == null) {
      _defaultTypesIniting = true;

      Map<CharSequence, TypeGetter> defaultTypes = new HashMap<>();
      getDefaultTypes( defaultTypes );
      _defaultTypes = defaultTypes;

      _defaultTypesIniting = false;
    }
    return _defaultTypes;
  }

  public List<ITypeLoader> getAllTypeLoaders() {
    return new ArrayList<>( getModuleTypeLoader().getTypeLoaderStack() );
  }

  private List<ITypeLoaderListener> getListeners() {
    List<ITypeLoaderListener> listeners = new ArrayList<>( _listeners.size() );
    List<WeakReference<ITypeLoaderListener>> obsoleteListeners = null;
    for (WeakReference<ITypeLoaderListener> ref : _listeners) {
      ITypeLoaderListener typeLoaderListener = ref.get();
      if (typeLoaderListener != null) {
        listeners.add(typeLoaderListener);
      }
      else {
        if (obsoleteListeners == null) {
          obsoleteListeners = new ArrayList<>();
        }
        obsoleteListeners.add(ref);
      }
    }
    if (obsoleteListeners != null) {
      _listeners.removeAll( obsoleteListeners );
    }

    return listeners;
  }

  public ITypeRef getTypeReference(final IType type)
  {
    ITypeRef ref = getModuleTypeLoader().getTypeRefFactory().get(type);
    if( ref == null )
    {
      throw new NoReferenceFoundException( type );
    }
    return ref;
  }

  public ITypeRef getOrCreateTypeReference(final IType type)
  {
    return getModuleTypeLoader().getTypeRefFactory().create(type);
  }

  public <T extends ITypeLoader> T getTypeLoader( final Class<? extends T> loaderType )
  {
    return getModuleTypeLoader().getTypeLoader(loaderType);
  }

  public INamespaceType getNamespace( String fqn )
  {
    for( int i = 0; i < fqn.length(); ++i )
    {
      char c = fqn.charAt( i );
      if( !(Character.isJavaIdentifierPart( c ) || c == '.' || c == '*') )
      {
        return null;
      }
    }
    return getModuleTypeLoader().getNamespaceType( fqn );
  }

  public IType getIntrinsicTypeFromObject( final Object object )
  {
    return getModuleTypeLoader().getIntrinsicTypeFromObject( object );
  }

  public IType getIntrinsicTypeByFullName( String fullyQualifiedName ) throws ClassNotFoundException
  {
    IType type = FrequentUsedJavaTypeCache.instance().getHighUsageType(fullyQualifiedName);
    if (type != null) {
      return type;
    }

    type = getByFullNameIfValid(fullyQualifiedName);
    if( type == null )
    {
      throw new ClassNotFoundException( fullyQualifiedName + " in module " + getModule());
    }
    else
    {
      return type;
    }
  }

  /**
   * Gets an intrinsic type based on a relative name.  This could either be the name of an entity,
   * like "User", the name of a typekey, like "SystemPermission", or a class name, like
   * "java.lang.String" (relative and fully qualified class names are the same as far as this factory
   * is concerned).  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param relativeName the relative name of the type
   * @param typeUses     the map of used types to use when resolving
   * @return the corresponding IType
   * @throws ClassNotFoundException if the specified name doesn't correspond to any type
   */
  public IType getByRelativeName(String relativeName, ITypeUsesMap typeUses) throws ClassNotFoundException {
    String relativeName1 = relativeName;
    getExecutionEnv();
    IType type = FrequentUsedJavaTypeCache.instance().getHighUsageType(relativeName1);
    if (type != null) {
      return type;
    }
    //## todo: consider handling requests to find a parameterized type... the following is a giant hack
    int i = relativeName1 == null ? -1 : relativeName1.indexOf( '<' );
    if( i >= 0 )
    {
      relativeName1 = relativeName1.substring( 0, i );
    }
    //##
    type = getTypeByRelativeNameIfValid_NoGenerics(relativeName1, typeUses);
    if( type == null )
    {
      throw new ClassNotFoundException(relativeName1);
    }
    else
    {
      return type;
    }
  }

  public IType getTypeByRelativeNameIfValid_NoGenerics(String relativeName, ITypeUsesMap typeUses)
  {
    IType type;
    if( typeUses != null )
    {
      // Then if we have a type uses map and we are resolving let it try
      type = typeUses.resolveType(relativeName);
    }
    else
    {
      // ask for it by full name
      type = getByFullNameIfValid(relativeName);
    }

    if( type == null )
    {
      type = getDefaultType(relativeName);
    }
    return type;
  }

  public Set<? extends CharSequence> getAllTypeNames() {
    TypeSystem.lock();
    try {
      Set<CharSequence> names = new HashSet<>( getDefaultTypes().keySet() );

      // Walk the list backward so we resolve type dependencies in the correct order
      List<ITypeLoader> loaders = getAllTypeLoaders();
      for (int i = loaders.size() - 1; i >= 0; i--) {
        names.addAll(loaders.get(i).getAllTypeNames());
      }
      return names;
    } finally {
      TypeSystem.unlock();
    }
  }

  public void clearErrorTypes()
  {
    getModuleTypeLoader().clearErrorTypes();
  }

  public void refresh( final boolean clearCachedTypes )
  {
    // dlank: should an exception be thrown if the server is not in dev mode?
    TypeSystem.lock();
    try
    {
      dumpMaps();

      ++_iRefreshChecksum;
      ++_iSingleRefreshChecksum;

      getModuleTypeLoader().refreshed();

      CommonServices.getPlatformHelper().refresh();

      fireRefreshed();

      if( clearCachedTypes )
      {
        getModuleTypeLoader().getTypeRefFactory().clearCaches();
        MetaType.clearCaches();
      }

      SingleServingGosuClassLoader.clearCache();

      _defaultTypes = null;
      
      _env = null;
      _module = null;
      _moduleTypeLoader = null;
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void shutdown()
  {
    TypeSystem.lock();
    try
    {
      getModuleTypeLoader().shutdown();

      for (TypeSystemShutdownListener shutdownListener : _shutdownListeners) {
        shutdownListener.shutdown();
      }

      ExecutionEnvironment executionEnv = getExecutionEnv();
      TypeSystem.refresh(false);
      executionEnv.shutdown();
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void refreshTypes(final RefreshRequest request) {
    TypeRefFactory typeRefFactory = (TypeRefFactory) getModuleTypeLoader().getTypeRefFactory();
    TypeSystem.lock();
    try {
      ++_iSingleRefreshChecksum;

      // Step 1: Find all top-level types that need to be refreshed
      Set<IType> typesToRefresh = new HashSet<>( 10 );
      Set<String> doNotDelete = new HashSet<>( 10 );
      for (String typeName : request.types) {
        IType type = typeRefFactory.get(typeName);

        if (type != null) {
          typesToRefresh.add(type);

          if (!((AbstractTypeRef) type).isStale()) {
            ITypeRef topLevelType = getTopLevelType(type);
            if (topLevelType != null) {
              typesToRefresh.add(topLevelType);
            }
            if (type instanceof IGosuEnhancement) {
              // add the current enhanced type
              IGosuEnhancement enhancement = (IGosuEnhancement) type;
              IType enhancedType = enhancement.getEnhancedType();
              if( enhancedType != null && !(enhancedType instanceof INonLoadableType) && !TypeSystem.isDeleted( enhancedType ) ) {
                AbstractTypeRef enhancedTopLevelType = (AbstractTypeRef)getTopLevelType( enhancedType );
                if( enhancedTopLevelType != null ) {
                  typesToRefresh.add( enhancedTopLevelType );
                  doNotDelete.add( enhancedTopLevelType._getTypeName() );
                }
              }
            }
            // add the old enhanced type
            ITypeLoaderStack moduleTypeLoader = getModuleTypeLoader();
            GosuClassTypeLoader gosuClassTypeLoader = moduleTypeLoader.getTypeLoader(GosuClassTypeLoader.class);
            String orphanedEnhancementName = gosuClassTypeLoader.getEnhancementIndex().getOrphanedEnhancement(type.getName());
            if (orphanedEnhancementName != null) {
              IType orphanedEnhancement = typeRefFactory.get(orphanedEnhancementName);
              if (orphanedEnhancement instanceof AbstractTypeRef && !((AbstractTypeRef) orphanedEnhancement).isStale()) {
                typesToRefresh.add(orphanedEnhancement);
                doNotDelete.add( ((AbstractTypeRef)orphanedEnhancement)._getTypeName() );
              }
            }
          }
        }
      }

      // Step 2: Find all subordinate types that need to be refreshed
      IdentitySet<ITypeRef> typesToMakeStaleSet = new IdentitySet<>( typesToRefresh.size() * 2 );
      for (IType type : typesToRefresh) {
        typesToMakeStaleSet.add((ITypeRef) type);
        addGosuProxyClass(typesToMakeStaleSet, type);
        List<ITypeRef> subordinateRefs = typeRefFactory.getSubordinateRefs(type.getName());
        for (ITypeRef typeRef : subordinateRefs) {
          typesToMakeStaleSet.add(typeRef);
          addGosuProxyClass(typesToMakeStaleSet, typeRef);
        }
      }

      // Step 3: Some stuff
      for( IType typeToRefresh: typesToMakeStaleSet )
      {
        if( typeToRefresh instanceof IGosuClassInternal && !((AbstractTypeRef)typeToRefresh).isStale() )
        {
          IGosuClassInternal gsClass = (IGosuClassInternal)typeToRefresh;
          ((IGosuClassInternal)gsClass.dontEverCallThis()).unloadBackingClass();
        }
      }

      // Step 4: Sort all the types
      ITypeRef[] typesToMakeStaleArray = typesToMakeStaleSet.toArray(new ITypeRef[typesToMakeStaleSet.size()]);
      Arrays.sort(typesToMakeStaleArray,
          new Comparator<IType>() {
            public int compare(IType t1, IType t2) {
              return computeSortIndex((AbstractTypeRef) t2) - computeSortIndex((AbstractTypeRef) t1);
            }

            private int computeSortIndex(AbstractTypeRef ref) {
              return ref != null ? ref._getIndexForSortingFast(ref._getTypeName()) : 10000;
            }
          }
      );

      Map<ITypeLoader, Set<String>> typeLoaderToTypeMap = new HashMap<>();
      typeLoaderToTypeMap.put(request.typeLoader, new HashSet<>( Arrays.asList( request.types ) ));

      for (ITypeRef type : typesToMakeStaleSet) {
        ITypeLoader typeLoader = type.getTypeLoaderDirectly();
        Set<String> typeRefs = typeLoaderToTypeMap.get(typeLoader);
        if (typeRefs == null) {
          typeRefs = new HashSet<>( typesToMakeStaleSet.size() );
          typeLoaderToTypeMap.put(typeLoader, typeRefs);
        }
        typeRefs.add(((AbstractTypeRef)type)._getTypeName());
      }

      // Step 5: Make the references stale
      for (IType type : typesToMakeStaleArray) {
        if (type != null) {
          RefreshKind kind = request.kind;
          if( kind == RefreshKind.DELETION ) {
            if( doNotDelete.stream().anyMatch( e -> ((AbstractTypeRef)type)._getTypeName().startsWith( e ) ) ) {
              // only delete what was deleted e.g., if an enhancement is deleted, don't delete the enhanced type, just refresh it
              kind = RefreshKind.MODIFICATION;
            }
          }
          ((ITypeRef)type)._setStale( kind );
        }
      }

      // Step 6: Clear all caches
      getModuleTypeLoader().clearFromCaches(request);

      // Step 7: notify the typeloader
      for (ITypeLoader typeLoader : typeLoaderToTypeMap.keySet()) {
        final Set<String> strings = typeLoaderToTypeMap.get(typeLoader);
        RefreshRequest theRequest = new RefreshRequest(strings.toArray( new String[0] ), request, typeLoader);
        typeLoader.refreshedTypes(theRequest);
      }

      // Step 8: Call all listeners
      for (ITypeLoaderListener listener : getListeners()) {
        listener.refreshedTypes(request);
      }
    } finally {
      TypeSystem.unlock();
    }
  }

  private void addGosuProxyClass( IdentitySet<ITypeRef> allTypes, IType type ) {
    if( type instanceof IJavaTypeInternal && !((AbstractTypeRef)type).isStale() && !((AbstractTypeRef)type).isDeleted()) {
      IGosuClassInternal gsProxyClass = ((IJavaTypeInternal)type).getAdapterClassDirectly();
      if( gsProxyClass != null ) {
        allTypes.add( (ITypeRef)gsProxyClass );
      }
    }
  }

  private ITypeRef getTopLevelType(IType type) {
    IType topLevelType = TypeLord.getTopLevelType(type);
    if (topLevelType instanceof ITypeRef) {
      return (ITypeRef) topLevelType;
    } else {
      return null;
    }
  }

  public int getRefreshChecksum()
  {
    return _iRefreshChecksum;
  }

  public int getSingleRefreshChecksum()
  {
    return _iSingleRefreshChecksum;
  }

  /**
   * Maintains weak refs to listeners. This is primarily so that tests don't
   * accumulate a bunch of listeners over time. Otherwise this is a potential
   * memory gobbler in tests.
   * <p>
   * Note! Callers must manage the lifecycle of the listener, otherwise since this
   * method creates a weak ref, it will be collected when it goes out of scope.
   *
   * @param l Your type loader listener
   */
  public void addTypeLoaderListenerAsWeakRef( ITypeLoaderListener l )
  {
    if(!hasListener(l)) {
      _listeners.add( new WeakReference<>( l ));
    }
  }

  public void removeTypeLoaderListener( ITypeLoaderListener l )
    {
    for(WeakReference<ITypeLoaderListener> ref : _listeners) {
      if(ref.get() == l) {
        _listeners.remove(ref);
        break;
    }
    }
  }

  private boolean hasListener( ITypeLoaderListener l ) {
    for(WeakReference<ITypeLoaderListener> ref : _listeners) {
      if(ref.get() == l) {
        return true;
    }
    }
    return false;
  }

  private void fireRefreshed()
  {
    TypeSystem.lock();
    try
    {
      getModuleTypeLoader().refreshed();
      for( ITypeLoaderListener listener : getListeners())
      {
        listener.refreshed();
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public void incrementChecksums() {
    _iRefreshChecksum++;
    _iSingleRefreshChecksum++;
  }

  private static ExecutionEnvironment getExecutionEnv()
  {
    return ExecutionEnvironment.instance();
  }

  public void addShutdownListener(TypeSystemShutdownListener listener) {
    _shutdownListeners.add(listener);
  }

  interface TypeGetter {
    IType get();
  }

  private static void getDefaultTypes( Map<CharSequence, TypeGetter> typeMap )
  {
    // built-in types
    typeMap.put( "Type", () -> MetaType.ROOT_TYPE_TYPE.get() );
  }

  /**
   * Gets the intrinsic type for a given class.<p>
   * <p/>
   * <b>Note:</b> you should use this method only if you do not have an
   * Object of class <code>javaClass</code> to get the type from. If you
   * do have such an object, use {@link #getFromObject} instead.
   *
   * @param javaClass the Class to convert to an intrinsic type
   * @return the IType that corresponds to that class
   * @see #getFromObject(Object)
   */
  public IType get(Class<?> javaClass) {
    assert javaClass != null;
    IType type = FrequentUsedJavaTypeCache.instance().getHighUsageType(javaClass);
    if (type != null) {
      return type;
    }

    String fqn = computeFullyQualifiedName(javaClass);

    if (IType.class.isAssignableFrom(javaClass) && fqn.endsWith(ITypeRefFactory.SYSTEM_PROXY_SUFFIX)) {
      type = JavaType.get(javaClass);
      if (type != null) {
        return type;
      }
    }

    type = getByFullNameIfValid(fqn);
    if (type != null) {
      return type;
    }

    // NOTE pdalbora 11-Apr-2011 -- If it's a proxy class, then it won't be found by name. Instead, just create
    // a JavaType for it. This won't happen in the editor (Eclipse), because a proxy class only exists at runtime;
    // therefore, using the DefaultTypeLoader here should be fine.
    if (Proxy.isProxyClass(javaClass)) {
      return JavaType.get(javaClass);
    }

    return null;
  }

  private String computeFullyQualifiedName(Class<?> javaClass) {
    if (javaClass.isArray()) {
      return computeFullyQualifiedName(javaClass.getComponentType()) + "[]";
    } else {
      return javaClass.getName().replace('$', '.');
    }
  }

  public IType get(IJavaClassInfo javaClassInfo) {
    if (javaClassInfo == null) {
      throw new NullPointerException("Cannot have a null class info.");
    }
    if (javaClassInfo instanceof ClassJavaClassInfo) {
        return get(((ClassJavaClassInfo) javaClassInfo).getJavaClass());
    }

    String name = javaClassInfo.getName().replace('$', '.');
    IType type = FrequentUsedJavaTypeCache.instance().getHighUsageType(name);
    if (type == null) {
      type = getByFullNameIfValid(name);
    }
    return type;
  }

  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   * @return the IType for the object
   * @see #get(Class)
   */
  public IType getFromObject(Object object) {
    return getIntrinsicTypeFromObject(object);
  }

  public IType getByRelativeName(String relativeName) throws ClassNotFoundException {
    return getByRelativeName(relativeName, CommonServices.getEntityAccess().getDefaultTypeUses());
  }

  /**
   * Gets an intrinsic type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   * @return the corresponding IType
   * @throws RuntimeException if the specified name doesn't correspond to any type
   */
  public IType getByFullName(String fullyQualifiedName) {
    try {
      return getIntrinsicTypeByFullName(fullyQualifiedName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeExceptionWithNoStacktrace(e);
    }
  }

  /**
   * Gets a type based on a fully-qualified name.  This could either be the name of an entity,
   * like "entity.User", the name of a typekey, like "typekey.SystemPermission", or a class name, like
   * "java.lang.String".  Names can have [] appended to them to create arrays, and multi-dimensional arrays
   * are supported.
   * <p/>
   * This method behaves the same as getByFullName except instead of throwing it returns null.
   *
   * @param fullyQualifiedName the fully qualified name of the type
   * @return the corresponding IType or null if the type does not exist
   */
  public IType getByFullNameIfValid(String fullyQualifiedName) {
    return getByFullNameIfValid(fullyQualifiedName, false);
  }

  public IType getByFullNameIfValidNoJava(String fullyQualifiedName) {
    return getByFullNameIfValid(fullyQualifiedName, true);
  }

  private IType getByFullNameIfValid(String fullyQualifiedName, boolean skipJava) {
    if (!isValidTypeName(fullyQualifiedName)) {
      return null;
    }

    try {
      IType type = ((Module) TypeSystem.getModule()).getModuleTypeLoader()
        .getTypeByFullNameIfValid(fullyQualifiedName, skipJava);
      if (type != null) {
        return type;
      }
    } catch (Exception e) {
      throw GosuExceptionUtil.forceThrow(e);
    }

    return null;
  }

  private boolean isValidTypeName( String fqn) {
    // empty names are invalid
    if( fqn == null || fqn.length() == 0 ) {
      return false;
    }

    if( fqn.contains( "block(" ) ||
        fqn.contains( "block (" ) )
    {
      return false;
    }

    int braceIndex = fqn.indexOf('[');
    if( braceIndex > 0 ) {
      if( fqn.length() < braceIndex + 2 ||
          fqn.charAt( braceIndex + 1 ) != ']' ) {
        return false;
      }
      fqn = fqn.substring( 0, braceIndex );
    }

    // the last char should be an identifier char
    return Character.isJavaIdentifierPart( fqn.charAt( fqn.length() - 1 ) );
  }

  @Override
  public IType boundTypes(IType targetType, List<IType> typesToBound) {
    return TypeLord.boundTypes(targetType, typesToBound);
  }

  public void refresh(ITypeRef typeRef) {
    refreshTypes(new RefreshRequest(null, new String[]{typeRef.getName()}, typeRef.getTypeLoader(), RefreshKind.MODIFICATION));
  }

  private void dumpMaps() {
    _boxToPrimitiveMap = null;
    _primitiveToBoxMap = null;
    PRIMITIVE_TYPES_BY_NAME.clear();
  }

  @Override
  public void refreshed(IResource file, String typeName, RefreshKind refreshKind) {
    ((ITypeLoaderStackInternal) getModuleTypeLoader()).refresh(file, typeName, refreshKind);
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    for (ITypeLoader loader : getModuleTypeLoader().getTypeLoaderStack()) {
      final List<String> typeNames = new ArrayList<>();
      if (loader.handlesFile(file)) {
        typeNames.addAll(Arrays.asList(loader.getTypesForFile(file)));
      }
      if (!typeNames.isEmpty()) {
        return typeNames.toArray( new String[0] );
      }
    }

    // Default
    return new String[] {"default." + file.getBaseName()};
  }

  /**
    * Converts a String name of a type into an IType.
    *
    * @throws IllegalArgumentException if the type string doesn't correspond to any known IType
    */
   public IType parseType(String typeString) throws IllegalArgumentException {
     return parseType(typeString, (ITypeUsesMap)null);
   }

  public IType parseType(String typeString, ITypeUsesMap typeUsesMap) throws IllegalArgumentException {
    IType type = getTypeByRelativeNameIfValid_NoGenerics(typeString, typeUsesMap);
    if (type == null) {
      throw new IllegalArgumentException("Could not parse type " + typeString);
    } else {
      return type;
    }
  }

  public IType parseType(String typeString, TypeVarToTypeMap actualParamByVarName) {
    return TypeLord.parseType(typeString, actualParamByVarName);
  }
  public IType parseType(String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap) {
    return TypeLord.parseType(typeString, actualParamByVarName, typeUsesMap);
  }

  public ITypeLiteralExpression parseTypeExpression(String typeString, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap) throws ParseResultsException {
    return TypeLord.parseTypeLiteral(typeString, actualParamByVarName, typeUsesMap);
  }

  public IType getComponentType(IType valueType) {
    IType componentType;
    if (valueType.isArray()) {
      componentType = valueType.getComponentType();
    } else if (valueType instanceof IPlaceholder && ((IPlaceholder) valueType).isPlaceholder()) {
      componentType = valueType.getComponentType();
    } else {
      IType qrsType = JavaTypes.IQUERY_RESULT_SET();
      if (qrsType.isAssignableFrom(valueType)) {
        for (IType iType : valueType.getAllTypesInHierarchy()) {
          if (iType.isParameterizedType() && iType.getGenericType().equals(qrsType)) {
            return iType.getTypeParameters()[0];
          }
        }
        componentType = ErrorType.getInstance();
      } else {
        componentType = ErrorType.getInstance();
      }
    }
    return componentType;
  }

  public ITypeVariableType getOrCreateTypeVariableType(String strName, IType boundingType, IType enclosingType) {
    IGenericTypeVariable typeVar = new GenericTypeVariable(strName, boundingType);
    typeVar.createTypeVariableDefinition(enclosingType);
    return new TypeVariableType(enclosingType, typeVar);
  }

  public IFunctionType getOrCreateFunctionType(IMethodInfo mi) {
    return new FunctionType(mi);
  }

  public IFunctionType getOrCreateFunctionType(String strFunctionName, IType retType, IType[] paramTypes) {
    return new FunctionType(strFunctionName, retType, paramTypes);
  }

  public TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType ) {
    return TypeLord.mapTypeByVarName( ownersType, declaringType );
  }

  public IType getActualType(IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars) {
    return TypeLord.getActualType(type, actualParamByVarName, bKeepTypeVars);
  }

  public void inferTypeVariableTypesFromGenParamTypeAndConcreteType(IType genParamType, IType argType, TypeVarToTypeMap map) {
    TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType(genParamType, argType, map);
  }
  public void inferTypeVariableTypesFromGenParamTypeAndConcreteType_Reverse(IType genParamType, IType argType, TypeVarToTypeMap map) {
    TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType_Reverse(genParamType, argType, map);
  }

  public IErrorType getErrorType() {
    return ErrorType.getInstance();
  }

  public IErrorType getErrorType(String strErrantName) {
    return ErrorType.getInstance(strErrantName);
  }

  public IErrorType getErrorType(ParseResultsException pe) {
    return new ErrorType(pe);
  }

  public IDefaultTypeLoader getDefaultTypeLoader() {
    return DefaultTypeLoader.instance();
  }

  public IType findParameterizedType(IType type, IType rhsType) {
    return TypeLord.findParameterizedType(type, rhsType);
  }

  public Set<String> getNamespacesFromTypeNames(Set<? extends CharSequence> allTypeNames, Set<String> namespaces) {
    return TypeLord.getNamespacesFromTypeNames(allTypeNames, namespaces);
  }

  public void pushTypeLoader(ITypeLoader loader) {
    getModuleTypeLoader().pushTypeLoader( loader );
  }

  public void pushIncludeAll() {
    GosuClassTypeInfo.pushIncludeAll();
  }

  public void popIncludeAll() {
    GosuClassTypeInfo.popIncludeAll();
  }

  public boolean isIncludeAll() {
    return GosuClassTypeInfo.isIncludeAll();
  }

  public IType getCurrentCompilingType() {
    return GosuClassCompilingStack.getCurrentCompilingType();
  }

  public void pushCompilingType(IType type) {
    GosuClassCompilingStack.pushCompilingType(type);
  }

  public void popCompilingType() {
    GosuClassCompilingStack.popCompilingType();
  }

  public IType getCompilingType(String strName) {
    return GosuClassCompilingStack.getCompilingType(strName);
  }

  public void pushSymTableCtx(ISymbolTable ctx) {
    CompiledGosuClassSymbolTable.pushSymTableCtx(ctx);
  }

  public void popSymTableCtx() {
    CompiledGosuClassSymbolTable.popSymTableCtx();
  }

  public ISymbolTable getSymTableCtx() {
    return CompiledGosuClassSymbolTable.getSymTableCtx();
  }

  public IType getTypeFromObject(Object obj) {
    return getIntrinsicTypeFromObject(obj);
  }

  public boolean isExpandable(IType type) {
    return TypeLord.isExpandable(type);
  }

  public String getNameOfParams(IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType) {
    return TypeLord.getNameOfParams(paramTypes, bRelative, bWithEnclosingType);
  }

  public ISymbolTable getCompiledGosuClassSymbolTable() {
    return CompiledGosuClassSymbolTable.instance();
  }

  public IType getJavaType(Class javaClass) {
    return TypeSystem.get(javaClass);
  }

  public String getNameWithQualifiedTypeVariables(IType type) {
    return TypeLord.getNameWithQualifiedTypeVariables(type, false);
  }

  public IType getDefaultParameterizedType(IType type) {
    return TypeLord.getDefaultParameterizedType(type);
  }

  public IType getDefaultParameterizedTypeWithTypeVars(IType type) {
    return TypeLord.getDefaultParameterizedTypeWithTypeVars(type);
  }

  public boolean canCast( IType lhsType, IType rhsType ) {
    if( lhsType instanceof ICompoundType ) {
      return canCastCompountType( (ICompoundType)lhsType, rhsType );
    }
    while( lhsType instanceof TypeVariableType && !TypeLord.isRecursiveType( (TypeVariableType)lhsType, ((TypeVariableType)lhsType).getBoundingType() ) ) {
      lhsType = ((TypeVariableType)lhsType).getBoundingType();
    }
    while( rhsType instanceof TypeVariableType && !TypeLord.isRecursiveType( (TypeVariableType)rhsType, ((TypeVariableType)rhsType).getBoundingType() ) ) {
      rhsType = ((TypeVariableType)rhsType).getBoundingType();
    }

    if( lhsType != null ) {
      // Support explicit downcast

      if( lhsType instanceof IMetaType && rhsType instanceof IMetaType ) {
        // unwrap metatypes
        return canCast( ((IMetaType)lhsType).getType(), ((IMetaType)rhsType).getType() );
      }

      if( rhsType.isAssignableFrom( lhsType ) ) {
        return true;
      }
      else if( lhsType.isAssignableFrom( rhsType ) ) {
        return true;
      }
      else if( rhsType.isInterface() && lhsType.isInterface() && !genericInterfacesClash( rhsType, lhsType ) ) {
        return true;
      }
      else if( rhsType.isInterface() && ((!lhsType.isFinal() && !lhsType.isPrimitive() && !(lhsType instanceof IFunctionType) && !(lhsType.isArray()) && !genericInterfacesClash( rhsType, lhsType)) || canCastMetaType( lhsType, rhsType )) ) {
        // Support cross-casting to an interface
        return true;
      }
      else if( lhsType.isInterface() && ((!rhsType.isFinal() && !rhsType.isPrimitive() && !(rhsType instanceof IFunctionType) && !(rhsType.isArray()) && !genericInterfacesClash( lhsType, rhsType))) ) {
        return true;
      }
      else if( lhsType == JavaTypes.OBJECT() && rhsType.isPrimitive() && rhsType != JavaTypes.pVOID() ) {
        return true;
      }
      else if( lhsType.isParameterizedType() && rhsType.isParameterizedType() ) {
        boolean bRawAssignable = rhsType.getGenericType().isAssignableFrom( lhsType.getGenericType() );
        if( bRawAssignable || canCast( lhsType.getGenericType(), rhsType.getGenericType() ) ) {
          IType compatibleRhsType;
          if( bRawAssignable ) {
            compatibleRhsType = rhsType;
          }
          else {
            compatibleRhsType = TypeLord.findParameterizedType( rhsType, lhsType.getGenericType() );
            if( compatibleRhsType == null ) {
              return false;
              //throw new IllegalStateException(); // generic types are assignable, so there must be a corresponding compatible type
            }
          }
          IType[] lhsParams = lhsType.getTypeParameters();
          IType[] rhsParams = compatibleRhsType.getTypeParameters();
          for( int i = 0; i < lhsParams.length; i++ ) {
            if( !rhsParams[i].isAssignableFrom( lhsParams[i] ) &&
                !canCast( lhsParams[i], rhsParams[i] ) ) {
              return false;
            }
          }
          return true;
        }
      }
    }
    return false;
  }

  private boolean canCastCompountType( ICompoundType lhsType, IType rhsType )
  {
    for( IType type: lhsType.getTypes() ) {
      if( !canCast( type, rhsType ) ) {
        return false;
      }
    }
    return true;
  }

  private boolean genericInterfacesClash( IType rhsType, IType lhsType ) {
    return _genericInterfacesClash( rhsType, lhsType ) || _genericInterfacesClash( lhsType, rhsType );
  }
  private boolean _genericInterfacesClash( IType rhsType, IType lhsType ) {
    if( !rhsType.isParameterizedType() || !lhsType.isParameterizedType() ) {
      return false;
    }
    IType lhsTypeInRhs = TypeLord.findParameterizedType( lhsType, rhsType.getGenericType() );
    if( lhsTypeInRhs != null ) {
      lhsTypeInRhs = TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( lhsTypeInRhs, lhsTypeInRhs.getEnclosingType() );
      return !rhsType.isAssignableFrom( lhsTypeInRhs ) && !lhsTypeInRhs.isAssignableFrom( rhsType );
    }
    return false;
  }

  private boolean canCastMetaType( IType lhsType, IType rhsType ) {
    return (lhsType instanceof IMetaType && ((IMetaType) lhsType).getType() instanceof ITypeVariableType && canCast( ((ITypeVariableType) ((IMetaType) lhsType).getType()).getBoundingType(), rhsType )) ||
      (rhsType instanceof IGosuClass && ((IGosuClass)rhsType).isStructure() &&
           (lhsType instanceof IMetaType && StandardCoercionManager.isStructurallyAssignable( rhsType, ((IMetaType) lhsType).getType() ) ||
            JavaTypes.CLASS().isAssignableFrom( lhsType ) && (!lhsType.isParameterizedType() || StandardCoercionManager.isStructurallyAssignable( rhsType, lhsType.getTypeParameters()[0] ))));
  }

  public IJavaType getPrimitiveType(String name) {
    return JavaType.getPrimitiveType(name);
  }

  public IType getPrimitiveType(IType boxType) {
    initBoxMapsIfNeeded();
    return _boxToPrimitiveMap.get(boxType);
  }

  public IType getBoxType(IType primitiveType) {
    initBoxMapsIfNeeded();
    return _primitiveToBoxMap.get(primitiveType);
  }

  public IType getExpandableComponentType( IType type ) {
    return TypeLord.getExpandableComponentType( type );
  }

  private void initBoxMapsIfNeeded() {
    if (_boxToPrimitiveMap == null) {
      TypeSystem.lock();
      try {
        if (_boxToPrimitiveMap == null) {
          _boxToPrimitiveMap = new HashMap<>();
          _primitiveToBoxMap = new HashMap<>();
          mapBoxType(JavaTypes.VOID(), JavaTypes.pVOID());
          mapBoxType(JavaTypes.BOOLEAN(), JavaTypes.pBOOLEAN());
          mapBoxType(JavaTypes.BYTE(), JavaTypes.pBYTE());
          mapBoxType(JavaTypes.CHARACTER(), JavaTypes.pCHAR());
          mapBoxType(JavaTypes.DOUBLE(), JavaTypes.pDOUBLE());
          mapBoxType(JavaTypes.FLOAT(), JavaTypes.pFLOAT());
          mapBoxType(JavaTypes.INTEGER(), JavaTypes.pINT());
          mapBoxType(JavaTypes.LONG(), JavaTypes.pLONG());
          mapBoxType(JavaTypes.SHORT(), JavaTypes.pSHORT());
        }
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  private void mapBoxType(IType boxType, IType primitiveType) {
    _boxToPrimitiveMap.put(boxType, primitiveType);
    _primitiveToBoxMap.put(primitiveType, boxType);
  }

  public IExecutionEnvironment getExecutionEnvironment() {
    return _env == null ? _env = ExecutionEnvironment.instance() : _env;
  }

  @Override
  public IGosuClassLoader getGosuClassLoader() {
    DefaultTypeLoader loader = (DefaultTypeLoader) getModule().getTypeLoaders(IDefaultTypeLoader.class).get(0);
    return loader.getGosuClassLoader();
  }

  @Override
  public void dumpGosuClassLoader() {
    DefaultTypeLoader loader = (DefaultTypeLoader) getModule().getTypeLoaders(IDefaultTypeLoader.class).get(0);
    loader.dumpGosuClassLoader();
  }

  @Override
  public IType replaceTypeVariableTypeParametersWithBoundingTypes( IType type, IType enclosingType ) {
    return TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( type, enclosingType );
  }

  @Override
  public IMetaType getDefaultType() {
    return (IMetaType) MetaType.DEFAULT_TYPE_TYPE.get();
  }

  @Override
  public IType getFunctionalInterface( IFunctionType type )
  {
    return TypeLord.getFunctionalInterface( type );
  }
}
