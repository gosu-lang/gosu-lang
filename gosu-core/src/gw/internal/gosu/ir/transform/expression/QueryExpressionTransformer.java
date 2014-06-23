/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.QueryExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.statements.IFunctionStatement;
import gw.config.CommonServices;
import gw.lang.reflect.gs.ICompilableType;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 */
public class QueryExpressionTransformer extends EvalBasedTransformer<QueryExpression>
{
  public static final Map<String, QueryExpression> QUERY_EXPRESSIONS = Collections.synchronizedMap( new HashMap<String, QueryExpression>() );
  private static final Class[] PARAM_TYPES = new Class[]{Object.class, Object[].class, IType[].class, IType.class, int.class, int.class, String.class};

  public static IRExpression compile( TopLevelTransformationContext cc, QueryExpression expr )
  {
    QueryExpressionTransformer compiler = new QueryExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private QueryExpressionTransformer( TopLevelTransformationContext cc, QueryExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    String queryExprKey;
    synchronized( QUERY_EXPRESSIONS )
    {
      queryExprKey = EvalExpressionTransformer.makeEvalKey( getGosuClass(),
              _expr().getLineNum(), _expr().getColumn(), _expr().toString() );
      QUERY_EXPRESSIONS.put( queryExprKey, _expr() );
    }

    IFunctionStatement fs = _expr().getLocation().getEnclosingFunctionStatement();
    IRExpression compileAndRunEvalSource = callStaticMethod( QueryExpressionTransformer.class, "compileAndRunQuery",
                                                             PARAM_TYPES,
                                                             exprList(
                                                               pushEnclosingContext(),
                                                               pushCapturedSymbols( getGosuClass(), _expr().getCapturedForBytecode() ),
                                                               pushEnclosingFunctionTypeParamsInArray( _expr() ),
                                                               pushType( getGosuClass() ),
                                                               pushConstant( _expr().getLineNum() ),
                                                               pushConstant( _expr().getColumn() ),
                                                               pushConstant( _expr().toString() )
                                                             ) );
    return checkCast( TypeSystem.getByFullName( "gw.api.database.IQueryBeanResult", TypeSystem.getGlobalModule() ), compileAndRunEvalSource );
  }

  public static Object compileAndRunQuery( Object outer, Object[] capturedValues,
                                           IType[] immediateFuncTypeParams, IType enclosingClass, int iLineNum, int iColumnNum, String exprText )
  {
    String exprKey = EvalExpressionTransformer.makeEvalKey( enclosingClass, iLineNum, iColumnNum, exprText );
    QueryExpression queryExpr = QUERY_EXPRESSIONS.get( exprKey );
    if( queryExpr == null )
    {
      if( enclosingClass instanceof ICompilableType )
      {
        ((ICompilableType)enclosingClass).compile();
        queryExpr = QUERY_EXPRESSIONS.get( exprKey );
      }
    }

    IQueryExpressionEvaluator evaluator = CommonServices.getEntityAccess().getQueryExpressionEvaluator( queryExpr );
    if( evaluator != null )
    {
      return evaluator.evaluate( new Object[] {outer, capturedValues, immediateFuncTypeParams, enclosingClass} );
    }

    throw new EvaluationException( "No query expression evaluator is defined." );
  }

  public static void clearQueryExpressions() {
    QUERY_EXPRESSIONS.clear();
  }
}
