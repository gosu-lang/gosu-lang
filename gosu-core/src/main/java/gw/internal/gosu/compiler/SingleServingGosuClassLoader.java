/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.ir.TransformingCompiler;
import gw.lang.Gosu;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.TypeSystemLockHelper;
import gw.util.concurrent.ConcurrentHashSet;
import gw.util.concurrent.ConcurrentWeakValueHashMap;

import java.util.Map;
import java.util.Set;

public class SingleServingGosuClassLoader extends ClassLoader implements IGosuClassLoader
{
  static final Map<String, Class> CACHE = new ConcurrentWeakValueHashMap<String, Class>();

  private GosuClassLoader _parent;
  private Set<String> _classes; // reflects Gosu classes loaded in this loader

  public static Class getCached( ICompilableType gsClass ) {
    return CACHE.get( gsClass.getName() );
  }

  public static void clearCache() {
    CACHE.clear();
  }
  public static void clearCache( String gosuClassName ) {
    Class cls = CACHE.remove( gosuClassName );
    if( cls != null )
    {
      ClassLoader loader = cls.getClassLoader();
      if( loader instanceof SingleServingGosuClassLoader )
      {
        ((SingleServingGosuClassLoader)loader).unload( gosuClassName );
      }
    }
  }

  // for null sentinal only
  SingleServingGosuClassLoader()
  {
  }

  SingleServingGosuClassLoader( GosuClassLoader parent )
  {
    super( parent.getActualLoader() );
    _parent = parent;
    _classes = new ConcurrentHashSet<>();
  }

  public Class<?> findClass( String strName ) throws ClassNotFoundException
  {
    Class cls = CACHE.get( strName.replace( '$', '.' ) );
    if( cls != null ) {
      return cls;
    }
    return _parent.findClass( strName );
  }

  protected void unload( String gosuClassName )
  {
    // Remove all classes in this single-serving loader from the global cache
    _classes.forEach( CACHE::remove );
  }

  @Override
  protected Class<?> loadClass( String name, boolean resolve ) throws ClassNotFoundException {
    // Acquire the type system lock and this loader's lock in a consistent order to prevent deadlock.
    // Note this is only important here for the case where the parent loader of this loader
    // loads a gosu class that is turn needs to load

    TypeSystemLockHelper.getTypeSystemLockWithMonitor( this );
    try {
      int iDot = name.lastIndexOf( '.' );
      String ns;
      if( iDot > 0 && _parent.hasDiscreteNamespace( ns = name.substring( 0, iDot ) ) )
      {
        SingleServingGosuClassLoader loader = _parent.getDiscreteNamespaceLoader( ns );
        IType type = TypeSystem.getByFullNameIfValidNoJava( name );
        if( type instanceof ICompilableType )
        {
          return loader._defineClass( (ICompilableType)type );
        }
      }
      return super.loadClass( name, resolve );
    }
    finally {
      TypeSystem.unlock();
    }
  }

  @Override
  public void dumpAllClasses() {
    // Do nothing here:  it should be taken care of by the main classloader  
  }

  @Override
  public boolean waitForLoaderToUnload( String packageName, long millisToWait )
  {
    return _parent.waitForLoaderToUnload( packageName, millisToWait );
  }

  @Override
  public void evictLoader( String packageName )
  {
    _parent.evictLoader( packageName );
  }

  Class _defineClass( ICompilableType gsClass )
  {
    Class cls = getCached( gsClass );
    if( cls != null )
    {
      return cls;
    }

    byte[] classBytes = compileClass( gsClass, _parent.shouldDebugClass( gsClass ) );
    CompilationStatistics.instance().collectStats( gsClass, classBytes, true );

    String gosuClassName = gsClass.getName();
    if( classBytes == null )
    {
      throw new IllegalStateException( "Could not generate class for " + gosuClassName );
    }
    String strPackage = gsClass.getNamespace();
    if( getPackage( strPackage ) == null )
    {
      definePackage( strPackage, null, null, null, null, null, null, null );
    }

    cls = defineClass( gsClass.getJavaName(), classBytes, 0, classBytes.length );
    if( shouldCache(gsClass) )
    {
      CACHE.put( gosuClassName, cls );
      _classes.add( gosuClassName );
    }
    return cls;
  }

  private boolean shouldCache(ICompilableType gsClass) {
    return gsClass != null && !gsClass.getName().startsWith(Gosu.GOSU_SCRATCHPAD_FQN);
  }

  private byte[] compileClass( ICompilableType type, boolean debug )
  {
    return TransformingCompiler.compileClass( type, debug );
  }

  @Override
  public IJavaType getFunctionClassForArity(int length)
  {
    return null;
  }

  @Override
  public ClassLoader getActualLoader() {
    return this;
  }

  @Override
  public Class defineClass(String name, byte[] bytes) {
    return super.defineClass(name, bytes, 0, bytes.length);
  }

  @Override
  public byte[] getBytes(ICompilableType gsClass)
  {
    return compileClass( gsClass, false );
  }

  @Override
  public void assignParent( ClassLoader classLoader )
  {
    throw new UnsupportedOperationException( "Should not happen" );
  }

  public boolean isDisposed()
  {
    return false;
  }
}
