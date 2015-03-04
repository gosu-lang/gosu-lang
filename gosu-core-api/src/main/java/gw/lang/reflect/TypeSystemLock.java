package gw.lang.reflect;

import gw.util.GosuUnsafeUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 */
public class TypeSystemLock implements Lock
{
  @Override
  public void lock()
  {
    GosuUnsafeUtil.monitorEnter( getMonitor() );
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
    GosuUnsafeUtil.monitorExit( getMonitor() );
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
