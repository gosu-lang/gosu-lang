/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.expressions.IMapAccessExpression;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IType;


public final class MapAccess extends Expression implements IMapAccessExpression
{
  /** The map expression */
  private Expression _rootExpression;

  private boolean _bNullSafe;

  /**
   * An expression for accessing the map
   */
  private Expression _keyExpression;

  private IType _keyType;

  public MapAccess()
  {
  }

  public Expression getRootExpression()
  {
    return _rootExpression;
  }

  public void setRootExpression( Expression rootExpression )
  {
    _rootExpression = rootExpression;
    setTypeInternal( _rootExpression.getType() );
  }

  public Expression getKeyExpression()
  {
    return _keyExpression;
  }

  public void setKeyExpression( Expression keyExpression )
  {
    _keyExpression = keyExpression;
  }

  public boolean isNullSafe()
  {
    return _bNullSafe;
  }
  public void setNullSafe( boolean bNullSafe )
  {
    _bNullSafe = bNullSafe;
  }

  /**
   * Evaluates the expression.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return getRootExpression().toString() + "[" + getKeyExpression().toString() + "]";
  }

  public static boolean supportsMapAccess( IType type )
  {
    return JavaTypes.MAP().isAssignableFrom(type);
  }

  private void setTypeInternal( IType type )
  {
    IType paramedType = TypeLord.findParameterizedTypeInHierarchy(type, JavaTypes.MAP());

    if( paramedType != null )
    {
      IType[] parameters = paramedType.getTypeParameters();
      _keyType = parameters[0];
      setType( parameters[1] );
    }
    else
    {
      _keyType = GosuParserTypes.GENERIC_BEAN_TYPE();
      setType( GosuParserTypes.GENERIC_BEAN_TYPE() );
    }
  }

  public static IType getKeyType( IType type )
  {
    IType paramedType = TypeLord.findParameterizedTypeInHierarchy( type, JavaTypes.MAP() );

    if( paramedType != null )
    {
      IType[] parameters = paramedType.getTypeParameters();
      return parameters[0];
    }
    else
    {
      return GosuParserTypes.GENERIC_BEAN_TYPE();
    }
  }


  public IType getComponentType()
  {
    return getType();
  }

  public IType getKeyType()
  {
    return _keyType;
  }

}