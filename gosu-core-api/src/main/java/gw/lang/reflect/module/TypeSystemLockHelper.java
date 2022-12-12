/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.config.CommonServices;
import gw.lang.UnstableAPI;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.TypeSystem;
import gw.util.ILogger;
import manifold.util.ReflectUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@UnstableAPI
public class TypeSystemLockHelper {

  /**
   * We avoid doing deadlock detection with class loading if we're running in
   * Studio primarily because the TIDB can grab the type system lock and hold
   * it for a long time during TIDB initialization.
   */
  private static Boolean _bStudioRunning = null;
  private static boolean isStudioRunning()
  {
    if( _bStudioRunning == null )
    {
      _bStudioRunning = System.getProperty( "gw.studio.running" ) != null;
    }
    return _bStudioRunning;
  }

  public static void getTypeSystemLockWithMonitor(Object objectToLock)
  {
    long lStart = System.currentTimeMillis();
    while( !TypeSystem.getGlobalLock().tryLock() )
    {
      try
      {
        //## hack:
        // This prevents deadlock ie., the call to findClass() could come back around and
        // try to grab the type sys lock. Not much we can do about this since this method
        // is called by private method ClassLoader.loadClassInternal(), which is synchronized!

        // Release this class loader's monitor, let some other thread have it (if that's the case),
        // and try again to acquire the type sys lock. The idea is to prevent deadlock by ensuring
        // we can acquire both locks or none at all... albeit expensively.
        try {
          maybeWaitOnContextLoader( objectToLock );
          objectToLock.wait(100);
        } catch (IllegalMonitorStateException e) {
          // Ugh! It turns out to be non-deterministic whether or not the VM will invoke this loop with the classloader's
          // monitor acquired.  However, there can still be deadlocks due to other locks (not the monitor, but VM-level
          // locks around loading specific class names), so we have to just sleep and try again, even though it's
          // inefficient
          Thread.sleep(100);
        }
        if( !isStudioRunning() && System.currentTimeMillis() - lStart > 1000000 )  // wait pretty long (1000 secs as opposed to 10 secs) to avoid a false positive deadlock detection
        {
          dumpAllStackTraces( objectToLock );
        }
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private static void dumpAllStackTraces( Object objectToLock )
  {
    StringBuilder b = new StringBuilder();
    for( Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet() ) {
      Thread thread = entry.getKey();
      b.append( thread.getName() ).append( '\n' );
      if( isTypeSystemLockOwner( thread ) ) {
        b.append( "!!! OWNS TYPE SYSTEM LOCK !!!\n" );
      }
      if( objectToLock != null && isMonitorOwner( thread, objectToLock ) ) {
        b.append( "!!! OWNS MONITOR: " ).append( objectToLock ).append( "!!!\n" );
        b.append( "!!! Alternate lock strategy: " ).append( GosuInitialization._enableAlternateLockingStrategy ? "Enabled" : "Disabled" ).append( " !!!\n" );
      }
      for( StackTraceElement stackTraceElement : entry.getValue() ) {
        b.append( stackTraceElement ).append( '\n' );
      }
      b.append( '\n' );
    }
    System.err.print( b );
    throw new RuntimeException( "Deadlock detected while loading classes" );
  }

  private static boolean isTypeSystemLockOwner( Thread thread )
  {
    ReentrantLock lock = (ReentrantLock)TypeSystem.getGlobalLock();
    try {
      return ReflectUtil.method( lock, "getOwner" ).invoke() == thread;
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public static boolean isMonitorOwner( Thread thread, Object monitor ) {
    if( thread == Thread.currentThread() && Thread.holdsLock( monitor ) ) {
      return true;
    }
    ThreadInfo ti = ManagementFactory.getThreadMXBean().getThreadInfo( new long[]{thread.getId()}, true, false )[0];
    for( java.lang.management.MonitorInfo mi : ti.getLockedMonitors() ) {
      if( mi.getIdentityHashCode() == System.identityHashCode( monitor ) ) {
        return true;
      }
    }
    return false;
  }

  private static void maybeWaitOnContextLoader(Object objectToLock) throws InterruptedException {
    ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
    if( objectToLock != ctxLoader && ctxLoader != null ) {
      if(!GosuInitialization._enableAlternateLockingStrategy) {
        try {
          ctxLoader.wait(100);
        } catch (IllegalMonitorStateException e) {
          // ok, only wait if this thread owns the monitor, otherwise keep rolling
        }
      } else {
        ILogger logger = CommonServices.getEntityAccess().getLogger();
        if(logger.isDebugEnabled() && isMonitorOwner(Thread.currentThread(), ctxLoader)) {
          logger.debug("Holds context monitor, but not waiting on context loader " +
                  ctxLoader.getClass().getSimpleName() + ctxLoader.hashCode());
        }
      }
    }
  }
}
