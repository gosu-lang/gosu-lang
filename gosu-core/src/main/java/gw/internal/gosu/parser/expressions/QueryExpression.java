/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.config.CommonServices;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.ir.transform.expression.EvalExpressionTransformer;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IQueryExpression;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.expressions.IQueryPartAssembler;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;

import java.util.List;
import java.util.Map;
import java.util.Iterator;


/**
 * Represents a 'query' expression in the Gosu grammar:
 * <pre>
 * <i>query-expression</i>
 *   <b>find</b> <b>(</b> &lt;identifier&gt; <b>in</b> &lt;query-path-expression&gt; <b>where</b> &lt;where-clause-expression&gt; <b>)</b>
 * </pre>
 * <p/>
 *
 * @see gw.internal.gosu.parser.QueryExpressionParser
 * @see gw.lang.parser.IGosuParser
 *
 * @deprecated
 */
public class QueryExpression extends Expression implements IQueryExpression
{
  /**
   * The type of objects this query expression returns
   */
  private IType _entityType;

  /**
   * The (declared) Identifier part of the expression.
   */
  protected String _strIdentifier;
  /**
   * The query path specifying the context for the query.
   */
  private QueryPathExpression _qpe;
  /**
   * The conditional (test) expression specifying criteria for the query.
   */
  private Expression _whereExpression;
  private boolean _distinct = false;
  private int _iNameOffset;
  private List<ICapturedSymbol> _capturedForBytecode;
  private Map<String, ITypeVariableDefinition> _capturedTypeVars;

  public QueryExpression()
  {
  }

  @Override
  public IType getTypeImpl()
  {
    if( _type == null )
    {
      if (CommonServices.getEntityAccess().isEntityClass(_entityType))
      {
        _type = TypeSystem.getByFullName("gw.api.database.IQueryBeanResult").getParameterizedType(_entityType);
      }
      else
      {
        _type = TypeSystem.getErrorType();
      }
    }
    return _type;
  }

  public IType getEntityType()
  {
    return _entityType;
  }

  public void setEntityType( IType entityType )
  {
    _entityType = entityType;
  }

  public String getIdentifier()
  {
    return _strIdentifier;
  }

  public void setIdentifier( String strIdentifier )
  {
    _strIdentifier = strIdentifier;
  }

  public QueryPathExpression getInExpression()
  {
    return _qpe;
  }

  public void setInExpression( QueryPathExpression qpe )
  {
    _qpe = qpe;
  }

  public Expression getWhereClauseExpression()
  {
    return _whereExpression;
  }

  public void setWhereClauseExpression( Expression whereExpression )
  {
    _whereExpression = whereExpression;
  }

  public void setCapturedSymbolsForBytecode( List<ICapturedSymbol> captured )
  {
    _capturedForBytecode = captured;
  }
  public List<ICapturedSymbol> getCapturedForBytecode()
  {
    return _capturedForBytecode;
  }

  public void setCapturedTypeVars( Map<String, ITypeVariableDefinition> typeVariables )
  {
    for( Iterator<ITypeVariableDefinition> iter = typeVariables.values().iterator(); iter.hasNext(); )
    {
      ITypeVariableDefinition tvd = iter.next();
      if( !(tvd.getEnclosingType() instanceof IFunctionType) )
      {
        iter.remove();
      }
    }
    _capturedTypeVars = typeVariables;
  }
  public Map<String, ITypeVariableDefinition> getCapturedTypeVars()
  {
    return _capturedTypeVars;
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  public void buildPrimaryQuery( IQueryExpressionEvaluator evaluator )
  {
    if( _whereExpression != null )
    {
      ((IQueryPartAssembler)_whereExpression).assembleQueryPart( evaluator );
    }
  }

  @Override
  public String toString()
  {
    return "find( " + getIdentifier() + " in " + getInExpression().toString() +
           (_whereExpression == null ? " )" : (" where " + _whereExpression.toString()) + " )" );
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
    return identifierName.equals( getIdentifier() ) ||
           identifierName.equals( getInExpression().getRootName() );
  }

  public String[] getDeclarations() {
    return new String[] {getIdentifier()};
  }

  public void setDistinct( boolean distinct )
  {
    _distinct = distinct;
  }

  public boolean isDistinct()
  {
    return _distinct;
  }

  @Override
  public Object evalRhsExpr( IExpression rhs, Object[] ctxArgs )
  {
    while( rhs instanceof TypeAsExpression )
    {
      rhs = ((TypeAsExpression)rhs).getLHS();
    }
    if( rhs instanceof EvalExpression )
    {
      return EvalExpressionTransformer.compileAndRunEvalSource( ((StringLiteral)((EvalExpression)rhs).getExpression()).getValue(), ctxArgs[0], (Object[])ctxArgs[1],
                                                                (IType[])ctxArgs[2], (IType)ctxArgs[3], (EvalExpression)rhs );
    }
    else if( rhs instanceof ILiteralExpression )
    {
      return rhs.evaluate();
    }
    else
    {
      throw new IllegalStateException( "Cannot evalute RHS in query of type " + rhs.getClass() + " with content " + rhs.toString() );
    }
  }

  @Override
  public boolean shouldClearParseInfo() {
    return false;
  }
}
