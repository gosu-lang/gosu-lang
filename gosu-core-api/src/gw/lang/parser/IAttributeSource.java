/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.ITypeLoaderListener;

public interface IAttributeSource extends ITypeLoaderListener
{
  boolean hasAttribute( String strAttr );

  Object getAttribute( String strAttr );

  void setAttribute( String strAttr, Object value );
}
