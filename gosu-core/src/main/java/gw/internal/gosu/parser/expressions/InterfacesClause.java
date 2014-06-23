/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IInterfacesClause;
import gw.lang.reflect.IType;

public class InterfacesClause extends Expression implements IInterfacesClause
{
  private IType _subType;
  private IType[] _interfaces;

  public InterfacesClause( IType subType, IType[] interfaces )
  {
    _subType = subType;
    _interfaces = interfaces;
  }

  public IType[] getInterfaces()
  {
    return _interfaces;
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(
      (_subType.isInterface()
       ? Keyword.KW_extends
       : Keyword.KW_implements) + " " );
    boolean bFirst = true;
    for( IType iface : _interfaces )
    {
      if( !bFirst )
      {
        sb.append( ", " );
      }
      bFirst = false;
      sb.append( iface.getName() );
    }
    return sb.toString();
  }

}