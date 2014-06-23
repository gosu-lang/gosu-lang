package gw.lang.enhancements
uses java.util.concurrent.locks.Lock

/*
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreLockEnhancement : Lock
{

  /**
   * Implements the standard pattern of locking the  lock, running the given block in a try
   * statement and then unlocking the lock in a finally statement.
   */
  function with( b() )
  {
    this.lock()
    try
    {
      b()
    }
    finally
    {
      this.unlock()
    }
  }
  
}
