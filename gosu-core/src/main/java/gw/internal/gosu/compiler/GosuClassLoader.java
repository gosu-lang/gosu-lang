/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.config.CommonServices;
import gw.internal.gosu.ir.TransformingCompiler;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.internal.gosu.parser.ICompilableTypeInternal;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.IGosuProgramInternal;
import gw.internal.gosu.parser.JavaMethodCache;
import gw.internal.gosu.parser.ModuleClassLoader;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.TypeSystemAwareCache;
import gw.lang.reflect.IGosuClassLoadingObserver;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.BytecodeOptions;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.UrlClassLoaderWrapper;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.TypeSystemLockHelper;
import gw.util.GosuExceptionUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GosuClassLoader implements IGosuClassLoader
{
  private DiscreteLoaderCache _discreteLoaders = new DiscreteLoaderCache();
  private ClassLoader _loader;


  //## For tests only
  public static GosuClassLoader instance()
  {
    return (GosuClassLoader)TypeSystem.getGosuClassLoader();
  }

  @Override
  public void dumpAllClasses()
  {
    // We also need to clear out anything added to some static map of random stuff due
    // to the compilation of the now-orphaned classes.  Side-note:  this doesn't need to
    // be an instance method, since it's calling static helpers, but it's an instance
    // method at the moment so it can appear on IGosuClassLoader and make it through
    // the wall and be usable from gosu-core-api
    AbstractElementTransformer.clearCustomRuntimes();
  }

  @Override
  public byte[] getBytes( ICompilableType gsClass )
  {
    try
    {
      return compileClass( gsClass, false );
    }
    catch( Exception pre )
    {
      throw GosuExceptionUtil.forceThrow( new IOException( pre ) );
    }
  }

  public GosuClassLoader( ClassLoader parent )
  {
    assignParent( parent );
    init();
  }

  // Note this really assigns the actual class loader associated with Gosu (now that Gosu loads its classes in the java App loader)
  public void assignParent( ClassLoader parent )
  {
    if( parent instanceof ModuleClassLoader && ((ModuleClassLoader)parent).isDeferToParent() )
    {
      // For a given module, the loader that loads Gosu classes *must* also be the loader that loads Java classes.
      // In the case where we are using the single default module (the normal mode for Gosu's runtime) the module
      // class loader does nothing, it doesn't have a classpath, so we don't want to add our gosuclass protocol to
      // its classpath, instead we want to add it to its parent, which has the classpath for the everything, including
      // the Java classfiled (and potential Gosu classfiles) in the module.
      // The driving force behind doing this is that, if gosu classes are persisted to disk, we want Java to load
      // them as a normal class, but we also need for the loader to be able to resolve Gosu classes that may not
      // have (or could not have) been written to disk. For instance, fragments and dynamic programs are compiled
      // at runtime and therefore can't be precompiled -- they are compiled on demand so the gosuclass protocol,
      // being in the classpath of the app class loader, resolves the name and compile the class and produce the
      // resource/stream associated with the compiled bytes.
      _loader = parent.getParent();
      while( _loader instanceof URLClassLoader )
      {
        if( ((URLClassLoader) _loader).getURLs().length != 0 )
        {
          break;
        }
        if( !(_loader.getParent() instanceof URLClassLoader) &&
            !UrlClassLoaderWrapper.canWrap( _loader.getParent() ) )
        {
          break;
        }
        _loader = _loader.getParent();
      }
    }
    else
    {
      _loader = parent;
    }
  }

  private void init()
  {
    // Keep parse trees
  }

  public ClassLoader getLoader()
  {
    return _loader;
  }

  @Override
  public Class loadClass( String strName ) throws ClassNotFoundException
  {
    String strGsName = strName.replace( '$', '.' );
    //## hack:
    if (strGsName.startsWith("com.guidewire.commons.metadata.proxy._generated.iface.")) {
      strGsName = "entity." + strGsName.substring(strName.lastIndexOf('.') + 1);
    }

    IType type = TypeSystem.getByFullNameIfValid( strGsName );
    if( type instanceof IGosuClassInternal )
    {
      return ((IGosuClassInternal)type).getBackingClass();
    }
    else if( type instanceof IJavaBackedType )
    {
      return ((IJavaBackedType)type).getBackingClass();
    }

    TypeSystemLockHelper.getTypeSystemLockWithMonitor(_loader);
    try
    {
      return _loader.loadClass( strName );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  @Override
  public Class<?> findClass( String strName ) throws ClassNotFoundException {
    return loadClass( strName );
  }

  @Override
  public IJavaType getFunctionClassForArity( boolean hasReturn, int length )
  {
    return FunctionClassUtil.getFunctionClassForArity( hasReturn, length );
  }

  public Class defineClass( ICompilableTypeInternal gsClass, boolean useSingleServingLoader ) throws ClassNotFoundException
  {
    try
    {
      if( gsClass instanceof IGosuClassInternal && ((IGosuClassInternal)gsClass).hasBackingClass() )
      {
        return ((IGosuClassInternal)gsClass).getBackingClass();
      }

      // there is no point in defining eval classes in a single serving class loader (it wastes memory)
      if( useSingleServingLoader || TypeLord.isEvalProgram( gsClass ) || isThrowawayProgram( gsClass ) || isEnclosingTypeInSingleServingLoader( gsClass ) || hasDiscreteNamespace( gsClass.getNamespace() ) )
      {
        // These classes are "fire and forget"; they need to be disposable after they run,
        // so we load them in a separate class loader so we can unload them -- it's the only
        // way to unload a class in java.
        return defineClassInLoader( gsClass, true );
      }

      return findOrDefineClass( gsClass );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e, gsClass.getName() );
    }
  }

  boolean hasDiscreteNamespace( String namespace )
  {
    return _discreteLoaders.getLoader( namespace ) != null;
  }
  DiscreteClassLoader getDiscreteNamespaceLoader( String namespace )
  {
    return _discreteLoaders.getLoader( namespace );
  }
  @SuppressWarnings("UnusedDeclaration")
  public boolean isLoaderUnloaded( String namespace )
  {
    return _discreteLoaders.isLoaderUnloaded( namespace );
  }

  private boolean isEnclosingTypeInSingleServingLoader( ICompilableTypeInternal gsClass )
  {
    ICompilableTypeInternal enclosingType = gsClass.getEnclosingType();
    ClassLoader enclosingLoader = getClassLoader( enclosingType );
    return enclosingLoader instanceof SingleServingGosuClassLoader;
  }

  private Class findOrDefineClass( ICompilableTypeInternal gsClass ) throws ClassNotFoundException
  {
    String strName = gsClass.getJavaName();
    Class cls = null;
    try
    {
      cls = _loader.loadClass( strName );

      if( cls.getClassLoader() instanceof SingleServingGosuClassLoader )
      {
        if( ((SingleServingGosuClassLoader)cls.getClassLoader()).isDisposed() )
        {
          cls = null;
        }
      }
    }
    catch( ClassNotFoundException cnfe )
    {
      TypeSystem.lock();
      try
      {
        cls = defineClassInLoader( gsClass, false );
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return cls;
  }

  private Class defineClassInLoader( ICompilableTypeInternal gsClass, boolean forceSingleServingLoader )
  {
    if( forceSingleServingLoader || shouldUseSingleServingLoader( gsClass ) || BytecodeOptions.isSingleServingLoader() )
    {
      SingleServingGosuClassLoader loader = getOrCreateSingleServingLoader( gsClass );
      return defineClassInSingleServingLoader( gsClass, loader );
    }
    else
    {
      return defineAndMaybeVerify( gsClass );
    }
  }

  private SingleServingGosuClassLoader getOrCreateSingleServingLoader( ICompilableTypeInternal gsClass )
  {
    ICompilableTypeInternal enclosingType = gsClass.getEnclosingType();
    ClassLoader enclosingLoader = isOldStyleGosuAnnotationExpression(gsClass) ? null : getClassLoader( enclosingType );
    if( enclosingLoader instanceof SingleServingGosuClassLoader && !(enclosingLoader instanceof DiscreteClassLoader) )
    {
      return (SingleServingGosuClassLoader)enclosingLoader;
    }
    DiscreteClassLoader namespaceLoader = getDiscreteNamespaceLoader( gsClass.getNamespace() );
    return namespaceLoader == null ? new SingleServingGosuClassLoader( this ) : namespaceLoader;
  }

  private boolean isOldStyleGosuAnnotationExpression(ICompilableTypeInternal gsClass)
  {
    if( !(gsClass instanceof IGosuProgram) )
    {
      return false;
    }

    final IType expectedReturnType = ((IGosuProgram) gsClass).getExpectedReturnType();
    return expectedReturnType != null && JavaTypes.IANNOTATION().isAssignableFrom( expectedReturnType );
  }

  private ClassLoader getClassLoader( ICompilableTypeInternal enclosingType ) {
    if( enclosingType == null ) {
      return null;
    }
    Class cached = SingleServingGosuClassLoader.getCached( enclosingType );
    if( cached != null ) {
      return cached.getClassLoader();
    }
    return enclosingType instanceof IJavaBackedType
           ? ((IJavaBackedType)enclosingType).getBackingClass().getClassLoader()
           : enclosingType instanceof IHasJavaClass
             ? ((IHasJavaClass)enclosingType).getBackingClass().getClassLoader()
             : null;
  }

  private Class<?> defineClassInSingleServingLoader( ICompilableTypeInternal gsClass, SingleServingGosuClassLoader loader ) {
    Class<?> result = loader._defineClass( gsClass );
    // Define all inner classes and blocks, too. Otherwise, they eventually could be loaded through URL handler.
    for (int i = 0; i < gsClass.getBlockCount(); i++) {
      defineClassInSingleServingLoader( (ICompilableTypeInternal)gsClass.getBlock(i), loader );
    }
    if( gsClass.getInnerClasses() != null ) {
      for( IType inner: gsClass.getInnerClasses() ) {
        try {
          defineClassInSingleServingLoader( (ICompilableTypeInternal)inner, loader );
        }
        catch( LinkageError le ) {
          // ignore case when we've already loaded the class
        }
      }
    }
    return result;
  }

  private boolean shouldUseSingleServingLoader(ICompilableTypeInternal gsClass) {
    List<IGosuClassLoadingObserver> observers = CommonServices.getEntityAccess().getGosuClassLoadingObservers();
    if (observers != null) {
      for (IGosuClassLoadingObserver observer : observers) {
        if (observer.shouldUseSingleServingLoader(gsClass)) {
          return true;
        }
      }
    }
    return hasDiscreteNamespace( gsClass.getNamespace() );
  }

  boolean shouldDebugClass( ICompilableType gsClass )
  {
    return BytecodeOptions.shouldDebug( gsClass.getName() );
  }

  private Class defineAndMaybeVerify( ICompilableTypeInternal gsClass )
  {
    try
    {
      GosuClassPathThing.init();

      String strJavaClass = gsClass.getJavaName();
      Class cls = _loader.loadClass( strJavaClass );

      if( BytecodeOptions.aggressivelyVerify() )
      {
        JavaMethodCache.getDeclaredMethods( cls );
        cls.getDeclaredMethods(); //force verification
      }

      return cls;
    }
    catch( ClassFormatError | VerifyError ve )
    {
      if( BytecodeOptions.aggressivelyVerify() )
      {
        compileClass( gsClass, true ); // print the bytecode
      }
      throw ve;
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
  }

  private static byte[] compileClass( ICompilableType type, boolean debug )
  {
    return TransformingCompiler.compileClass( type, debug );
  }

  private boolean isThrowawayProgram( ICompilableType gsClass ) {
    return gsClass instanceof IGosuProgramInternal && ((IGosuProgramInternal) gsClass).isThrowaway();
  }

  @Override
  public ClassLoader getActualLoader() {
    return getLoader();
  }

  @Override
  public Class defineClass( String name, byte[] bytes )
  {
    TypeSystem.lock();
    try
    {
      Method defineClass = ClassLoader.class.getDeclaredMethod( "defineClass", String.class, byte[].class, int.class, int.class );
      defineClass.setAccessible( true );
      return (Class)defineClass.invoke( _loader, name, bytes, 0, bytes.length );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public boolean waitForLoaderToUnload( String packageName, long millisToWait )
  {
    long lStart = System.currentTimeMillis();
    while( !_discreteLoaders.isLoaderUnloaded( packageName ) )
    {
      if( System.currentTimeMillis() - lStart > millisToWait )
      {
        return false;
      }
      System.gc();
      try
      {
        Thread.sleep( 100 );
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }
    return true;
  }

  public void evictLoader( String packageName )
  {
    _discreteLoaders.evictLoader( packageName );
  }

  private class DiscreteLoaderCache
  {
    // Wrap a delegate to hide the TypeSystemAwareCache#get() method
    private DelegateCache _delegate = new DelegateCache();

    public DiscreteClassLoader getLoader( String key )
    {
      if( getDiscretePackages().isEmpty() )
      {
        return null;
      }
      return _delegate.getLoader( key );
    }

    public boolean isLoaderUnloaded( String key )
    {
      if( getDiscretePackages().isEmpty() )
      {
        return true;
      }
      return _delegate.get( key ).get() == null;
    }

    public void evictLoader( String key )
    {
      _delegate.evict( key );
    }

    private List<String> getDiscretePackages()
    {
      String[] discretePackages = ExecutionEnvironment.instance().getDiscretePackages();
      return discretePackages == null ? Collections.emptyList() : Arrays.asList( discretePackages );
    }

    private class DelegateCache extends TypeSystemAwareCache<String, WeakReference<DiscreteClassLoader>>
    {
      public DelegateCache()
      {
        //noinspection Convert2Lambda
        super( "Discrete Loaders", 100,
               new MissHandler<String, WeakReference<DiscreteClassLoader>>()
               {
                  @Override
                  public WeakReference<DiscreteClassLoader> load( String key )
                  {
                    Optional<String> match = getDiscretePackages().stream().filter( key::startsWith ).findFirst();
                    if( !match.isPresent() )
                    {
                      return new WeakReference<>( DiscreteClassLoader.NULL_SENTINAL );
                    }

                    String unloadableNamespace = match.get();
                    if( key.equals( unloadableNamespace ) )
                    {
                      return new WeakReference<>( null );
                    }
                    return _delegate.get( unloadableNamespace );
                  }
                } );
      }

      private DiscreteClassLoader getLoader( String key )
      {
        WeakReference<DiscreteClassLoader> ref = super.get( key );
        if( ref.get() != DiscreteClassLoader.NULL_SENTINAL )
        {
          // Must maintain a ref to the loader, otherwise it can get collected in between assigning it to the weak ref and returning from this method, in theory anyway
          //noinspection MismatchedReadAndWriteOfArray
          DiscreteClassLoader[] loader = new DiscreteClassLoader[1];

          if( ref.get() == null )
          {
            String unloadableNamespace = getDiscretePackages().stream()
              .filter( ( t ) -> {
                if( key.equals( t ) )
                {
                  return true;
                }
                if( !t.endsWith( "." ) )
                {
                  t = t + '.';
                }
                return key.startsWith( t );
              } ).findFirst().get();
            put( unloadableNamespace, ref = new WeakReference<>( loader[0] = new DiscreteClassLoader( unloadableNamespace, GosuClassLoader.this ) ) );
          }
          return ref.get();
        }
        return null;
      }
    }
  }

}

