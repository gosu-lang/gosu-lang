/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.xml.ws;

import gw.internal.xml.ws.server.WebservicesServletBase;
import gw.lang.PublishInGosu;

import java.util.Map;

/**
 * Similar to a java.lang.ThreadLocal, stores data specific to the current WSI request in progress.
 */
@PublishInGosu
public class WsiRequestLocal<T> {

  /**
   * Returns the current value of this request local.
   * @return the current value of this request local
   */
  public T get() {
    @SuppressWarnings( { "unchecked", "MismatchedQueryAndUpdateOfCollection" } )
    Map<WsiRequestLocal, T> map = (Map<WsiRequestLocal, T>) WebservicesServletBase.getRequestLocalMapForCurrentThread();
    //noinspection SynchronizationOnLocalVariableOrMethodParameter
    synchronized ( map ) {
      return map.get( this );
    }
  }

  /**
   * Sets a new value of this request local.
   * @param value the new value
   */
  public void set( T value ) {
    @SuppressWarnings( { "unchecked", "MismatchedQueryAndUpdateOfCollection" } )
    Map<WsiRequestLocal, T> map = (Map<WsiRequestLocal, T>) WebservicesServletBase.getRequestLocalMapForCurrentThread();
    //noinspection SynchronizationOnLocalVariableOrMethodParameter
    synchronized ( map ) {
      map.put( this, value );
    }
  }

}
