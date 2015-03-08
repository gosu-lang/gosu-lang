package gw.lang.reflect;

import gw.lang.init.GosuInitialization;
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
  private volatile boolean _initialized = false;

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
      GosuUnsafeUtil.monitorEnter( getMonitor() );
      _initialized = true;
    }
  }

  private boolean initializing()
  {
    return !_initialized &&
           (!GosuInitialization.isAnythingInitialized() ||
            !GosuInitialization.instance( TypeSystem.getExecutionEnvironment() ).isInitialized());
  }

  @Override
  public void lockInterruptibly() throws InterruptedException
  {
    lock();
  }

  @Override
  public boolean tryLock()
  {
    return GosuUnsafeUtil.tryMonitorEnter( getMonitor() );
  }

  @Override
  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException
  {
    return tryLock();
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
      GosuUnsafeUtil.monitorExit( getMonitor() );
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
  }
}
