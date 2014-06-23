/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Symbol;
import gw.internal.gosu.parser.CannotExecuteGosuException;


import gw.lang.parser.expressions.IExistsExpression;
import gw.lang.reflect.java.JavaTypes;


/**
 * Represents an 'exists' expression in the Gosu grammar:
 * <pre>
 * <i>exists-expression</i>
 *   <b>exists(</b> &lt;identifier&gt; <b>in</b> &lt;expression&gt; [ <b>index</b> &lt;identifier&gt; ] <b>where</b> &lt;expression&gt; <b>)</b>
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public final class ExistsExpression extends Expression implements IExistsExpression
{
  /**
   * The (declared) Identifier part of the expression.
   */
  private Symbol _identifier;
  /**
   * The (declared) index Identifier part of the expression.
   */
  private Symbol _indexIdentifier;
  /**
   * The source expression evaluating to an Array or Collection to iterate over.
   */
  private Expression _inExpression;
  /**
   * The conditional (test) expression evaluated for each value in the array.
   */
  private Expression _whereExpression;

  private int _iNameOffset;


  public ExistsExpression()
  {
    _type = JavaTypes.pBOOLEAN();
  }

  public Symbol getIdentifier()
  {
    return _identifier;
  }

  public void setIdentifier( Symbol identifier )
  {
    _identifier = identifier;
  }

  public Symbol getIndexIdentifier()
  {
    return _indexIdentifier;
  }

  public void setIndexIdentifier( Symbol indexIdentifier )
  {
    _indexIdentifier = indexIdentifier;
  }

  public Expression getInExpression()
  {
    return _inExpression;
  }

  public void setInExpression( Expression inExpression )
  {
    _inExpression = inExpression;
  }

  public Expression getWhereExpression()
  {
    return _whereExpression;
  }

  public void setWhereExpression( Expression whereExpression )
  {
    _whereExpression = whereExpression;
  }

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
    String strIndex = _indexIdentifier == null ? null : _indexIdentifier.getName();
    if( strIndex != null )
    {
      strIndex = " index " + strIndex;
    }
    else
    {
      strIndex = "";
    }

    return "exists( " + getIdentifier().getName() + " in " + getInExpression().toString() + strIndex + " where " + getWhereExpression().toString() + " )";
  }

  @Override
  public int getNameOffset( String identifierName )
  {
    return _iNameOffset;
  }
  @Override
  public void setNameOffset( int iOffset, String identifierName )
  {
    _iNameOffset = iOffset;
  }

  public boolean declares( String identifierName )
  {
    return identifierName.equals( getIdentifier().getName() );
  }

  public String[] getDeclarations() {
    return new String[] {getIdentifier().getName().toString()};
  }

}
