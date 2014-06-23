/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.ObjectLiteralExpression;
import gw.lang.ir.IRExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.parser.HashedObjectLiteral;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ObjectLiteralExpressionTransformer extends AbstractExpressionTransformer<ObjectLiteralExpression>
{
  public static final List<ObjectLiteralExpression> QUERY_EXPRESSIONS = Collections.synchronizedList( new ArrayList<ObjectLiteralExpression>() );

  public static IRExpression compile( TopLevelTransformationContext cc, ObjectLiteralExpression expr )
  {
    ObjectLiteralExpressionTransformer compiler = new ObjectLiteralExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private ObjectLiteralExpressionTransformer( TopLevelTransformationContext cc, ObjectLiteralExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression typeArg = pushType( _expr().getType() );
    IRExpression idValueArg = ExpressionTransformer.compile( _expr().getArgs()[0], _cc() );
    idValueArg = boxValue( _expr().getArgs()[0].getType(), idValueArg );
    return checkCast( _expr().getType(), callStaticMethod( ObjectLiteralExpressionTransformer.class, "getObjectFromTypeAndId", new Class[]{IType.class, Object.class},
                                                           exprList( typeArg, idValueArg ) ) );
  }

  public static Object getObjectFromTypeAndId( IType type, Object idValue )
  {
    String strLocalOrPublicId = CommonServices.getCoercionManager().makeStringFrom( idValue );
    long lId = 0;
    try
    {
      lId = Long.parseLong( strLocalOrPublicId );
    }
    catch( NumberFormatException nfe )
    {
      try
      {
        lId = CommonServices.getEntityAccess().getHashedEntityId( strLocalOrPublicId, type );
      }
      catch( Exception e )
      {
        throw new RuntimeException( "No entity with id, " + strLocalOrPublicId + ", found for ObjectLiteral of type, " +
                                    TypeSystem.getUnqualifiedClassName( type ) );
      }
    }

    HashedObjectLiteral objectLiteral = new HashedObjectLiteral( type, lId );
    return CommonServices.getCoercionManager().convertValue( objectLiteral, objectLiteral.getAssignableClass() );
  }
}
