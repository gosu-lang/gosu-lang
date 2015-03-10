/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.ir.TransformingCompiler;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemLock;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.module.TypeSystemLockHelper;
import gw.util.concurrent.ConcurrentWeakValueHashMap;

import java.util.Map;

public class SingleServingGosuClassLoader extends ClassLoader implements IGosuClassLoader
{
  private static final Map<String, Class> CACHE = new ConcurrentWeakValueHashMap<String, Class>();

  private GosuClassLoader _parent;

  public static Class getCached( ICompilableType gsClass ) {
    return CACHE.get( gsClass.getName() );
  }

  public static void clearCache() {
    CACHE.clear();
  }

  SingleServingGosuClassLoader( GosuClassLoader parent )
  {
    super( parent.getActualLoader() );
    _parent = parent;
  }

  public Class<?> findClass( String strName ) throws ClassNotFoundException
  {
    Class cls = CACHE.get( strName.replace( '$', '.' ) );
    if( cls != null ) {
      return cls;
    }
    return _parent.findClass( strName );
  }

  @Override
  protected Object getClassLoadingLock( String className )
  {
    return TypeSystemLock.getMonitor();
  }

//  @Override
//  protected Class<?> loadClass( String name, boolean resolve ) throws ClassNotFoundException {
//    // Acquire the type system lock and this loader's lock in a consistent order to prevent deadlock.
//    // Note this is only important here for the case where the parent loader of this loader
//    // loads a gosu class that is turn needs to load
//
//    TypeSystemLockHelper.getTypeSystemLockWithMonitor( this );
//    try {
//      return super.loadClass( name, resolve );
//    }
//    finally {
//      TypeSystem.unlock();
//    }
//  }

  @Override
  public void dumpAllClasses() {
    // Do nothing here:  it should be taken care of by the main classloader  
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

    if( classBytes == null )
    {
      throw new IllegalStateException( "Could not generate class for " + gsClass.getName() );
    }
    String strPackage = gsClass.getNamespace();
    if( getPackage( strPackage ) == null )
    {
      definePackage( strPackage, null, null, null, null, null, null, null );
    }

    cls = defineClass( GosuClassLoader.getJavaName( gsClass ), classBytes, 0, classBytes.length );
    CACHE.put( gsClass.getName(), cls );
    return cls;
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
