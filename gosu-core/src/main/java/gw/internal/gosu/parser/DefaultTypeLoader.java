/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.parser.java.classinfo.JavaSourceClass;
import gw.internal.gosu.properties.PropertiesSourceProducer;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IExtendedTypeLoader;
import gw.lang.reflect.IType;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.SimpleTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.ISourceProducer;
import gw.lang.reflect.gs.SourceProducerSourceFileHandle;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.module.IModule;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class DefaultTypeLoader extends SimpleTypeLoader implements IExtendedTypeLoader, IDefaultTypeLoader {
  private ClassCache _classCache;
  private IGosuClassLoader _gosuClassLoader;            //## todo: use a ConcurrentWeakValueHashMap here?
  private Map<String, IJavaClassInfo> _classInfoCache = new ConcurrentHashMap<>( 1000 );
  protected Set<String> _namespaces;

  public static DefaultTypeLoader instance(IModule module) {
    if (module == null) {
      module = ExecutionEnvironment.instance().getJreModule();
    }
    return module.getModuleTypeLoader().getTypeLoader(DefaultTypeLoader.class);
  }

  public DefaultTypeLoader(final IModule module) {
    super(module);
    _classCache = new ClassCache(module);
  }

  public static DefaultTypeLoader instance() {
    IModule module = TypeSystem.getCurrentModule();
    return instance(module);
  }

  @Override
  public IType getType(String fullyQualifiedName) {
    IJavaClassInfo classInfo = getJavaClassInfo( fullyQualifiedName );
    if (classInfo != null) {
      return JavaType.create(classInfo, this);
    } else {
      return null;
    }
  }

  public IJavaType getInnerType(String fqn) {
    IType cachedType = ((ModuleTypeLoader) _module.getModuleTypeLoader()).getCachedType( fqn );
    if (cachedType != null && !(cachedType instanceof IErrorType)) {
      return (IJavaType) cachedType;
    } else {
      return (IJavaType) getType(fqn);
    }
  }

  public IJavaClassInfo getJavaClassInfo(String fullyQualifiedName) {
    if (fullyQualifiedName.startsWith("[")) {
      throw new IllegalArgumentException("Cannot call getJavaClassInfo with a raw array descriptor");
    }
    if (ExecutionMode.isIDE() && _module != TypeSystem.getJreModule() && _module.equals(TypeSystem.getGlobalModule())) {
      return null;
    }

    // strip off all trailing array brackets "[]"
    String fqnNoArrays = ModuleTypeLoader.stripArrayBrackets(fullyQualifiedName);

    IJavaClassInfo result = _classInfoCache.get(fqnNoArrays);
    if (result == null) {
      result = resolveJavaClassInfo(fqnNoArrays);
      if (result == null) {
        result = IJavaClassInfo.NULL_TYPE;
      }
      _classInfoCache.put(fqnNoArrays, result);
    }
    if( result != IJavaClassType.NULL_TYPE ) {
      int numArrays = (fullyQualifiedName.length() - fqnNoArrays.length()) / 2;
      for( int i = 0; i < numArrays; i++ )
      {
        result = result.getArrayType();
      }
    }
    return result == IJavaClassInfo.NULL_TYPE ? null : result;
  }

  @Override
  public IJavaClassInfo getJavaClassInfoForClassDirectly(Class clazz, IModule module) {
    return new ClassJavaClassInfo(clazz, module);
  }

  public IJavaClassInfo getJavaClassInfo( Class aClass, IModule gosuModule ) {
    if( ExecutionMode.isIDE() && _module != TypeSystem.getJreModule() && _module.equals( TypeSystem.getGlobalModule() ) ) {
      return null;
    }
    String fullyQualifiedName = aClass.getName().replace('$', '.');
    IJavaClassInfo result = _classInfoCache.get( fullyQualifiedName );
    if( result == null ) {
      result = new ClassJavaClassInfo( aClass, gosuModule );
      _classInfoCache.put( fullyQualifiedName, result );
    }
    return result == IJavaClassInfo.NULL_TYPE ? null : result;
  }

  public IJavaClassInfo getJavaClassInfo( AsmClass aClass, IModule gosuModule ) {
    if (ExecutionMode.isIDE() && _module != TypeSystem.getJreModule() && _module.equals( TypeSystem.getGlobalModule() )) {
      return null;
    } else {
    String fullyQualifiedName = aClass.getName().replace('$', '.');
    IJavaClassInfo result = _classInfoCache.get( fullyQualifiedName );
    if( result == null ) {
      result = new AsmClassJavaClassInfo( aClass, gosuModule );
      _classInfoCache.put( fullyQualifiedName, result );
    }
    return result == IJavaClassInfo.NULL_TYPE ? null : result;
  }
  }

  public IJavaClassInfo resolveJavaClassInfo( String fqn )
  {
    // We can load IJavaClassInfo from .class file or from .java file.
    //
    // In addition to loading bytecode directly by .class file,
    // we can also load bytecode indirectly by .java file via dynamic compilation.
    //
    // How does this work?
    //
    // - .class file
    // -- if there is a .class file for the name for the fqn, we load either using
    // --- ASM, which is only done during compilation or in an IDE, or using
    // --- class loading, which is done only at runtime
    //
    // - .java file
    // -- if there is a .java source file for the fqn, our Gosu URL handler intervenes
    //    with the primary class loader and dynamically compiles the .java file and
    //    and serves it up as if it were loaded from disk.  This is only done at
    //    runtime.
    //
    // If there is a .java file on disk for the type, Standard Gosu always loads
    // classinfo from source as JavaSourceType (both in IDE/compiler and runtime).
    // However the class loader still dynamically compiles the java source
    // in the course of running a program.  Likewise, JavaSourceType leverages
    // this in its implementation of getBackingClass(), which simply call Class.forName()
    // to dynamically compile the source via our class loading transmogrifier.
    //
    //                              JavaClassInfo
    //                                    +
    //                 +------------------+-----------------+
    //                 |                                    |
    //                 v                                    v
    //          Bytecode-based                         Source-based
    //                 +                                    +
    //       +---------+---------+                          |
    //       |                   |                          |
    //       v                   v                          |
    // ASM Processing       Class loading                   |
    //       +                   +                          |
    //       |            +------+------+            +------+-----+
    //       |            |             |            |            |
    //       v            v             v            v            v
    //  .class file   .class file  .java file   .java file    .gs file (!)
    //                                  +            +            +
    //                                  |            |            |
    //                                  v            v            v
    //                               Dynamic    Java parser   Gosu Stub
    //                               compiler                   file
    //
    // (!) Note Java from .gs file supports the use-case where a Java source file
    // references a Gosu class e.g., in a method return type.  Gosu needs to get
    // the type info at the *IJavaClassInfo* level for the the Gosu class.

    if( !ExecutionMode.isIDE() && classFileExists( fqn ) )
    {
      // If not in an IDE, favor .class files as the basis for Java types.
      //
      // Note since we can dynamically compile and load a Java class from Java source and since this process can
      // be triggered via the class loader, we must avoid loading it via getByClass(), which will indirectly load
      // the bytecode class via source.
      //
      // We avoid this at runtime for the use-case where we are running both a Gosu class and a Java class from
      // source.  The Java class extends the Gosu class (the Gosu class may be generic) and the Gosu class references
      // the Java class in one of its member declaration types.
      //
      // Essentially, in order for Java and Gosu to reference each other in this way Gosu we must parse against our
      // declaration-level source-based IJavaClassInfo, not the full bytecode class-based IJavaClasaInfo.
      // Otherwise the bytecode IJavaClassInfo resolves Gosu references via on-demand Java stub file generation from
      // declaration-compiled Gosu, which won't work since declaration-compiled Gosu needs the IJavaClassInfo for
      // the Java class reference.  The source-based IJavaClassInfo can resolve type references using lazy Java
      // stub files generated from Gosu and avoid this paradox.

      return getByClass( fqn, _module, _module );
    }

    // First check for a .java file and load a JavaSourceType...
    ISourceFileHandle fileHandle = getSourceFileHandle( fqn );
    if( fileHandle == null )
    {
      // If no .java file is found, load from .class file
      return getByClass( fqn, _module, _module );
    }

    if( fileHandle.getParentType() != null && !fileHandle.getParentType().isEmpty() )
    {
      String parentType = fileHandle.getTypeNamespace();
      IJavaClassInfo parentClassInfo = getJavaClassInfo( parentType );
      if( parentClassInfo == null )
      {
        return null;
      }
      IJavaClassInfo[] declaredClasses = parentClassInfo.getDeclaredClasses();
      IJavaClassInfo inner = null;
      for( IJavaClassInfo declaredClass : declaredClasses )
      {
        String name = declaredClass.getName();
        // cache all inner classes now
        if( !_classInfoCache.containsKey( name ) )
        {
          _classInfoCache.put( name, declaredClass );
        }
        //## todo: these names should be consistent
        if( fqn.equals( name ) || name.replace( '$', '.' ).equals( fqn ) )
        {
          inner = declaredClass;
        }
      }
      return inner;
    }

    return JavaSourceClass.createTopLevel( fileHandle, _module );
  }

  @Override
  public ISourceFileHandle getSourceFileHandle( String fqn ) {
    ISourceFileHandle aClass = _module.getFileRepository().findClass( fqn, EXTENSIONS_ARRAY );
    if( aClass == null ) {
      aClass = loadFromSourceProducer( fqn );
    }
    else if( !aClass.getClassType().isJava() ) {
      aClass = null;
    }
    return aClass;
  }

  private ISourceFileHandle loadFromSourceProducer( String fqn )
  {
    for( ISourceProducer sp: getJavaSourceProducers() )
    {
      if( sp.isType( fqn ) )
      {
        if( sp.isTopLevelType( fqn ) )
        {
          return new SourceProducerSourceFileHandle( fqn, sp );
        }
        else
        {
          int iLastDot = fqn.lastIndexOf( '.' );
          String enclosingClass = fqn.substring( 0, iLastDot );
          String simpleName = fqn.substring( iLastDot+1 );
          return new InnerClassFileSystemSourceFileHandle( sp.getClassType( fqn ), enclosingClass, simpleName, false );
        }
      }
    }
    return null;
  }

  private IJavaClassInfo getByClass( String className, IModule lookupModule, IModule actualModule ) {
    DefaultTypeLoader loader = (DefaultTypeLoader)lookupModule.getTypeLoaders( IDefaultTypeLoader.class ).get( 0 );
    if( ExecutionMode.isRuntime() ) {
      Class theClass = loader.loadClass( className );
      if( theClass == null ) {
        return null;
      }
      return getJavaClassInfo( theClass, actualModule );
    } else {
      AsmClass theClass = loader.loadAsmClass( className );
      if( theClass == null ) {
        return null;
      }
      return getJavaClassInfo( theClass, actualModule );
    }
  }

  @Override
  public IType getIntrinsicTypeFromObject(Object object) {
    if (object == null) {
      return null;
    }

    if (object instanceof IGosuObject) {
      if (object instanceof AbstractTypeRef) {
        object = ((AbstractTypeRef) object)._getType();
      }
      IType type = ((IGosuObject) object).getIntrinsicType();
      if( type instanceof IBlockClass ) {
        return ((IBlockClass)type).getBlockType();
      }
      return type;
    }

    if (object instanceof IType) {
      return MetaType.get((IType) object);
    }

    return TypeSystem.get(object.getClass()); // Call allllll the way back through the stack if this is a Class;
  }

  @Override
  public Set<String> computeTypeNames() {
    Set<String> allTypeNames = _classCache.getAllTypeNames();
    allTypeNames.addAll(_module.getFileRepository().getAllTypeNames(DOT_JAVA_EXTENSION));
    getJavaSourceProducers().forEach( sp -> allTypeNames.addAll( sp.getAllTypeNames() ) );
    return allTypeNames;
  }

  @Override
  public URL getResource(String name) {
    return getGosuClassLoader().getActualLoader().getResource(name);
  }

  @Override
  public void refreshedTypesImpl(RefreshRequest request) {
    for (String fullyQualifiedTypeName : request.types) {
      _classCache.remove(fullyQualifiedTypeName);
      _classInfoCache.remove(fullyQualifiedTypeName);
    }
    _module.getFileRepository().typesRefreshed( request );
  }

  @Override
  public boolean isCaseSensitive() {
    return true;
  }

  @Override
  public List<String> getHandledPrefixes() {
    return Collections.emptyList();
  }

  @Override
  public boolean handlesNonPrefixLoads() {
    return true;
  }

  public void refreshedImpl() {
    JavaType.unloadTypes();
    if (ExecutionMode.isRuntime()) {
      _classCache.clearClasspathInfo();
    } else {
      _classCache.dispose();
      _classCache = new ClassCache(getModule());
      dumpGosuClassLoader();
    }
    _namespaces = null;

    _classInfoCache.clear();

    doForAllSourceProviders( ISourceProducer::clear );

    _module.getFileRepository().typesRefreshed( null );

    JavaTypes.flushCache();
  }

  public void clearMisses() {
    Iterator<Map.Entry<String,IJavaClassInfo>> iterator = _classInfoCache.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, IJavaClassInfo> entry = iterator.next();
      if (entry.getValue() == IJavaClassInfo.NULL_TYPE) {
        iterator.remove();
      }
    }
  }

  public Class loadClass(String className) {
    return _classCache.loadClass( className );
  }

  public AsmClass loadAsmClass(String className) {
    return _classCache.loadAsmClass( className );
  }

  public boolean classFileExists( String className ) {
    return _classCache.classFileExists( className );
  }

  public IGosuClassLoader getGosuClassLoader() {
    if (_gosuClassLoader == null) {
      _gosuClassLoader = new GosuClassLoader(_module.getModuleClassLoader());
      GosuClassPathThing.init();
    }
    return _gosuClassLoader;
  }

  void dumpGosuClassLoader() {
    if (_gosuClassLoader != null) {
      _gosuClassLoader.dumpAllClasses();
      // The module classloader should be a PluginContainer, which should be new at this point; the old one should be disposed
      if( !haveWeRecreatedTheModuleLoader() ) {
        // Gosu's classloader hasn't been recreated, which implies the _classCache was not recreated,
        // which means we have to manually dump and recreate the plugin loader and the module loader.
        _classCache.reassignClassLoader();
      }
      _gosuClassLoader.assignParent( _module.getModuleClassLoader() );
      GosuClassPathThing.init();
    }
  }

  private boolean haveWeRecreatedTheModuleLoader() {
    ClassLoader gosusLoader = _gosuClassLoader.getActualLoader();
    ClassLoader csr = _module.getModuleClassLoader();
    while( csr != null ) {
      if( csr == gosusLoader ) {
        return false;
      }
      csr = csr.getParent();
    }
    return true;
  }

  @Override
  public Set<String> getExtensions() {
    return EXTENSIONS;
  }

  @Override
  public boolean hasNamespace(String namespace) {
    return _module.getFileRepository().hasNamespace(namespace) > 0 || _classCache.hasNamespace(namespace);
  }

  @Override
  public Set<String> getAllNamespaces() {
    if (_namespaces == null) {
      try {
        _namespaces = TypeSystem.getNamespacesFromTypeNames(getAllTypeNames(), new HashSet<String>());
      } catch (NullPointerException e) {
        //!! hack to get past dependency issue with tests
        return Collections.emptySet();
      }
    }
    return _namespaces;
  }

  @Override
  public void refreshedNamespace(String namespace, IDirectory dir, RefreshKind kind) {
    if (_namespaces != null) {
      if (kind == RefreshKind.CREATION) {
        _namespaces.add(namespace);
      } else if (kind == RefreshKind.DELETION) {
        _namespaces.remove(namespace);
      }
    }
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    Set<TypeName> names = new HashSet<TypeName>();
    names.addAll(_module.getFileRepository().getTypeNames(namespace, Collections.singleton(".java"), this));
    names.addAll(_classCache.getTypeNames(namespace));

    doForAllSourceProviders( sp -> names.addAll( sp.getTypeNames( namespace ) ) );

    return names;
  }


  @Override
  public <T> List<T> getInterface( Class<T> apiInterface )
  {
    if( apiInterface.getName().equals( "editor.plugin.typeloader.ITypeFactory" ) )
    {
      List<T> impls = new ArrayList<T>();
      try
      {
        //noinspection unchecked
        impls.add( (T)Class.forName( "editor.plugin.typeloader.java.JavaTypeFactory" ).newInstance() );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }

      doForAllSourceProviders( sp -> impls.addAll( sp.getInterface( apiInterface ) ) );

      return impls;
    }
    return super.getInterface( apiInterface );
  }

  private void doForAllSourceProviders( Consumer<ISourceProducer> consumer )
  {
    getJavaSourceProducers().forEach( consumer );
    getGosuSourceProducers().forEach( consumer );
  }

  @Override
  protected void addBuiltInSourceProducers( Set<ISourceProducer> set )
  {
    set.add( new PropertiesSourceProducer( this ) );
  }
}
