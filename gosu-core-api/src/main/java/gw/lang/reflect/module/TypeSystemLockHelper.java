/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.lang.UnstableAPI;
import gw.lang.reflect.TypeSystem;

import java.util.Map;

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
          StringBuilder b = new StringBuilder();
          for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            b.append(entry.getKey().getName()).append('\n');
            for (StackTraceElement stackTraceElement : entry.getValue()) {
              b.append(stackTraceElement).append('\n');
            }
            b.append('\n');
          }
          System.err.print(b.toString());
          throw new RuntimeException( "Deadlock detected while loading classes" );
        }
      }
      catch( InterruptedException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private static void maybeWaitOnContextLoader(Object objectToLock) throws InterruptedException {
    ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
    if( objectToLock != ctxLoader && ctxLoader != null ) {
      try {
        ctxLoader.wait( 100 );
      }
      catch (IllegalMonitorStateException e) {
        // ok, only wait if this thread owns the monitor, otherwise keep rolling
      }
    }
  }
}
