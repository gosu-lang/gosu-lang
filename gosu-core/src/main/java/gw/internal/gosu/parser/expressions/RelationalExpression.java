/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.BeanAccess;
import gw.lang.parser.expressions.IRelationalExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;


/**
 * Represents a relational expression in the Gosu grammar:
 * <pre>
 * <i>relational-expression</i>
 *   &lt;bitshift-expression&gt;
 *   &lt;relational-expression&gt; <b>&lt;</b> &lt;bitshift-expression&gt;
 *   &lt;relational-expression&gt; <b>&gt;</b> &lt;bitshift-expression&gt;
 *   &lt;relational-expression&gt; <b>&lt;=</b> &lt;bitshift-expression&gt;
 *   &lt;relational-expression&gt; <b>&gt;=</b> &lt;bitshift-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class RelationalExpression extends ConditionalExpression implements IRelationalExpression
{
  /**
   * An attribute specifying the type of operation e.g., > >= < <=
   */
  private String _strOperator;


  public String getOperator()
  {
    return _strOperator;
  }

  public void setOperator( String strOperator )
  {
    _strOperator = strOperator;
  }

  /**
   * Perform a relational comparison.
   */
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    Object lhsValue = getLHS().evaluate();
    Object rhsValue = getRHS().evaluate();

    IType lhsType = getLHS().getType();
    IType rhsType = getRHS().getType();
    
    if( _strOperator.equals( ">" ) )
    {
      if( BeanAccess.isNumericType( lhsType ) )
      {
        return compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) > 0;
      }
      else
      {
        if( BeanAccess.isBeanType( lhsType ) )
        {
          if( BeanAccess.isBeanType( rhsType ) )
          {
            if( lhsType.isAssignableFrom( rhsType ) )
            {
              if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
              {
                //noinspection unchecked
                return ((Comparable)lhsValue).compareTo( rhsValue ) > 0;
              }
            }
          }
        }
      }
    }
    else if( _strOperator.equals( "<" ) )
    {
      if( BeanAccess.isNumericType( lhsType ) )
      {
        return compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) < 0;
      }
      else
      {
        if( BeanAccess.isBeanType( lhsType ) )
        {
          if( BeanAccess.isBeanType( rhsType ) )
          {
            if( lhsType.isAssignableFrom( rhsType ) )
            {
              if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
              {
                //noinspection unchecked
                return ((Comparable)lhsValue).compareTo( rhsValue ) < 0;
              }
            }
          }
        }
      }
    }
    else if( _strOperator.equals( ">=" ) )
    {
      if( BeanAccess.isNumericType( lhsType ) )
      {
        return compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) >= 0;
      }
      else
      {
        if( BeanAccess.isBeanType( lhsType ) )
        {
          if( BeanAccess.isBeanType( rhsType ) )
          {
            if( lhsType.isAssignableFrom( rhsType ) )
            {
              if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
              {
                //noinspection unchecked
                return ((Comparable)lhsValue).compareTo( rhsValue ) >= 0;
              }
            }
          }
        }
      }
    }
    else // if( _strOperator.equals( "<=" ) )
    {
      if( BeanAccess.isNumericType( lhsType ) )
      {
        return compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) <= 0;
      }
      else
      {
        if( BeanAccess.isBeanType( lhsType ) )
        {
          if( BeanAccess.isBeanType( rhsType ) )
          {
            if( lhsType.isAssignableFrom( rhsType ) )
            {
              if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
              {
                //noinspection unchecked
                return ((Comparable)lhsValue).compareTo( rhsValue ) <= 0;
              }
            }
          }
        }
      }
    }

    throw new UnsupportedOperationException( "Operands are not compile-time constants.\n" +
                                             "(see http://java.sun.com/docs/books/jls/third_edition/html/expressions.html#5313)" );
  }

  @Override
  public String toString()
  {
    return getLHS().toString() + _strOperator + getRHS().toString();
  }

}
