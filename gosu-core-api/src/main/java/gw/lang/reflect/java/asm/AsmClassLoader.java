/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.fs.IFile;
import gw.util.cache.FqnCache;
import manifold.util.ReflectUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 */
public class AsmClassLoader {
  /**
   * EXPERIMENTAL: If true, read class file and process with ASM asynchronously via ThreadPoolExecutor.
   * This is a CPU perf optimization.
   */
  public static boolean ASYNC_ASM_PROCESSING = true;

  private Object _module;
  private FqnCache<AsmClass> _cache;
  private static final ThreadPoolExecutor _taskQueue = makeTaskQueue();

  public AsmClassLoader( Object module ) {
    _module = module;
    _cache = new FqnCache<>();
  }

  private static ThreadPoolExecutor makeTaskQueue()
  {
    int cores = Runtime.getRuntime().availableProcessors() - 1;
    int threads = Math.max( cores, 1 );
    return (ThreadPoolExecutor)Executors.newFixedThreadPool( threads, new TokenizerThreadFactory() );
  }

  private static class TokenizerThreadFactory implements ThreadFactory
  {
    private final AtomicInteger _threadNumber;

    private TokenizerThreadFactory()
    {
      _threadNumber = new AtomicInteger( 1 );
    }

    @Override
    public Thread newThread( Runnable workRunner )
    {
      Thread thread = new Thread( Thread.currentThread().getThreadGroup(), workRunner,
        "Gosu ASM processor " + "(thread: " + _threadNumber.getAndIncrement(), 0 );
      if( !thread.isDaemon() )
      {
        thread.setDaemon( true );
      }
      if( thread.getPriority() != Thread.NORM_PRIORITY )
      {
        thread.setPriority( Thread.NORM_PRIORITY );
      }
      return thread;
    }
  }

//  private void shutdownTaskQueue( ICompilerDriver driver )
//  {
//    _taskQueue.shutdownNow();
//    boolean terminated = false;
//    try
//    {
//      terminated = _taskQueue.awaitTermination( 60, TimeUnit.SECONDS );
//    }
//    catch( InterruptedException e )
//    {
//      throw ManExceptionUtil.unchecked( e );
//    }
//    if( !terminated )
//    {
//      throw new RuntimeException( "Failed to properly terminate ASM processing queue." );
//    }
//  }

  public AsmClass findClass( String fqn, IFile file ) {
    AsmClass asmClass = _cache.get( fqn );
    if( asmClass == null ) {
      asmClass = _cache.get( fqn );
      if( asmClass == null ) {
        asmClass = new AsmClass( fqn, _module, file.toURI(), file );
        _cache.add( fqn, asmClass );

        if( ASYNC_ASM_PROCESSING ) {
          AsmClass finalAsmClass = asmClass;
          _taskQueue.submit( () -> ReflectUtil.method( finalAsmClass, "init" ).invoke() );
        }
      }
    }
    return asmClass;
  }

  public AsmClass findClass( String fqn, File file ) {
    AsmClass asmClass = _cache.get( fqn );
    if( asmClass == null ) {
      asmClass = _cache.get( fqn );
      if( asmClass == null ) {
        asmClass = new AsmClass( fqn, _module, file.toURI(), file );
        _cache.add( fqn, asmClass );

        if( ASYNC_ASM_PROCESSING ) {
          AsmClass finalAsmClass = asmClass;
          _taskQueue.submit( () -> ReflectUtil.method( finalAsmClass, "init" ).invoke() );
        }
      }
    }
    return asmClass;
  }

  public static class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
    public ExposedByteArrayOutputStream() {
      super( 1024 );
    }

    public byte[] getByteArray() {
      return buf;
    }
  }

}
