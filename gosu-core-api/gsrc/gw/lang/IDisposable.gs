package gw.lang

interface IDisposable
{
  /**
   * Releases or resets resources owned and/or allocated by the implementing class
   * <p>
   * A type's dispose method should release all the resources that it owns. It should 
   * also release all resources owned by its base types by calling its parent type's 
   * dispose method. The parent type's dispose method should release all resources that 
   * it owns and in turn call its parent type's dispose method, propagating this 
   * pattern through the hierarchy of base types. To help ensure that resources are 
   * always cleaned up appropriately, a dispose method should be callable multiple 
   * times without throwing an exception.
   *
   *  Copyright 2014 Guidewire Software, Inc.
   */
  function dispose()
}
