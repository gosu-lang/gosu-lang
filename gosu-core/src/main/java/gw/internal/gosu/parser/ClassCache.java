/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.ExecutionMode;
import gw.fs.IFile;
import gw.internal.gosu.module.DefaultSingleModule;
import gw.lang.reflect.java.asm.AsmClass;
import gw.lang.reflect.java.asm.AsmClassLoader;
import gw.lang.reflect.IInjectableClassLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LockingLazyVar;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassCache {
  @SuppressWarnings({"unchecked"})
  private final Map<String, Class> _classMap = new HashMap<String, Class>();
  private Set<CharSequence> _packages = new HashSet<CharSequence>();
  private IModule _module;
  private LockingLazyVar<ClassPath> _classPathCache;
  private LockingLazyVar<Set<String>> _allTypeNamesCache = new LockingLazyVar<Set<String>>() {
    @Override
    protected Set<String> init() {
      HashSet<String> strings = new HashSet<String>();
      Set<String> filteredClassNames = _classPathCache.get().getFilteredClassNames();
      for (String className : filteredClassNames) {
        strings.add(className.replace('$', '.'));
      }
      return strings;
    }
  };
  private AsmClassLoader _asmClassLoader;
  private boolean ignoreTheCache;

  public ClassCache(final IModule module) {
    _module = module;
    ignoreTheCache = ExecutionMode.isRuntime();
    _classPathCache =
      new LockingLazyVar<ClassPath>() {
        protected ClassPath init() {
          return
            new ClassPath( _module,
                    ExecutionMode.isRuntime() ?
                        IClassPath.ONLY_API_CLASSES : // FIXME-isd: for performance reasons, only select API classes
                        IClassPath.ALLOW_ALL_WITH_SUN_FILTER);
        }
      };
    _asmClassLoader = new AsmClassLoader(_module);
  }

  private Class tryToLoadClass(CharSequence name) {
    if (_packages.contains(name)) {
      // A package name, valid or not, is never a Class, so fail immediately
      return ClassNotFoundMarkerClass.class;
    }

    try {
      Class<?> cls;
      try {
        cls = _module.getModuleClassLoader().loadClass(name.toString());
      } catch (ClassNotFoundException cnfe) {
        return ClassNotFoundMarkerClass.class;
      }
      if (cls == null) {
        return ClassNotFoundMarkerClass.class;
      }
      if (!cls.isArray() && !cls.isPrimitive()) {
        addFoundPackages(cls);
      }
      return cls;
    } catch (VerifyError e) {
      // Probably could not inherit from final class
      return VerifyError.class;
    }
  }

  private void addFoundPackages(Class<?> aClass) {
    String packageName = aClass.getName();
    if (packageName.contains(".")) {
      packageName = packageName.substring(0, packageName.lastIndexOf('.'));
      int dotIndex = 0;
      while (dotIndex != -1) {
        int nextDot = packageName.indexOf('.', dotIndex);
        _packages.add(packageName.subSequence(0, nextDot == -1 ? packageName.length() : nextDot));
        dotIndex = nextDot == -1 ? -1 : nextDot + 1;
      }
    }
  }

  public AsmClass loadAsmClass( String className ) {
    AsmClass primitiveClazz = AsmClass.findPrimitive( className );
    try {
      IModule jreModule = _module.getExecutionEnvironment().getJreModule();
      if( jreModule == _module && primitiveClazz != null ) {
        return primitiveClazz;
      }
    }
    catch( Exception e ) {
      // ignore, jreModule isn't available yet
    }

    if( _classPathCache.get().isEmpty() ) {
      return null;
    }

    StringBuilder s = new StringBuilder( className );
    int i;
    do {
      if( ignoreTheCache || _classPathCache.get().contains( className ) ) {
        IFile file = _classPathCache.get().get( className );
        if( file != null ) {
          try {
            return _asmClassLoader.findClass( className, file.openInputStream() );
          }
          catch( IOException e ) {
            throw new RuntimeException( e );
          }
        }
      }
      i = s.lastIndexOf( "." );
      if( i >= 0 ) {
        if( isPackage( s, i ) ) {
          return null;
        }
        s.setCharAt( i, '$' );
        className = s.toString();
      }
    } while( i >= 0 );

    return null;
  }

  public Class loadClass(String className) {
    Class primitiveClazz = Primitives.get(className);
    if (_module.getExecutionEnvironment().getJreModule() == _module && primitiveClazz != null) {
      return primitiveClazz;
    }

    StringBuilder s = new StringBuilder(className);
    int i;
    do {
      boolean isInClassPathCache = _classPathCache.get().contains(className);
      if (ignoreTheCache || isInClassPathCache) {
        Class aClass = loadClassImplImpl(className);
        if (aClass != null) {
          return aClass;
        }
      }
      i = s.lastIndexOf(".");
      if (i >= 0) {
        if( isPackage( s, i ) ) {
          return null;
        }
        s.setCharAt(i, '$');
        className = s.toString();
      }
    } while (i >= 0);

    return null;
  }

  /**
   * Short-circuit the case where com.foo.Fred is not a class name, but
   * com.foo is a package.  Avoid the expensive test for com.foo$Fred as a an
   * inner class (and then com$foo$Fred).
   * <p>
   * Yes, java supports a package and a class having the same name, but in this case
   * we are checking for an inner class of a class having the same name as a package...
   * Let's just not support references to those in Gosu in the name of both sanity
   * and performance.
   * <p>
   * Warning this design decision was not vetted through various committees, architect
   * round tables, community processes, or guys with beards.
   */
  private boolean isPackage( StringBuilder s, int i ) {
    try {
      String maybePackage = s.substring( 0, i );
      if( getPackageMethod().invoke( _module.getModuleClassLoader(), maybePackage ) != null ) {
        return true;
      }
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
    return false;
  }

  private static Method _getPackageMethod = null;
  private Method getPackageMethod() {
    if( _getPackageMethod == null ) {
      try {
        _getPackageMethod = ClassLoader.class.getDeclaredMethod("getPackage", String.class);
        _getPackageMethod.setAccessible( true );
      }
      catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      }
    }
    return _getPackageMethod;
  }

  private Class loadClassImplImpl(String type) {
    type = normalizeArrayNotation(type);
    Class clazz = _classMap.get(type);
    if (clazz == null) {
      TypeSystem.lock();
      try {
        clazz = _classMap.get(type);
        if (clazz == null) {
          clazz = tryToLoadClass(type);
          _classMap.put(type, clazz);
        }
      }
      finally {
        TypeSystem.unlock();
      }
    }
    if (clazz != ClassNotFoundMarkerClass.class) {
      return clazz;
    } else {
      return null;
    }
  }

  /**
   * Normalizes a class name string to the appropriate java class name.  For example:
   * <p/>
   * java.lang.String -> java.lang.String
   * java.lang.String[] -> [Ljava.lang.String;
   * java.lang.String[][] -> [[Ljava.lang.String;
   *
   * @param type Name of type
   * @return ormalizes a class name string to the appropriate java class name
   */
  private static String normalizeArrayNotation(String type) {
    int arrayDimensions = 0;
    while (type.endsWith("[]")) {
      type = type.substring(0, type.length() - 2);
      arrayDimensions++;
    }

    if (arrayDimensions == 0) {
      return type;
    } else {
      String arraySignature = Primitives.getArraySignature(type);
      if (arraySignature != null) {
        type = arraySignature;
      } else {
        type = "[L" + type + ";";
      }
      for (int i = 1; i < arrayDimensions; i++) {
        type = "[" + type;
      }

      return type;
    }
  }

  public Set<String> getAllTypeNames() {
    return _allTypeNamesCache.get();
  }

  public void clearClasspathInfo() {
    _allTypeNamesCache.clear();
  }

  public void remove(String fullyQualifiedName) {
    TypeSystem.lock();
    try {
      _classMap.remove(fullyQualifiedName);
    }
    finally {
      TypeSystem.unlock();
    }
  }

  public void dispose() {
    _module.disposeLoader();
  }

  /**
   * Called in Single module mode.  If the parent loader of the ModuleClassLoader is the GosuPluginContainer,
   * we drop the ModuleClassLoader and its parent, the GosuPluginContainer. New ones are created and assigned here.
   * Note, this is a giant hack among many gianter hacks that keep the old test framework floating.
   */
  public void reassignClassLoader() {
    ClassLoader loader = _module.getModuleClassLoader();
    if( loader.getParent() instanceof IInjectableClassLoader ) {
      // Dispose the GosuPluginContainer "singleton" and create a new one
      ((IInjectableClassLoader)loader.getParent()).dispose();
      // Dispose the ModuleClassLoader and create new one, its parent will be the new GosuPluginContainer.
      // Note the ModuleClassLoader in the single module case does absolutely nothing as it has no classpath; it fully delegates to its parent.
      _module.disposeLoader();
    }
  }

  public boolean hasNamespace(String namespace) {
    return _classPathCache.get().hasNamespace(namespace);
  }

  public Set<TypeName> getTypeNames(String namespace) {
    return _classPathCache.get().getTypeNames(namespace);
  }

  /**
   * A non-instantiable marker class to help us store misses
   * in this typeloader
   */
  private static final class ClassNotFoundMarkerClass {
    private ClassNotFoundMarkerClass() {
    }
  }

}
