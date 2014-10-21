/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.MultiplicativeExpression;
import gw.lang.IDimension;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

/**
 */
public class MultiplicativeExpressionTransformer extends ArithmeticExpressionTransformer<MultiplicativeExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, MultiplicativeExpression expr )
  {
    MultiplicativeExpressionTransformer gen = new MultiplicativeExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private MultiplicativeExpressionTransformer( TopLevelTransformationContext cc, MultiplicativeExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression expr = compileNumericArithmetic();
    if( expr != null ) {
      return expr;
    }
    else {
      IType dimensionType = findDimensionType( _expr().getType() );
      if( dimensionType != null ) {
        return dimensionMultiplication( dimensionType );
      }
      return dynamicMultiplication();
    }
  }

  private IRExpression dimensionMultiplication( IType type ) {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    IRSymbol tempLhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
    IRAssignmentStatement tempLhsInitAssn = buildAssignment( tempLhsInit, lhs );
    IRSymbol tempRhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsInitAssn = buildAssignment( tempRhsInit, rhs );
    boolean bLhsDim = JavaTypes.IDIMENSION().isAssignableFrom( _expr().getLHS().getType() );
    if( _expr().isNullSafe() ) {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn,
        buildCast( getDescriptor( _expr().getType() ),
                   buildTernary( buildEquals( identifier( tempLhsInit ), nullLiteral() ),
                      nullLiteral(),
                      buildTernary( buildEquals( identifier( tempRhsInit ), nullLiteral() ),
                                    nullLiteral(),
                                    (type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER())
                                    ? callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( bLhsDim ? tempLhsInit : tempRhsInit ),
                                                  Collections.singletonList( multiplyBigDimension( type, tempLhsInit, tempRhsInit ) ) )
                                    : callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( bLhsDim ? tempLhsInit : tempRhsInit ),
                                                  Collections.singletonList( boxValueToType( type, multiplyBoxedDimension( type, tempLhsInit, tempRhsInit ) ) ) ),
                                    getDescriptor( _expr().getType() ) ),
                      getDescriptor( _expr().getType() ) ) ) );
    }
    else {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn,
                             buildCast( getDescriptor( _expr().getType() ),
                                        (type == JavaTypes.BIG_DECIMAL() || type == JavaTypes.BIG_INTEGER())
                                        ? callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( bLhsDim ? tempLhsInit : tempRhsInit ),
                                                      Collections.singletonList( multiplyBigDimension( type, tempLhsInit, tempRhsInit ) ) )
                                        : callMethod( IDimension.class, "fromNumber", new Class[]{Number.class}, identifier( bLhsDim ? tempLhsInit : tempRhsInit ),
                                                      Collections.singletonList( boxValueToType( type, multiplyBoxedDimension( type, tempLhsInit, tempRhsInit ) ) ) ) ) );
    }
  }

  private IRArithmeticExpression multiplyBoxedDimension( IType type, IRSymbol tempLhsInit, IRSymbol tempRhsInit ) {
    IRExpression lhsExpr;
    IRExpression rhsExpr;
    IType primitiveType = TypeSystem.getPrimitiveType( type );
    if( getDescriptor( IDimension.class ).isAssignableFrom( tempLhsInit.getType() ) ) {
      lhsExpr = unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() ) );
      lhsExpr = numberConvert( lhsExpr.getType(), getDescriptor( primitiveType ), lhsExpr );
      if( _expr().getRHS().getType().isPrimitive() ) {
        rhsExpr = numberConvert( _expr().getRHS().getType(), primitiveType, identifier( tempRhsInit ) );
      }
      else {
        rhsExpr = numberConvert( TypeSystem.getPrimitiveType( _expr().getRHS().getType() ), primitiveType, unboxValueFromType( _expr().getRHS().getType(), identifier( tempRhsInit ) ) );
      }
    }
    else {
      rhsExpr = unboxValueFromType( type, callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() ) );
      rhsExpr = numberConvert( rhsExpr.getType(), getDescriptor( primitiveType ), rhsExpr );
      if( _expr().getLHS().getType().isPrimitive() ) {
        lhsExpr = numberConvert( _expr().getLHS().getType(), primitiveType, identifier( tempLhsInit ) );
      }
      else {
        lhsExpr = numberConvert( TypeSystem.getPrimitiveType( _expr().getLHS().getType() ), primitiveType, unboxValueFromType( _expr().getLHS().getType(), identifier( tempLhsInit ) ) );
      }
    }

    return new IRArithmeticExpression( getDescriptor( primitiveType ), lhsExpr, rhsExpr, IRArithmeticExpression.Operation.fromString( _expr().getOperator() ) );
  }

  private IRExpression multiplyBigDimension( IType type, IRSymbol tempLhsInit, IRSymbol tempRhsInit ) {
    IRExpression lhsExpr;
    IRExpression rhsExpr;
    if( getDescriptor( IDimension.class ).isAssignableFrom( tempLhsInit.getType() ) ) {
      lhsExpr = callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempLhsInit ), Collections.<IRExpression>emptyList() );
      IRSymbol tempRhsConv = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
      rhsExpr = buildComposite( convertOperandToBig( type, type == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, _expr().getRHS().getType(), identifier( tempRhsInit ), tempRhsConv ),
                                identifier( tempRhsConv ) );
    }
    else {
      rhsExpr = callMethod( IDimension.class, "toNumber", new Class[]{}, identifier( tempRhsInit ), Collections.<IRExpression>emptyList() );
      IRSymbol tempLhsConv = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
      lhsExpr = buildComposite( convertOperandToBig( type, type == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class, _expr().getLHS().getType(), identifier( tempRhsInit ), tempLhsConv ),
                                identifier( tempLhsConv ) );
    }

    if( type == JavaTypes.BIG_DECIMAL() ) {
      return bigDecimalArithmetic( checkCast( BigDecimal.class, lhsExpr ), rhsExpr, _expr().getOperator() );
    }
    else {
      return bigIntegerArithmetic( checkCast( BigInteger.class, lhsExpr ), rhsExpr, _expr().getOperator() );
    }
  }

  private IRExpression dynamicMultiplication()
  {
    // Call into Gosu's runtime for the answer.  The arguments are the result type, the boxed LHS, the boxed RHS,
    // the LHS type, the RHS type, whether it's addition, and whether it's numeric
    IRExpression evaluateCall = callStaticMethod( MultiplicativeExpression.class, "evaluate",
                                                  new Class[]{IType.class, Object.class, Object.class, IType.class, IType.class, int.class, boolean.class },
                                                  exprList( pushType( _expr().getType() ),
                                                            boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ),
                                                            boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ),
                                                            pushType( _expr().getLHS().getType() ),
                                                            pushType( _expr().getRHS().getType() ),
                                                            pushConstant( _expr().getOperator().charAt( _expr().getOperator().length()-1 ) ),
                                                            pushConstant( _expr().isNullSafe() ) ) );

    // Ensure value is unboxed if type is primitive
    return unboxValueToType( _expr().getType(), evaluateCall );
  }
}
