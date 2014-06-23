/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.TypeLord;
import gw.lang.reflect.interval.ISequenceable;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.Expression;
import gw.lang.parser.expressions.IIntervalExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;


/**
 * Represents an interval expression in the Gosu grammar:
 * <pre>
 * <i>interval-expression</i>
 *   &lt;bitshift-expression&gt;
 *   &lt;interval-expression&gt; <b>..</b> &lt;bitshift-expression&gt;
 *   &lt;interval-expression&gt; <b>|..</b> &lt;bitshift-expression&gt;
 *   &lt;interval-expression&gt; <b>..|</b> &lt;bitshift-expression&gt;
 *   &lt;interval-expression&gt; <b>|..|</b> &lt;bitshift-expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class IntervalExpression extends BinaryExpression implements IIntervalExpression
{
  private boolean _bLeftClosed;
  private boolean _bRightClosed;

  public IntervalExpression( boolean bLeftClosed, boolean bRightClosed, Expression lhsExpr, Expression rhsExpr )
  {
    _bLeftClosed = bLeftClosed;
    _bRightClosed = bRightClosed;
    setLHS( lhsExpr );
    setRHS( rhsExpr );
    setOperator( (bLeftClosed ? "" : "|") + ".." + (bRightClosed ? "" : "|") );
  }

  public boolean isLeftClosed()
  {
    return _bLeftClosed;
  }

  public boolean isRightClosed()
  {
    return _bRightClosed;
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  public static IType getIntervalType( IType type )
  {
    if( AbstractElementTransformer.isIntType( type ) ||
        AbstractElementTransformer.isBoxedIntType( type ) )
    {
      return JavaTypes.INTEGER_INTERVAL();
    }
    else if( type == JavaTypes.pLONG() ||
             type == JavaTypes.LONG() )
    {
      return JavaTypes.LONG_INTERVAL();
    }
    else if( type == JavaTypes.BIG_INTEGER() )
    {
      return JavaTypes.BIG_INTEGER_INTERVAL();
    }
    else if( type == JavaTypes.pFLOAT() ||
             type == JavaTypes.FLOAT() ||
             type == JavaTypes.pDOUBLE() ||
             type == JavaTypes.DOUBLE() ||
             type == JavaTypes.BIG_DECIMAL() )
    {
      return JavaTypes.BIG_DECIMAL_INTERVAL();
    }
    else if( JavaTypes.DATE().isAssignableFrom( type ) )
    {
      return JavaTypes.DATE_INTERVAL();
    }
    else if( JavaTypes.getGosuType(ISequenceable.class ).isAssignableFrom( type ) )
    {
      if( type instanceof ErrorType )
      {
        return JavaTypes.SEQUENCEABLE_INTERVAL().getParameterizedType( type, JavaTypes.OBJECT(), JavaTypes.OBJECT() );
      }
      IType parameterizedSequenceable = TypeLord.findParameterizedType( type, JavaTypes.getGosuType( ISequenceable.class ) );
      return JavaTypes.SEQUENCEABLE_INTERVAL().getParameterizedType( type, parameterizedSequenceable.getTypeParameters()[1], parameterizedSequenceable.getTypeParameters()[2] );
    }
    else
    {
      if( type instanceof ErrorType )
      {
        return JavaTypes.COMPARABLE_INTERVAL().getParameterizedType( JavaTypes.OBJECT() );
      }
      return JavaTypes.COMPARABLE_INTERVAL().getParameterizedType( type );
    }
  }

}