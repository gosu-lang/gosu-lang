/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.statements.MapAssignmentStatement;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.reflect.java.JavaTypes;

import java.util.AbstractMap;
import java.util.Map;

/**
 */
public class MapAssignmentStatementTransformer extends AbstractStatementTransformer<MapAssignmentStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, MapAssignmentStatement stmt )
  {
    MapAssignmentStatementTransformer gen = new MapAssignmentStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private MapAssignmentStatementTransformer( TopLevelTransformationContext cc, MapAssignmentStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Push the Map value
    Expression rootExpression = _stmt().getMapAccessExpression().getRootExpression();
    IRExpression originalRoot = ExpressionTransformer.compile( rootExpression, _cc() );

    IRSymbol tempRoot = null;
    IRExpression root;
    if( _stmt().isCompoundStatement() )
    {
      tempRoot = _cc().makeAndIndexTempSymbol( originalRoot.getType() );
      root = identifier( tempRoot );
      ExpressionTransformer.addTempSymbolForCompoundAssignment( rootExpression, tempRoot );
    } else
    {
      root = originalRoot;
    }

    Class clsMap = getMapType();
    IRExpression key = ExpressionTransformer.compile( _stmt().getMapAccessExpression().getKeyExpression(), _cc() );
    IRExpression value = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
    IRExpression putCall = callMethod( clsMap, "put", new Class[]{Object.class, Object.class},
                                      root,
                                      exprList( key, value ) );
    if( _stmt().isCompoundStatement() )
    {
      ExpressionTransformer.clearTempSymbolForCompoundAssignment();
      return new IRStatementList( false, buildAssignment( tempRoot, originalRoot ), buildMethodCall( putCall ) );
    }
    return buildMethodCall( putCall );

  }

  private Class getMapType()
  {
    Class clsMap;
    if( JavaTypes.getJreType(AbstractMap.class).isAssignableFrom( _stmt().getMapAccessExpression().getRootExpression().getType() ) )
    {
      // Use class instead of interface i.e., invokevirtual is faster than invokeinterface
      clsMap = AbstractMap.class;
    }
    else
    {
      clsMap = Map.class;
    }
    return clsMap;
  }}
