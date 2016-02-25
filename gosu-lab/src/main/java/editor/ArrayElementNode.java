/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.lang.reflect.IType;


/**
 *
 */
public class ArrayElementNode extends BeanInfoNode
{
  private String _strValue;

  /**
   *
   */
  public ArrayElementNode( IType type, int iSubscript )
  {
    super( type );
    setDisplayName( "[" + iSubscript + "]" );
  }

  /**
   * @return
   */
  public String getValue()
  {
    return _strValue;
  }

  public void setValue( String strValue )
  {
    _strValue = strValue;
  }
}