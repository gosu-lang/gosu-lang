package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.init.GosuInitialization;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.util.GosuUnsafeUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
public class TypeSystemLock implements Lock
{
  private Lock _interimLock = new ReentrantLock();
  private ThreadLocal<Integer> _interimCount = new ThreadLocal<>();

  @Override
  public void lock()
  {
    if( initializing() )
    {
      _interimLock.lock();
      Integer count = _interimCount.get();
      if( count == null )
      {
        count = 0;
      }
      _interimCount.set( ++count );
    }
    else
    {
      enterLoaderChainMonitors();
    }
  }

  private boolean initializing()
  {
    return CommonServices.getPlatformHelper().isInIDE() ||
           !GosuInitialization.isAnythingInitialized() ||
           !GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).isInitialized();
  }

  @Override
  public void lockInterruptibly() throws InterruptedException
  {
    lock();
  }

  @Override
  public boolean tryLock()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void unlock()
  {
    Integer count = _interimCount.get();
    if( count != null && count != 0 )
    {
      _interimCount.set( --count );
      _interimLock.unlock();
    }
    else
    {
      exitLoaderChainMonitors();
    }
  }

  @Override
  public Condition newCondition()
  {
    throw new UnsupportedOperationException();
  }

  public static ClassLoader getMonitor()
  {
    return ClassLoader.getSystemClassLoader();
//    return
//      GosuClassPathThing.canWrapChain()
//      ? ClassLoader.getSystemClassLoader()
//      : TypeSystem.getGosuClassLoader().getActualLoader();
  }

  private static void enterLoaderChainMonitors()
  {
    //_enterLoaderChainMonitors( TypeSystem.getGosuClassLoader().getActualLoader() );
    _enterLoaderChainMonitors( getMonitor() );
  }
  private static void _enterLoaderChainMonitors( ClassLoader cl )
  {
    GosuUnsafeUtil.monitorEnter( cl );
//    if( cl != ClassLoader.getSystemClassLoader() && GosuClassPathThing.canWrapChain() )
//    {
//      _enterLoaderChainMonitors( cl.getParent() );
//    }
  }

  private static void exitLoaderChainMonitors()
  {
    _exitLoaderChainMonitors( getMonitor() );
  }
  private static void _exitLoaderChainMonitors( ClassLoader cl )
  {
//    if( cl != ClassLoader.getSystemClassLoader() && GosuClassPathThing.canWrapChain() )
//    {
//      _exitLoaderChainMonitors( cl.getParent() );
//    }
    GosuUnsafeUtil.monitorExit( cl );
  }
}
