/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.xml.xsd.ResourceFileXmlSchemaSource;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchema;
import gw.lang.ir.builder.IRClassBuilder;
import gw.lang.ir.builder.IRElementBuilder;
import gw.lang.ir.builder.IRMethodBuilder;
import gw.lang.reflect.*;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;
import gw.util.Pair;
import gw.util.concurrent.LockingLazyVar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static gw.lang.ir.builder.IRBuilderMethods.*;

public abstract class XmlSchemaResourceTypeLoaderBase<T> extends TypeLoaderBase implements IUninitializableTypeLoader, IResourceVerifier {

  private static IdentityHashMap<Class<? extends IXmlTypeData>,Class<?>> _typeProxies = new IdentityHashMap<Class<? extends IXmlTypeData>, Class<?>>();
  protected static Set<IXmlSchemaExceptionListener> _exceptionListeners = new HashSet<IXmlSchemaExceptionListener>();
  private boolean _processingTypeData;

  public static final String IGNORE_JAVA_CLASSES_PROPERTY = "gw.internal.xsd.useJavaClassesIfAvailable";
  private final String _fileExtension;
  protected static final String WSC_EXTENSION = "wsc";
  protected final Map<String, XmlSchemaIndex<T>> _schemasByNamespaceCache = new HashMap<String, XmlSchemaIndex<T>>();
  protected final LockingLazyVar<Map<String,IFile>> _schemaNamespaces = new LockingLazyVar<Map<String,IFile>>( TypeSystem.getGlobalLock() ) {
    @Override
    protected Map<String,IFile> init() {
      return loadAllSchemaNamespaces();
    }
  };
  protected final LockingLazyVar<Set<String>> _allNamespaces = new LockingLazyVar<Set<String>>( TypeSystem.getGlobalLock() ) {
    @Override
    protected Set<String> init() {
      return XmlSchemaResourceTypeLoaderBase.getAllNamespaces( true, XmlSchemaResourceTypeLoaderBase.this, getAdditionalSubPackages() );
    }
  };
  protected final LockingLazyVar<Set<String>> _allSchemaNamespacesAndSubNamespaces = new LockingLazyVar<Set<String>>( TypeSystem.getGlobalLock() ) {
    @Override
    protected Set<String> init() {
      return XmlSchemaResourceTypeLoaderBase.getAllNamespaces( false, XmlSchemaResourceTypeLoaderBase.this, getAdditionalSubPackages() );
    }
  };

  public XmlSchemaResourceTypeLoaderBase( String fileExtension, IModule module ) {
    super( module );
    _fileExtension = fileExtension;
  }

  public static Set<String> getAllNamespaces( boolean includeSuperNamespaces, XmlSchemaResourceTypeLoaderBase<?> typeLoader, String... additional ) {
    Set<String> namespaces = new HashSet<String>();
    for ( String namespace : typeLoader.getAllSchemaNamespaces() ) {
      addAllNamespacesForSchema( includeSuperNamespaces, typeLoader, namespaces, namespace, additional );
    }
    return namespaces;
  }

