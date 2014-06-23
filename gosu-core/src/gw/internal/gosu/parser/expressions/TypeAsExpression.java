/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICoercer;
import gw.lang.parser.Keyword;
import gw.lang.parser.coercers.MetaTypeToClassCoercer;
import gw.lang.parser.expressions.ITypeAsExpression;
import gw.lang.reflect.IType;
import gw.config.CommonServices;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;

import java.util.Date;

/**
 * Represents a typeas expression in the Gosu grammar:
 * <pre>
 * <i>typeas-expression</i>
 *   &lt;conditional-or-expression&gt; <b>typeas</b> &lt;type-literal&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public class TypeAsExpression extends Expression implements ITypeAsExpression
{
  protected Expression _lhs;
  private ICoercer _coercer;

  /**
   * Base constructor sets type to BooleanType.
   */
  public TypeAsExpression()
  {
  }

  /**
   * @return The expression for the left-hand-side operand.
   */
  public Expression getLHS()
  {
    return _lhs;
  }

  /**
   * @param e The expression for the left-hand-side operand.
   */
  public void setLHS( Expression e )
  {
    _lhs = e;
  }

  public boolean isCompileTimeConstant()
  {
    // Coercions tend not to be compile-time constants, only support them on primitive types, which involve only casting not object construction
    return (_coercer == null || getLHS() != null &&
                                ((getLHS().getType().isPrimitive() && getType().isPrimitive()) ||
                                 _coercer instanceof MetaTypeToClassCoercer)) &&
           getLHS().isCompileTimeConstant();
  }

  /**
   * Perform a type cast.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    Object value = getLHS().evaluate();

    IType argType = getType();

    if( value instanceof IType && argType instanceof IJavaType && JavaTypes.CLASS() == TypeLord.getPureGenericType( argType ) )
    {
      // Don't force class loading during compilation
      return value;
    }

    //============================================================================
    // Special Types
    // TODO cgross: remove these special cases (came from ParsedElement#convertValue)
    //              and allow the coercion manager to handle these cases
    //============================================================================
    if( argType == GosuParserTypes.NUMBER_TYPE() )
    {
      return CommonServices.getCoercionManager().makeDoubleFrom( value );
    }
    else if( argType == GosuParserTypes.STRING_TYPE() )
    {
      return CommonServices.getCoercionManager().makeStringFrom( value );
    }
    else if( argType == GosuParserTypes.DATETIME_TYPE() )
    {
      Date date = CommonServices.getCoercionManager().makeDateFrom( value );
      if ( date != null ) {
        return date;
      }
    }

    //============================================================================
    // This is really all we should be doing
    //============================================================================
    if( _coercer != null && (value != null || _coercer.handlesNull()) )
    {
      return _coercer.coerceValue( argType, value );
    }

    return value;
  }

  public void setCoercer( ICoercer coercer )
  {
    _coercer = coercer;
  }

  public ICoercer getCoercer()
  {
    return _coercer;
  }
  
  //------------------------------------------------------------------------------
  @Override
  public String toString()
  {
    return getLHS().toString()
           + " " + Keyword.KW_as + " "
           + getType().getName();
  }

}
