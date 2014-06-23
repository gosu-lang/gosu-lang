/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.MapAccess;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.util.Map;
import java.util.AbstractMap;

/**
 */
public class MapAccessTransformer extends AbstractExpressionTransformer<MapAccess>
{
  public static IRExpression compile( TopLevelTransformationContext cc, MapAccess expr )
  {
    MapAccessTransformer compiler = new MapAccessTransformer( cc, expr );
    return compiler.compile();
  }

  private MapAccessTransformer( TopLevelTransformationContext cc, MapAccess expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    // Since we need to short-circuit if the root evaluates to null, we store it in a temp variable
    // and return a ternary expression, so the result looks like:
    // temp = root
    // (temp == null ? null : temp.get(key))

    boolean bStandardGosu = CommonServices.getEntityAccess().getLanguageLevel().isStandard();
    boolean bNullSafe = _expr().isNullSafe() || !bStandardGosu;

    IRExpression root = ExpressionTransformer.compile( _expr().getRootExpression(), _cc() );
    IRSymbol temp = bNullSafe ? _cc().makeAndIndexTempSymbol( root.getType() ) : null;

    IRExpression key = ExpressionTransformer.compile( _expr().getKeyExpression(), _cc() );

    Class clsMap = getMapType();
    IRExpression getCall = callMethod( clsMap, "get",
                                       new Class[]{Object.class},
                                       bNullSafe ? identifier( temp ) : root,
                                       exprList( key ) );
    if( bNullSafe )
    {
      return buildComposite(
              buildAssignment( temp, root),
              buildNullCheckTernary(
                      identifier( temp ),
                      checkCast( _expr().getType(), nullLiteral()),
                      checkCast( _expr().getType(), getCall ) ) );
    }
    else
    {
      return checkCast( _expr().getType(), getCall );
    }
  }

  private Class getMapType()
  {
    Class clsMap;
    if( JavaTypes.getJreType(AbstractMap.class).isAssignableFrom( _expr().getRootExpression().getType() ) )
    {
      // Use class instead of interface i.e., invokevirtual is faster than invokeinterface
      clsMap = AbstractMap.class;
    }
    else
    {
      clsMap = Map.class;
    }
    return clsMap;
  }
}
