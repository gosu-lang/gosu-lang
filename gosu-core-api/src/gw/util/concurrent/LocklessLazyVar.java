/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util.concurrent;

/**
 * Implements the lazy initialization pattern.
 * No locking of any kind is used.
 */
public abstract class LocklessLazyVar<T>
{
  protected final static Object NULL = new Object();
  private volatile T _val = null;

  /**
   * @return the value of this lazy var, created if necessary
   */
  public final T get()
  {
    T result = _val;
    if(result == NULL) {
      return null;
    }
    if( result == null )
    {
      result = init();
      if (result == null) {
        _val = (T)NULL;
      } else {
        _val = result;
      }
    }
    return result;
  }

  protected abstract T init();

  /**
   * Clears the variable, forcing the next call to {@link #get()} to re-calculate
   * the value.
   */
  public final T clear()
  {
    T hold = _val;
    _val = null;
    return hold;
  }

  protected void initDirectly( T val )
  {
    _val = val;
  }

  public boolean isLoaded() {
    return _val != null;
  }

  /**
   * A simple init interface to make LockingLazyVar's easier to construct
   * from gosu.
   */
  public static interface LazyVarInit<Q> {
    public Q init();
  }

  /**
   * Creates a new LockingLazyVar based on the type of the LazyVarInit passed in.
   * This method is intended to be called with blocks from Gosu.
   * @deprecated Of dubious value. Use your own null check. In some cases in Gosu, this is used to pass a block and an interface would be better in those cases.
   */
  @Deprecated
  public static <Q> LocklessLazyVar<Q> make( final LazyVarInit<Q> init ) {
    return new LocklessLazyVar<Q>(){
      protected Q init()
      {
        return init.init();
      }
    };
  }

}
