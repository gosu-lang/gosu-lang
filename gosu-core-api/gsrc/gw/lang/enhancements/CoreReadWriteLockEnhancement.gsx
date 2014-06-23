package gw.lang.enhancements
uses java.util.concurrent.locks.ReadWriteLock

/**
 * Adds block-based convenience methods to all implementers of the ReadWriteLock
 * interface.
 *
 *  Copyright 2014 Guidewire Software, Inc.
 */
enhancement CoreReadWriteLockEnhancement : ReadWriteLock {

  /** 
   * Performs the given block, which should read from whatever datasource
   * is being locked, with the read lock aquired, and returns the given value.
   * So, for example, a HashMap<String> could be read like so:
   *  
   *   var val = _readWriteLock.read( \-> _hash[key] )
   *
   */
  function read<T>( b():T ) : T
  {
    var returnVal : T
    var readLock = this.readLock()
    readLock.lock()
    try
    {
      returnVal = b()
    }
    finally
    {
      readLock.unlock()
    }
    return returnVal
  }
  
  /** 
   * Performs the given block, which should write to whatever datasource
   * is being locked, with the write lock aquired
   * So, for example, a HashMap<String> could be written to like so:
   *  
   *   _readWriteLock.write( \-> { _hash[key] = value } )
   *
   * Note that since the block body is a statement, not an expression, it must
   * be put in {}'s 
   */
  function write( b():void )
  {
    var writeLock = this.writeLock()
    writeLock.with( b )
  }
  
}