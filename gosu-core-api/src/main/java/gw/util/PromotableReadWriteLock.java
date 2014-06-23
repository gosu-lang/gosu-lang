/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class PromotableReadWriteLock implements Lock
{
  private ReentrantReadWriteLock _rwlock;
  private ThreadLocal<State> _statePerThread;

  public PromotableReadWriteLock()
  {
    _rwlock = new ReentrantReadWriteLock( false );
    _statePerThread = new ThreadLocal<State>();
  }

  public void lockRead()
  {
    _rwlock.readLock().lock();
    State state = getState();
    if( !state.isWriteLocked() )
    {
      state.incReadCount();
    }
  }
  public void unlockRead()
  {
    _rwlock.readLock().unlock();
    State state = getState();
    if( !state.isWriteLocked() )
    {
      state.decReadCount();
    }
  }

  public void lockWrite()
  {
    releaseReadLocks();
    _rwlock.writeLock().lock();
    getState().incWriteCount();
  }

  public void unlockWrite()
  {
    reacquireReadLocks();
    _rwlock.writeLock().unlock();
    getState().decWriteCount();
  }

  private State getState()
  {
    State state = _statePerThread.get();
    if( state == null )
    {
      _statePerThread.set( state = new State() );
    }
    return state;
  }

  protected void releaseReadLocks()
  {
    State state = getState();
    if( state.isWriteLocked() )
    {
      return;
    }

    int iCount = state.getReadCount();
    for( int i = 0; i < iCount; i++ )
    {
      _rwlock.readLock().unlock();
    }
  }

  protected void reacquireReadLocks()
  {
    State state = getState();
    if( state.isWriteLockedMoreThanOnce() )
    {
      return;
    }

    int iCount = state.getReadCount();
    for( int i = 0; i < iCount; i++ )
    {
      _rwlock.readLock().lock();
    }
  }

  static class State
  {
    private int _iReadCount;
    private int _iWriteCount;

    public int getReadCount()
    {
      return _iReadCount;
    }
    public void incReadCount()
    {
      _iReadCount++;
    }
    public void decReadCount()
    {
      _iReadCount--;
      if( _iReadCount < 0 )
      {
        throw new IllegalStateException();
      }
    }

    public boolean isWriteLocked()
    {
      return _iWriteCount > 0;
    }
    public boolean isWriteLockedMoreThanOnce()
    {
      return _iWriteCount > 1;
    }
    public void incWriteCount()
    {
      _iWriteCount++;
    }
    public void decWriteCount()
    {
      _iWriteCount--;
    }
  }

  //
  // Lock impl (albeit hastily impled)
  //

  public void lock()
  {
    lockWrite();
  }
  public void unlock()
  {
    unlockWrite();
  }

  public void lockInterruptibly() throws InterruptedException
  {
    throw new UnsupportedOperationException();
  }

  public boolean tryLock()
  {
    releaseReadLocks();
    if( _rwlock.writeLock().tryLock() )
    {
      getState().incWriteCount();
      return true;
    }
    else
    {
      reacquireReadLocks();
      return false;
    }
  }

  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException
  {
    releaseReadLocks();
    if( _rwlock.writeLock().tryLock( time, unit ) )
    {
      getState().incWriteCount();
      return true;
    }
    else
    {
      reacquireReadLocks();
      return false;
    }
  }

  public Condition newCondition()
  {
    throw new UnsupportedOperationException();
  }
}