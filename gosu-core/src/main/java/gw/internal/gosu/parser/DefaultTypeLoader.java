/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import gw.config.ExecutionMode;
import gw.fs.IDirectory;
import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.parser.java.classinfo.JavaSourceClass;
import gw.internal.gosu.parser.java.classinfo.JavaSourceType;
import gw.lang.init.GosuTypeManifold;
import gw.lang.parser.IBlockClass;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IExtendedTypeLoader;
import gw.lang.reflect.IType;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.SimpleTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.module.IModule;

import gw.util.StreamUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import manifold.api.util.Pair;
import manifold.internal.javac.ClassSymbols;
import manifold.internal.javac.JavacPlugin;
import manifold.internal.runtime.Bootstrap;

public class DefaultTypeLoader extends SimpleTypeLoader implements IExtendedTypeLoader, IDefaultTypeLoader {
  private ClassCache _classCache;
  private IGosuClassLoader _gosuClassLoader;            //## todo: use a ConcurrentWeakValueHashMap here?
  private Map<String, IJavaClassInfo> _classInfoCache = new ConcurrentHashMap<String, IJavaClassInfo>(1000);
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

  public IJavaClassInfo getJavaClassInfo( AsmClass asmClass, IModule gosuModule ) {
    if (ExecutionMode.isIDE() && _module != TypeSystem.getJreModule() && _module.equals( TypeSystem.getGlobalModule() )) {
      return null;
    } else {
    String fullyQualifiedName = asmClass.getName().replace('$', '.');
    IJavaClassInfo result = _classInfoCache.get( fullyQualifiedName );
    if( result == null ) {
      result = new AsmClassJavaClassInfo( asmClass, gosuModule );
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

    boolean compilerModeOrClassFile = !ExecutionMode.isIDE() && (JavacPlugin.instance() != null || classFileExists( fqn ));
    if( compilerModeOrClassFile )
    {
      // If compiling with javac, favor javac as the basis for Java types.
      //
      // Note since we can dynamically compile and load a Java class from Java source and since this process can
      // be triggered via the class loader, we must avoid loading it via getByClass(), which will indirectly load
      // the bytecode class via source.

      IJavaClassInfo classInfo = getFromClassFileOrClassSymbol( fqn, _module, _module );
      if( classInfo != null )
      {
        return classInfo;
      }
    }

    // Attempt to load from a physical .java source file
    //
    IJavaClassInfo classInfo = getFromSourceFile( fqn );
    if( classInfo != null )
    {
      return classInfo;
    }

    if( !compilerModeOrClassFile )
    {
      // Load from .class file or source
      //
      classInfo = getFromClassFileOrClassSymbol( fqn, _module, _module );
    }

    return classInfo;
  }

  private IJavaClassInfo getFromSourceFile( String fqn )
  {
    // First check for a .java file and load a JavaSourceType...
    ISourceFileHandle fileHandle = getSourceFileHandle( fqn );
    if( fileHandle == null )
    {
      // If no .java file is found
      return null;
    }

    // loading from .java file...

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
  public ISourceFileHandle getSourceFileHandle(String qualifiedName) {
    ISourceFileHandle aClass = _module.getFileRepository().findClass(qualifiedName, EXTENSIONS_ARRAY);
    if (aClass == null || !aClass.getClassType().isJava()) {
      return null;
    }
    return aClass;
  }

  private IJavaClassInfo getFromClassFileOrClassSymbol( String fqn, IModule lookupModule, IModule actualModule ) {
    DefaultTypeLoader loader = (DefaultTypeLoader)lookupModule.getTypeLoaders( IDefaultTypeLoader.class ).get( 0 );
    if( ExecutionMode.isRuntime() ) {

      // Runtime

      Class theClass = loader.loadClass( fqn );
      if( theClass == null ) {
        return null;
      }
      return getJavaClassInfo( theClass, actualModule );
    }
    else {

      // Compile-time (or IDE)

      // When in javac prefer the ClassSymbol as the source of type info

      if( JavacPlugin.instance() != null )
      {
        // First, see if the type is Enter complete in javac. Meaning:
        //
        // 1. it's a Java *source* file (maybe from Manifold) and
        // 2. its members' types are resolved, thus
        // 3. it's safe to get type info from javac's ClassSymbol for it, which we prefer over loading and parsing the
        //    type ourselves, particularly if it is used on the java side too (sharing is good)

        if( GosuTypeManifold.isPostJava() || isJavacProcessedType( fqn ) )
        {
          AsmClass theClass = loader.loadAsmClassUsingJavac( fqn );
          if( theClass != null )
          {
            return getJavaClassInfo( theClass, actualModule );
          }
        }

        // Next, we attempt to load from source and parse it ourselves (only declarations).
        //
        // Note the source can come from Manifold, which comes from a ClassSymbol. Thus, failing a check for a physical
        // file e try to find the CompilationUnit from a javac ClassSymbol (the source of which may come from Manifold).
        IJavaClassInfo parsedClassInfo = maybeLoadFromSource( fqn );
        if( parsedClassInfo != null )
        {
          return parsedClassInfo;
        }
      }

      // Finally, load from the .class file using either javac ClassSymbol or ASM
      //
      AsmClass theClass = loader.loadAsmClass( fqn );
      return theClass == null ? null : getJavaClassInfo( theClass, actualModule );
    }
  }

  private boolean isJavacProcessedType( String fqn )
  {
    Map<String, Boolean> typesToProcess = JavacPlugin.instance().getTypeProcessor().getTypesToProcess();
    return typesToProcess.containsKey( fqn );
  }

  private IJavaClassInfo maybeLoadFromSource( String fqn )
  {
    if( JavacPlugin.instance() == null )
    {
      // Not compiling with javac

      return null;
    }

    // First, try to load directly from a physical source file (trying a ClassSymbol first may cause gosu to load prematurely if there is a cycle with references)
    //
    IJavaClassInfo classInfo = maybeLoadDirectlyFromSourceFile( fqn ); //getFromSourceFile( fqn );
    if( classInfo != null )
    {
      return classInfo;
    }

    // Next, try to load from the source from a ClassSymbol (source may be from Manifold)
    //
    if( !GosuTypeManifold.isPostJava() )
    {
      // javac is still compiling .java files, so we have to consider ClassSymbols incomplete,
      // thus we load/parse from source or use ASM on .class file.

      ClassSymbols classSymbols = ClassSymbols.instance( JavacPlugin.instance().getHost().getSingleModule() );
      BasicJavacTask javacTask = JavacPlugin.instance().getJavacTask();
      Pair<Symbol.ClassSymbol, JCTree.JCCompilationUnit> classSymPair = classSymbols.getClassSymbol( javacTask, fqn );
      classInfo = null;
      if( classSymPair != null )
      {
        JCTree.JCCompilationUnit compUnit = classSymPair.getSecond();
        if( compUnit != null )
        {
          // .java file: a compilation unit indicates source (potentially dynamic source generated from Manifold)

          try
          {
            CharSequence source = compUnit.getSourceFile().getCharContent( true );
            classInfo = JavaSourceType.createTopLevel( new StringSourceFileHandle( fqn, source, false, ClassType.Class ),
              _module, null );
            // cache the top-level class + all its nested classes
            cacheSourceClass( fqn, classInfo );
          }
          catch( IOException ignore )
          {
            //todo: log:
            System.err.println( "***\nFailed to load " + fqn + " from source. ***\n" );
          }
        }
        else if( classSymPair.getFirst() != null )
        {
          // .class file: load using javac ClassSymbol

          AsmClass theClass = loadAsmClassUsingJavac( fqn );
          classInfo = theClass == null ? null : getJavaClassInfo( theClass, _module );
        }
      }
      return classInfo;
    }
    return null;
  }

  private IJavaClassInfo maybeLoadDirectlyFromSourceFile( String fqn )
  {
    try
    {
      if( classFileExists( fqn ) )
      {
        return null;
      }

      File file = null;
      String relative = fqn.replace( '.', File.separatorChar ) + ".java";
      for( String path: JavacPlugin.instance().getJavaSourcePath() )
      {
        File f = new File( path, relative );
        if( f.isFile() )
        {
          file = f;
          break;
        }
      }
      if( file == null )
      {
        return null;
      }
      String source = StreamUtil.getContent( new BufferedReader( new FileReader( file ) ) );
      IJavaClassInfo classInfo = JavaSourceType.createTopLevel( new StringSourceFileHandle( fqn, source, false, ClassType.Class ),
        _module, null );
      // cache the top-level class + all its nested classes
      cacheSourceClass( fqn, classInfo );
      return classInfo;
    }
    catch( IOException e )
    {
      return null; // 'tarded API :(
    }
  }

  private void cacheSourceClass( String fqn, IJavaClassInfo classInfo )
  {
    _classInfoCache.put( fqn, classInfo );
    for( IJavaClassInfo declaredClass: classInfo.getDeclaredClasses() )
    {
      cacheSourceClass( declaredClass.getName().replace( '$', '.' ), declaredClass );
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

  public AsmClass loadAsmClassUsingJavac(String className) {
    return _classCache.loadClassUsingJavac( className );
  }

  public boolean classFileExists( String className ) {
    return _classCache.classFileExists( className );
  }

  public IGosuClassLoader getGosuClassLoader() {
    if (_gosuClassLoader == null) {
      _gosuClassLoader = new GosuClassLoader(_module.getModuleClassLoader());
      Bootstrap.init();
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
      Bootstrap.init();
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
    return names;
  }

  @Override
  public <T> T getInterface( Class<T> apiInterface )
  {
    if( apiInterface.getName().equals( "editor.plugin.typeloader.ITypeFactory" ) )
    {
      try
      {
        //noinspection unchecked
        return (T)Class.forName( "editor.plugin.typeloader.java.JavaTypeFactory" ).newInstance();
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
    return super.getInterface( apiInterface );
  }
}