  public static void addAllNamespacesForSchema( boolean includeSuperNamespaces, XmlSchemaResourceTypeLoaderBase<?> typeLoader, Set<String> namespaces, String namespace, String... additional ) {
    if ( includeSuperNamespaces ) {
      addAllSubNamespaces( namespaces, namespace + typeLoader.getElementsNamespacePrefix() );
      addAllSubNamespaces( namespaces, namespace + typeLoader.getTypesNamespacePrefix() );
      addAllSubNamespaces( namespaces, namespace + typeLoader.getEnumerationsNamespacePrefix() );
      addAllSubNamespaces( namespaces, namespace + ".attributes" );
    }
    else {
      namespaces.add( namespace );
      namespaces.add( namespace + typeLoader.getElementsNamespacePrefix() );
      namespaces.add( namespace + typeLoader.getTypesNamespacePrefix() );
      namespaces.add( namespace + typeLoader.getEnumerationsNamespacePrefix() );
      namespaces.add( namespace + ".attributes" );
    }
    namespaces.add( namespace + typeLoader.getTypesNamespacePrefix() + ".simple" );
    namespaces.add( namespace + typeLoader.getTypesNamespacePrefix() + ".complex" );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() + ".types" );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() + ".types.simple" );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() + ".types.complex" );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() + ".elements" );
    namespaces.add( namespace + typeLoader.getAnonymousNamespacePrefix() + ".attributes" );
    for ( String suffix : additional ) {
      namespaces.add( namespace + "." + suffix );
    }
  }

  private static void addAllSubNamespaces(Set<String> namespaces, String namespace) {
    while ( true ) { // for gw.foo.bar, also add gw.foo and gw
      namespaces.add( namespace );
      int idx = namespace.lastIndexOf( '.' );
      if ( idx < 0 ) {
        break;
      }
      namespace = namespace.substring( 0, idx );
    }
  }

  public static void refreshSchemasFromAllTypeLoaders() {
    TypeSystem.lock();
    try {
      for ( XmlSchemaResourceTypeLoaderBase typeloader : getAllXmlSchemaTypeLoaders() ) {
        typeloader._allSchemaNamespacesAndSubNamespaces.clear();
        typeloader._schemasByNamespaceCache.clear();
        typeloader._schemaNamespaces.clear();
        typeloader.clearTypeNames();
        typeloader._allNamespaces.clear();
        TypeSystem.clearErrorTypes();
      }
      XmlSchemaIndex.clearNormalizedSchemaNamespaces();
      TypeSystem.refresh( true );
    }
    finally {
      TypeSystem.unlock();
    }
  }

  private static Iterable<XmlSchemaResourceTypeLoaderBase> getAllXmlSchemaTypeLoaders() {
    List<XmlSchemaResourceTypeLoaderBase> xmlSchemaTypeLoaders = new ArrayList<XmlSchemaResourceTypeLoaderBase>();
    for ( XmlSchemaResourceTypeLoaderBase loader : TypeSystem.getGlobalModule().getTypeLoaders(XmlSchemaResourceTypeLoaderBase.class) ) {
      xmlSchemaTypeLoaders.add( loader );
    }
    return xmlSchemaTypeLoaders;
  }

  public static XmlSchemaIndex<?> findSchemaForNamespace( IModule module, String gosuNamespace ) {
    if ( module == null ) {
      throw new IllegalArgumentException( "module cannot be null" );
    }
    for (IModule m : module.getModuleTraversalList()) {
      // Skip global module, but only if requested module is not global module itself
      if (m == TypeSystem.getGlobalModule() && module != m) {
        continue;
      }
      XmlSchemaIndex<?> schemaIndex;
      List<? extends XmlSchemaResourceTypeLoaderBase> typeLoaders = m.getTypeLoaders(XmlSchemaResourceTypeLoaderBase.class);
      for ( XmlSchemaResourceTypeLoaderBase loader : typeLoaders ) {
        schemaIndex = loader.getSchemaForNamespace( gosuNamespace );
        if ( schemaIndex != null ) {
          return schemaIndex;
        }
      }
    }
    return null;
  }

  protected Map<String,IFile> loadAllSchemaNamespaces() {
    Map<String,IFile> allSchemaNamespaces = new HashMap<String, IFile>();
    HashMap<String,IDirectory> nonOverridden = getNonOverriddenCollectionPaths();
    List<Pair<String,IFile>> pairs = getModule().getFileRepository().findAllFilesByExtension(_fileExtension);
    for ( Pair<String, IFile> pair : pairs ) {
      IFile file = pair.getSecond();
      String packageName = pair.getFirst();
      String path = getParentPath(packageName);
      IDirectory useDirectory = nonOverridden.get(path);
      if (useDirectory != null && !useDirectory.equals(file.getParent())) {
        continue;
      }
      packageName = convertPathToPackage( packageName );
      // TODO-dp the second check is a hack to prevent wsdls from pc-cmdline from being processed here
      if ( packageName.startsWith( "config." ) || packageName.startsWith( "webservices." )) {
        continue;
      }
      String schemaNamespace = XmlSchemaIndex.normalizeSchemaNamespace(packageName, pair.getFirst());
      if ( ! CommonServices.getXmlSchemaCompatibilityConfig().useCompatibilityMode(schemaNamespace) ) {
        if ( ! allSchemaNamespaces.containsKey( schemaNamespace ) ) {
          allSchemaNamespaces.put( schemaNamespace, file );
        }
      }
    }
    return allSchemaNamespaces;
  }

  private HashMap<String, IDirectory> getNonOverriddenCollectionPaths() {
    HashMap<String, IDirectory> map = new HashMap<String, IDirectory>();
    List<Pair<String,IFile>> pairs = getModule().getFileRepository().findAllFilesByExtension(WSC_EXTENSION);
    for ( Pair<String, IFile> pair : pairs ) {
      IFile file = pair.getSecond();
      IDirectory dir = file.getParent().dir(file.getBaseName());
      String collectionPath = getCollectionPath(pair.getFirst());
      if (!map.containsKey(collectionPath)) {
         map.put(collectionPath, dir);
      }
    }
    return map;
  }

  private String getParentPath(String path) {
    int pos = path.lastIndexOf('/');
    return pos == -1 ? "" : path.substring(0, pos);
  }

  private String getCollectionPath(String path) {
    return path.substring(0, path.length() - WSC_EXTENSION.length() - 1);
  }

  private String convertPathToPackage( String packageName ) {
    packageName = packageName.substring( 0, packageName.length() - _fileExtension.length() - 1 ).replace( '.', '_' ).replace( '/', '.' );
    return packageName;
  }

  // called from XmlSchemaTestUtil.gs
  @SuppressWarnings( { "UnusedDeclaration" } )
  public void addSchemas( List<IFile> resourceFiles ) {
    HashMap<Pair<URL, String>, XmlSchema> caches = new HashMap<Pair<URL, String>, XmlSchema>();
    List<XmlSchemaIndex> schemaIndexes = new ArrayList<XmlSchemaIndex>();
    for ( IFile resourceFile : resourceFiles ) {
      try {
        String relativePath = TypeSystem.getGlobalModule().getFileRepository().getResourceName(resourceFile.toURI().toURL());
        String packageName = convertPathToPackage( relativePath );
        String schemaNamespace = XmlSchemaIndex.normalizeSchemaNamespace( packageName, relativePath );
        XmlSchemaIndex<T> schemaIndex = addSchemaToCacheIfNeeded( schemaNamespace, resourceFile, caches );
        if ( schemaIndex != null ) {
          schemaIndexes.add( schemaIndex );
          addAllNamespacesForSchema( true, this, _allNamespaces.get(), schemaNamespace, getAdditionalSubPackages() );
          addAllNamespacesForSchema( false, this, _allSchemaNamespacesAndSubNamespaces.get(), schemaNamespace, getAdditionalSubPackages() );
        }
      }
      catch ( MalformedURLException e ) {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
    clearTypeNames();
    for ( XmlSchemaIndex schemaIndex : schemaIndexes ) {
      schemaIndex.validate( caches );
    }
  }

  protected String[] getAdditionalSubPackages() {
    return new String[] {};
  }

  public IXmlTypeData loadTypeData( String fullyQualifiedTypeName ) {
    if ( ! getAllSchemaNamespacesAndSubNamespaces().contains( GosuClassUtil.getPackage( fullyQualifiedTypeName ) ) ) {
      return null;
    }
    startProcessingTypeData();
    try {
      XmlSchemaIndex<?> schemaIndex = getSchemaForType( fullyQualifiedTypeName );
      if ( schemaIndex == null ) {
        return null;
      }
      return schemaIndex.getTypeData( fullyQualifiedTypeName );
    }
    finally {
      endProcessingTypeData();
    }
  }

  public Set<String> getAllSchemaNamespacesAndSubNamespaces() {
    return _allSchemaNamespacesAndSubNamespaces.get();
  }

  public XmlSchemaIndex<T> getSchemaForNamespace( String packageName ) {
    return getSchemaForNamespace(packageName, null);
  }

  XmlSchemaIndex<T> getSchemaForNamespace( String packageName, Map<Pair<URL,String>, XmlSchema> caches ) {
    XmlSchemaIndex<T> schemaIndex = null;
    IFile resourceFile = _schemaNamespaces.get().get(packageName);
    if ( resourceFile != null ) {
      schemaIndex = addSchemaToCacheIfNeeded( packageName, resourceFile, caches );
    }
    return schemaIndex;
  }

  protected XmlSchemaIndex<T> addSchemaToCacheIfNeeded( String packageName, IFile resourceFile, Map<Pair<URL, String>, XmlSchema> caches ) {
    XmlSchemaIndex<T> schemaIndex = _schemasByNamespaceCache.get(packageName);
    if ( schemaIndex == null ) {
      TypeSystem.lock();
      try {
        schemaIndex = loadSchemaForNamespace( packageName, resourceFile, caches );
        if ( schemaIndex != null ) {
          _schemasByNamespaceCache.put( packageName, schemaIndex );
          _schemaNamespaces.get().put( packageName, resourceFile );
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    return schemaIndex;
  }

  protected abstract XmlSchemaIndex<T> loadSchemaForNamespace( String namespace, IFile resourceFile, Map<Pair<URL,String>, XmlSchema> caches  );

  public Set<String> computeTypeNames() {
    TypeSystem.lock();
    try {
      Map<Pair<URL,String>,XmlSchema> caches = new HashMap<Pair<URL, String>, XmlSchema>();   // key <URL, namespace>
      startProcessingTypeData();
      try {
        Set<String> names = new HashSet<String>();
        for ( String namespace : getAllSchemaNamespaces() ) {
          XmlSchemaIndex<T> schemaIndex = getSchemaForNamespace( namespace, caches );
          if ( schemaIndex != null ) {
            names.addAll( schemaIndex.getAllTypeNames( caches ) );
          }
        }
        return names;
      } finally {
        endProcessingTypeData();
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public Collection<String> getAllSchemaNamespaces() {
    return _schemaNamespaces.get().keySet();
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.emptyList();
  }

  public String getDeprecatedReason() {
    return null;
  }

  public Set<String> getAllNamespaces() {
    return _allNamespaces.get();
  }

  public String getSchemaSchemaTypeName() {
    return "gw.xsd.w3c.xmlschema.Schema";
  }

  public String getElementsNamespacePrefix() {
    return "";
  }

  public String getTypesNamespacePrefix() {
    return ".types";
  }

  public String getEnumerationsNamespacePrefix() {
    return ".enums";
  }

  public String getAnonymousNamespacePrefix() {
    return ".anonymous";
  }

  public XmlSchemaIndex<?> getSchemaForType( String fullyQualifiedTypeName ) {
    Set<String> namespacesAndSubNamespaces = getAllSchemaNamespacesAndSubNamespaces();
    XmlSchemaIndex<?> schemaIndex = null;
    String packageName = fullyQualifiedTypeName;
    while ( true ) {
      int idx = packageName.lastIndexOf( '.' );
      if ( idx < 0 ) {
        break;
      }
      packageName = packageName.substring( 0, idx );
      if ( ! namespacesAndSubNamespaces.contains( packageName ) ) {
        return null;
      }
      schemaIndex = getSchemaForNamespace( packageName );
      if ( schemaIndex != null ) {
        break;
      }
    }
    return schemaIndex;
  }

  protected void endProcessingTypeData() {
    _processingTypeData = false;
  }

  protected void startProcessingTypeData() {
    if ( _processingTypeData ) {
      throw new RuntimeException( "Attempt to loadTypeData() while resolving types" );
    }
    _processingTypeData = true;
  }

  @Override
  public final IType getType( String fullyQualifiedName ) {
    final IXmlTypeData typeData = loadTypeData( fullyQualifiedName );
    if ( typeData == null ) {
      return null;
    }
    try {
      Class<?> typeProxy = _typeProxies.get( typeData.getClass() );
      if ( typeProxy == null ) {
        typeProxy = generateTypeProxy( typeData.getClass(), typeData.getAdditionalInterfaces() );
        _typeProxies.put( typeData.getClass(), typeProxy );
      }
      return TypeSystem.getOrCreateTypeReference( (IType) typeProxy.getConstructor( ITypeLoader.class, IXmlTypeData.class ).newInstance( this, typeData ) );
    }
    catch ( Exception ex ) {
      throw new RuntimeException( ex );
    }
  }

  private Class<?> generateTypeProxy( Class<?> typeDataClass, List<Class<?>> additionalInterfaces ) throws NoSuchMethodException {

    LinkedHashSet<Class<?>> ifaces = new LinkedHashSet<Class<?>>();
    ifaces.addAll( additionalInterfaces ); // wish we get these from the typedata class instead of the typedata instance
    ifaces.add( IXmlType.class );

    Set<Method> generatedMethods = new HashSet<Method>();
    IRClassBuilder classBuilder = new IRClassBuilder( typeDataClass.getName() + ITypeRefFactory.USER_PROXY_SUFFIX, getClassOfType());

    for ( Constructor ctor : getClassOfType().getDeclaredConstructors() ) {
      classBuilder.createConstructor()._public().copyParameters( ctor ).body(
        _superInit( passArgs( IJavaClassInfo.Util.get( ctor ) ) ),
        _return()
      ) ;
    }

    for ( Class<?> iface : ifaces ) {
      classBuilder.withInterface( iface );
      for ( Method method : iface.getMethods() ) {
        if ( generatedMethods.add( method ) ) {
          generateMethod( classBuilder, method, typeDataClass );
        }
      }

    }
    return classBuilder.define( typeDataClass.getClassLoader() );
  }

  protected Class<?> getClassOfType() {
    return XmlType.class;
  }

  private void generateMethod( IRClassBuilder classBuilder, Method methodToImplement, Class<?> typeDataClass ) throws NoSuchMethodException {
    // if method exists on XmlType, don't generate it
    Method methodToDelegateTo;
    try {
      getClassOfType().getMethod(methodToImplement.getName(), methodToImplement.getParameterTypes());
      return;
    }
    catch ( NoSuchMethodException e ) {
      methodToDelegateTo = typeDataClass.getMethod( methodToImplement.getName(), methodToImplement.getParameterTypes() );
    }

    // We need to preserve synthetic/bridge modifiers for the sake of covariant methods
    int modifiers = Modifier.PUBLIC;
    if (methodToImplement.isSynthetic() || methodToImplement.isBridge()) {
      modifiers = modifiers | Modifier.VOLATILE;
    }

    IRMethodBuilder builder = classBuilder.createMethod().withModifiers( modifiers ).
            name( methodToImplement.getName()).copyParameters( methodToImplement ).returns( methodToImplement.getReturnType() );

    List<IRElementBuilder> bodyElements = new ArrayList<IRElementBuilder>();

    //noinspection ObjectEquality
    if (methodToImplement.getReturnType() == void.class) {
      bodyElements.add( call( "getTypeData" ).cast( methodToDelegateTo.getDeclaringClass() ).call( IJavaClassInfo.Util.get( methodToDelegateTo ), passArgs( IJavaClassInfo.Util.get( methodToImplement ) ) ) );
      bodyElements.add( _return() );
    }
    else {
      bodyElements.add( _return( call( "getTypeData" ).cast( methodToDelegateTo.getDeclaringClass() ).call( IJavaClassInfo.Util.get( methodToDelegateTo ), passArgs( IJavaClassInfo.Util.get( methodToImplement ) ) ) ) );
    }

    builder.body( bodyElements );
  }

  public Collection<? extends IConstructorInfo> getAdditionalConstructors( IXmlSchemaTypeData<T> typeData ) {
    return Collections.emptyList();
  }

  public Collection<? extends IPropertyInfo> getAdditionalProperties( IXmlSchemaTypeData<T> typeData ) {
    return Collections.emptyList();
  }

  public Collection<? extends IMethodInfo> getAdditionalMethods( IXmlSchemaTypeData<T> typeData ) {
    return Collections.emptyList();
  }

  public XmlSchemaIndex.NormalizationMode getPropertyNameNormalizationMode() {
    return XmlSchemaIndex.NormalizationMode.PROPERCASE;
  }

  @Override
  public final boolean isCaseSensitive() {
    return true;
  }

  @Override
  public void uninitialize() {
    XmlSchemaIndex.clear();
  }

  public static void addExceptionListener( IXmlSchemaExceptionListener listener ) {
    _exceptionListeners.add(listener);
  }

  public static void removeExceptionListener( IXmlSchemaExceptionListener listener ) {
    _exceptionListeners.remove(listener);
  }

  public void schemaIndexingExceptionOccurred( String namespace, IFile resourceFile, Throwable t ) {
    for (IXmlSchemaExceptionListener exceptionListener : _exceptionListeners) {
      exceptionListener.exceptionOccurred( namespace, resourceFile, t );
    }

  }

  public String getFileExtension() {
    return _fileExtension;
  }

  @Override
  public boolean hasNamespace(String namespace) {
    //TODO-dp can we do better?
    return getAllNamespaces().contains(namespace);
  }


  @Override
  public void refreshedNamespace(String namespace, IDirectory dir, RefreshKind kind) {
    // anything to do ?
  }

  @Override
  public boolean handlesFile(IFile file) {
    return getFileExtension().equals( file.getExtension() );
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    String type = TypeSystem.getGlobalModule().pathRelativeToRoot(file.getParent());
    String packageName = (type.replace('/', '.') + '.' + file.getBaseName()).toLowerCase();
    XmlSchemaIndex<?> index = getSchemaForNamespace(packageName);
    if (index != null) {
      Set<String> allTypeNames = index.getAllTypeNames(null);
      return allTypeNames.toArray(new String[allTypeNames.size()]);
    }

    // No schema index yet -- must be a new type, need to refresh namespace type at least
    return new String[] { packageName };
  }

  protected void createdType( String typeName ) {
    if ( _typeNames != null ) {
      _typeNames.add( typeName );
    }
  }

  @Override
  public RefreshKind refreshedFile(IFile file, String[] types, RefreshKind kind) {
    _allSchemaNamespacesAndSubNamespaces.clear();
    _schemasByNamespaceCache.clear();
    _schemaNamespaces.clear();
    clearTypeNames();
    _allNamespaces.clear();
    TypeSystem.clearErrorTypes();
    XmlSchemaIndex.clearNormalizedSchemaNamespaces();
    return kind;
  }

  @Override
  protected boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public boolean showTypeNamesInIDE() {
    return true;
  }

  @Override
  public List<String> verifyResources() throws Exception {
    List<String> errorMessages = new ArrayList<String>();
    for ( Map.Entry<String, IFile> entry : loadAllSchemaNamespaces().entrySet() ) {
      IFile schemaSourceFile = entry.getValue();
      URL url = schemaSourceFile.toURI().toURL();
      String resourceName = getModule().getFileRepository().getResourceName( url );
      verifySchemaSourceFile( entry.getKey(), schemaSourceFile, errorMessages, resourceName, url );
    }
    return errorMessages;
  }

  protected void verifySchemaSourceFile( String packageName, IFile schemaSourceFile, List<String> errorMessages, String resourceName, URL url ) throws Exception {
    try {
      XmlSchemaIndex<T> schemaIndex = new XmlSchemaIndex<T>( this, packageName, new ResourceFileXmlSchemaSource( schemaSourceFile ), null );
      schemaIndex.validate( null );
    }
    catch ( Exception ex ) {
      ex.printStackTrace();
      Throwable t = ex;
      while ( t.getCause() != null ) {
        t = t.getCause();
      }
      errorMessages.add( resourceName + ": " + t.toString() );
    }
  }

}
