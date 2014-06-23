/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IPropertyAsMethodCallIdentifier;


/**
 * For backward compatibility when we exposed both properties and methods on java getters.
 */
public class PropertyAsMethodCallIdentifier extends Identifier implements IPropertyAsMethodCallIdentifier
{
  private String _strMethodName;
  
  public PropertyAsMethodCallIdentifier( String strMethodName )
  {
    _strMethodName = strMethodName;
  }
  
  public String getMethodName()
  {
    return _strMethodName;
  }
}
