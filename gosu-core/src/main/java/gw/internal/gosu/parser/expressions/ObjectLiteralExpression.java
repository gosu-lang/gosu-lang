/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.expressions.IObjectLiteralExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public final class ObjectLiteralExpression extends Expression implements IObjectLiteralExpression
{
  private Expression[] _args;
  private boolean _bLiteralId;

  public ObjectLiteralExpression( IType intrinsicType )
  {
    setType( intrinsicType );
  }

  public Expression[] getArgs()
  {
    return _args;
  }

  public void setArgs( Expression[] args )
  {
    _args = args;
    setLiteralId( _args[0] instanceof Literal );
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  protected boolean isLiteralId()
  {
    return _bLiteralId;
  }

  protected void setLiteralId( boolean bLiteralId )
  {
    _bLiteralId = bLiteralId;
  }

  @Override
  public String toString()
  {
    String strOut = TypeSystem.getUnqualifiedClassName( getType() ) + "(";

    if( _args != null && _args.length > 0 )
    {
      strOut += " ";
      for( int i = 0; i < _args.length; i++ )
      {
        if( i != 0 )
        {
          strOut += ", ";
        }
        strOut += _args[i].toString();
      }
      strOut += " ";
    }

    return strOut += ")";
  }

}
