/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.PublishInGosu;

@PublishInGosu
public interface IPropertyAccessor
{
  /**
   * Returns the value for a property given a "this" object
   * @param ctx the "this" pointer.
   * @return the value of the property for the <i>ctx</i> object
   */
  public Object getValue( Object ctx );

  /**
   * Sets the property to the given value for the <i>ctx</i> object
   * @param ctx the "this" pointer
   * @param value the new value
   */
  public void setValue( Object ctx, Object value );

}
