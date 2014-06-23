/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.reflect.TypeSystem;

public abstract class LazyInit {

  private boolean _initializing = false;
  private boolean _initialized = false;

  protected final void maybeInit() {
    TypeSystem.lock();
    try {
      if ( _initialized ) {
        return;
      }
      if ( _initializing ) {
        throw new IllegalStateException( "Recursive Initialization" );
      }
      _initializing = true;
      try {
        initialize();
        _initialized = true;
      }
      finally {
        _initializing = false;
      }
    }
    finally {
      TypeSystem.unlock();
    }
  }

  protected abstract void initialize();

}
