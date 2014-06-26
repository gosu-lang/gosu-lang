/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.ArithmeticExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRArithmeticExpression;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collections;

/**
 */
abstract class ArithmeticExpressionTransformer<T extends ArithmeticExpression> extends AbstractExpressionTransformer<T> {
  public ArithmeticExpressionTransformer( TopLevelTransformationContext cc, T parsedElem ) {
    super( cc, parsedElem );
  }

  final IRExpression compileNumericArithmetic() {
    IType type = _expr().getType();
    boolean bNumeric = BeanAccess.isNumericType( type );
    IMethodInfo operatorOverload = _expr().getOverride();
    if( bNumeric && operatorOverload != null )
    {
      // The operator is overloaded, call into it...
      IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
      IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
      return callMethod( IRMethodFactory.createIRMethod( operatorOverload, null ), lhs, exprList( rhs ) );
    }
    else
    {
      if( isPrimitiveArithmetic() )
      {
        return primitiveArithmetic();
      }
      else {
        IType lhsType = _expr().getLHS().getType();
        IType rhsType = _expr().getRHS().getType();
        if( isMixedPrimitiveAndBoxedArithmetic( type, lhsType, rhsType ) )
        {
          return mixedPrimitiveAndBoxedArithmetic( type, _expr().getLHS(), _expr().getRHS(), _expr().isNullSafe(), _expr().getOperator() );
        }
        else if( isBigDecimalArithmetic( type, lhsType, rhsType ) ||
                 isBigIntegerArithmetic( type, lhsType, rhsType ) )
        {
          return bigArithmetic();
        }
        else if( isMixedBigDecimalArithmetic( type, lhsType, rhsType ) ||
                 isMixedBigIntegerArithmetic( type, lhsType, rhsType ) )
        {
          return mixedBigArithmetic( type, _expr().getLHS(), _expr().getRHS(), _expr().isNullSafe(), _expr().getOperator() );
        }
      }
    }
    return null;
  }

  final boolean isPrimitiveArithmetic()
  {
    return isPrimitiveNumberType( _expr().getType() ) &&
           isPrimitiveNumberType( _expr().getLHS().getType() ) &&
           isPrimitiveNumberType( _expr().getRHS().getType() );
  }
  final boolean isMixedPrimitiveAndBoxedArithmetic( IType type, IType lhsType, IType rhsType )
  {
    return isNumberType( type ) &&
           isNumberType( lhsType ) &&
           isNumberType( rhsType );
  }
  final boolean isBigDecimalArithmetic( IType type, IType lhsType, IType rhsType )
  {
    return JavaTypes.BIG_DECIMAL().equals( type ) &&
           JavaTypes.BIG_DECIMAL().equals( lhsType ) &&
           JavaTypes.BIG_DECIMAL().equals( rhsType );
  }
  final boolean isBigIntegerArithmetic( IType type, IType lhsType, IType rhsType )
  {
    return JavaTypes.BIG_INTEGER().equals( type ) &&
           JavaTypes.BIG_INTEGER().equals( lhsType ) &&
           JavaTypes.BIG_INTEGER().equals( rhsType );
  }
  final boolean isMixedBigDecimalArithmetic( IType type, IType lhsType, IType rhsType )
  {
    return JavaTypes.BIG_DECIMAL().equals( type ) &&
           (isNumberType( lhsType ) || JavaTypes.BIG_DECIMAL().equals( lhsType )) &&
           (isNumberType( rhsType ) || JavaTypes.BIG_DECIMAL().equals( rhsType ));
  }
  final boolean isMixedBigIntegerArithmetic( IType type, IType lhsType, IType rhsType )
  {
    return JavaTypes.BIG_INTEGER().equals( type ) &&
           (isNumberType( lhsType ) || JavaTypes.BIG_INTEGER().equals( lhsType )) &&
           (isNumberType( rhsType ) || JavaTypes.BIG_INTEGER().equals( rhsType ));
  }

  final IRExpression mixedPrimitiveAndBoxedArithmetic( IType exprType, Expression lhsExpr, Expression rhsExpr, boolean bNullSafe, String strOp ) {
    IType primitiveType = exprType.isPrimitive() ? exprType : TypeSystem.getPrimitiveType( exprType );

    if( bNullSafe ) {
      IRExpression lhs = ExpressionTransformer.compile( lhsExpr, _cc() );
      IRExpression rhs = ExpressionTransformer.compile( rhsExpr, _cc() );
      IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( lhsExpr.getType() ) );
      IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, lhs );
      IRSymbol tempRhs = _cc().makeAndIndexTempSymbol( getDescriptor( rhsExpr.getType() ) );
      IRAssignmentStatement tempRhsAssn = buildAssignment( tempRhs, rhs );

      IRSymbol lhsPrim;
      IRSymbol rhsPrim;

      IRCompositeExpression compExpr = new IRCompositeExpression( tempLhsAssn, tempRhsAssn );

      IRSymbol isNull = _cc().makeAndIndexTempSymbol( getDescriptor( boolean.class ) );
      IRAssignmentStatement isNullAssn = buildAssignment( isNull, booleanLiteral( false ) );
      compExpr.addElement( isNullAssn );

      if( StandardCoercionManager.isBoxed( lhsExpr.getType() ) ) {
        IType lhsPrimType = TypeSystem.getPrimitiveType( lhsExpr.getType() );
        lhsPrim = _cc().makeAndIndexTempSymbol( getDescriptor( primitiveType ) );
        compExpr.addElement( buildAssignment( lhsPrim, convertNullToPrimitive( primitiveType ) ) );
        compExpr.addElement( buildIfElse( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                          buildAssignment( isNull, booleanLiteral( true ) ),
                                          buildAssignment( lhsPrim, numberConvert( lhsPrimType, primitiveType, unboxValueFromType( lhsExpr.getType(), identifier( tempLhs ) ) ) ) ) );
      }
      else {
        lhsPrim = _cc().makeAndIndexTempSymbol( getDescriptor( primitiveType ) );
        compExpr.addElement( buildAssignment( lhsPrim, numberConvert( lhsExpr.getType(), primitiveType, identifier( tempLhs ) ) ) );
      }

      if( StandardCoercionManager.isBoxed( rhsExpr.getType() ) ) {
        IType rhsPrimType = TypeSystem.getPrimitiveType( rhsExpr.getType() );
        rhsPrim = _cc().makeAndIndexTempSymbol( getDescriptor( primitiveType ) );
        compExpr.addElement( buildAssignment( rhsPrim, convertNullToPrimitive( primitiveType ) ) );
        compExpr.addElement( buildIfElse( buildEquals( identifier( tempRhs ), nullLiteral() ),
                                          buildAssignment( isNull, booleanLiteral( true ) ),
                                          buildAssignment( rhsPrim, numberConvert( rhsPrimType, primitiveType, unboxValueFromType( rhsExpr.getType(), identifier( tempRhs ) ) ) ) ) );
      }
      else {
        rhsPrim = _cc().makeAndIndexTempSymbol( getDescriptor( primitiveType ) );
        compExpr.addElement( buildAssignment( rhsPrim, numberConvert( rhsExpr.getType(), primitiveType, identifier( tempRhs ) ) ) );
      }

      IRExpression expr = new IRArithmeticExpression( getDescriptor( primitiveType ), identifier( lhsPrim ), identifier( rhsPrim ), IRArithmeticExpression.Operation.fromString( strOp ) );
      if( StandardCoercionManager.isBoxed( exprType ) ) {
        expr = boxValueToType( exprType, expr );
      }
      expr = buildTernary( buildEquals( identifier( isNull ), booleanLiteral( true ) ),
                           StandardCoercionManager.isBoxed( exprType ) ? nullLiteral() : numberConvert( JavaTypes.pINT(), exprType, pushConstant( 0 ) ),
                           expr, getDescriptor( exprType ) );

      compExpr.addElement( expr );
      return compExpr;
    }
    else {
      IRExpression lhs = ExpressionTransformer.compile( lhsExpr, _cc() );
      if( StandardCoercionManager.isBoxed( lhsExpr.getType() ) ) {
        lhs = unboxValueFromType( lhsExpr.getType(), lhs );
      }
      lhs = numberConvert( lhs.getType(), getDescriptor( primitiveType ), lhs );

      IRExpression rhs = ExpressionTransformer.compile( rhsExpr, _cc() );
      if( StandardCoercionManager.isBoxed( rhsExpr.getType() ) ) {
        rhs = unboxValueFromType( rhsExpr.getType(), rhs );
      }
      rhs = numberConvert( rhs.getType(), getDescriptor( primitiveType ), rhs );

      IRExpression expr = new IRArithmeticExpression( getDescriptor( primitiveType ), lhs, rhs, IRArithmeticExpression.Operation.fromString( strOp ) );
      if( StandardCoercionManager.isBoxed( exprType ) ) {
        expr = boxValueToType( exprType, expr );
      }
      return expr;
    }
  }

  final IRExpression mixedBigArithmetic( IType bigType, Expression lhsExpr, Expression rhsExpr, boolean bNullSafe, String strOp )
  {
    Class bigClass = bigType == JavaTypes.BIG_INTEGER() ? BigInteger.class : BigDecimal.class;
    assert bigType == JavaTypes.BIG_INTEGER() || bigType == JavaTypes.BIG_DECIMAL();
    IRExpression lhs = ExpressionTransformer.compile( lhsExpr, _cc() );
    IRExpression rhs = ExpressionTransformer.compile( rhsExpr, _cc() );

    IRSymbol tempLhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( lhsExpr.getType() ) );
    IRAssignmentStatement tempLhsInitAssn = buildAssignment( tempLhsInit, lhs );
    IRSymbol tempRhsInit = _cc().makeAndIndexTempSymbol( getDescriptor( rhsExpr.getType() ) );
    IRAssignmentStatement tempRhsInitAssn = buildAssignment( tempRhsInit, rhs );

    IRSymbol tempLhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( bigType ) );
    IRStatement tempLhsRetAssn = convertOperandToBig( bigType, bigClass, lhsExpr.getType(), identifier( tempLhsInit ), tempLhsRet );
    IRSymbol tempRhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( bigType ) );
    IRStatement tempRhsRetAssn = convertOperandToBig( bigType, bigClass, rhsExpr.getType(), identifier( tempRhsInit ), tempRhsRet );

    if( bNullSafe ) {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn,
        buildCast( getDescriptor( bigType ),
                   buildTernary( lhs.getType().isPrimitive() ? booleanLiteral( false ) : buildEquals( identifier( tempLhsInit ), nullLiteral() ),
                      nullLiteral(),
                      buildTernary( rhs.getType().isPrimitive() ? booleanLiteral( false ) : buildEquals( identifier( tempRhsInit ), nullLiteral() ),
                                    nullLiteral(),
                                    buildComposite( tempLhsRetAssn, tempRhsRetAssn,
                                                    makeBigExpression( bigClass, identifier( tempLhsRet ), identifier( tempRhsRet ), strOp ) ),
                                    getDescriptor( bigType ) ),
                      getDescriptor( bigType ) ) ) );
    }
    else {
      return buildComposite( tempLhsInitAssn, tempRhsInitAssn, tempLhsRetAssn, tempRhsRetAssn,
                             makeBigExpression( bigClass, identifier( tempLhsRet ), identifier( tempRhsRet ), strOp ) );
    }
  }

  final IRExpression bigArithmetic()
  {
    IType bigType = _expr().getType();
    Class bigClass = bigType == JavaTypes.BIG_INTEGER() ? BigInteger.class : BigDecimal.class;
    assert bigType == JavaTypes.BIG_INTEGER() || bigType == JavaTypes.BIG_DECIMAL();
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
    if( _expr().isNullSafe() ) {
      IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( bigType ) );
      IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, lhs );
      IRSymbol tempRhs = _cc().makeAndIndexTempSymbol( getDescriptor( bigType ) );
      IRAssignmentStatement tempRhsAssn = buildAssignment( tempRhs, rhs );
      return buildComposite( tempLhsAssn, tempRhsAssn,
        buildCast( getDescriptor( bigType ),
                    buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                  nullLiteral(),
                                  buildTernary( buildEquals( identifier( tempRhs ), nullLiteral() ),
                                                nullLiteral(),
                                                makeBigExpression( bigClass, identifier( tempLhs ), identifier( tempRhs ), _expr().getOperator() ),
                                                getDescriptor( bigType ) ),
                                  getDescriptor( bigType ) ) ) );
    }
    else {
      return makeBigExpression( bigClass, lhs, rhs, _expr().getOperator() );
    }
  }

  final IRExpression makeBigExpression( Class bigClass, IRExpression lhs, IRExpression rhs, String strOp ) {
    if( bigClass == BigDecimal.class ) {
      return bigDecimalArithmetic( lhs, rhs, strOp );
    }
    else {
      return bigIntegerArithmetic( lhs, rhs, strOp );
    }
  }

  final IRExpression bigDecimalArithmetic( IRExpression lhs, IRExpression rhs, String strOp )
  {
    if( strOp.equals( "+" ) ||
        strOp.equals( "?+" ) ) {
      return callMethod( BigDecimal.class, "add", new Class[] {BigDecimal.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "-" ) ||
             strOp.equals( "?-" ) ) {
      return callMethod( BigDecimal.class, "subtract", new Class[] {BigDecimal.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "*" ) ||
             strOp.equals( "?*" ) )
    {
      return callMethod( BigDecimal.class, "multiply", new Class[] {BigDecimal.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "/" ) ||
             strOp.equals( "?/" ) )
    {
      return callMethod( BigDecimal.class, "divide",
                         new Class[] {BigDecimal.class, MathContext.class}, lhs,
                         Arrays.asList( rhs, getStaticField( TypeSystem.get( MathContext.class ), "DECIMAL128", JavaClassIRType.get( MathContext.class ), IRelativeTypeInfo.Accessibility.PUBLIC ) ) );
    }
    else if( strOp.equals( "%" ) ||
             strOp.equals( "?%" ) )
    {
      IRExpression remainder = callMethod( BigDecimal.class, "remainder",
                                           new Class[] {BigDecimal.class, MathContext.class}, lhs,
                                           Arrays.asList( rhs, getStaticField( TypeSystem.get( MathContext.class ), "DECIMAL128", JavaClassIRType.get( MathContext.class ), IRelativeTypeInfo.Accessibility.PUBLIC ) ) );
      return callMethod( BigDecimal.class, "abs", new Class[] {}, remainder, Collections.<IRExpression>emptyList() );
    }
    throw new IllegalStateException();
  }

  final IRExpression bigIntegerArithmetic( IRExpression lhs, IRExpression rhs, String strOp )
  {
    if( strOp.equals( "+" ) ||
        strOp.equals( "?+" ) )
    {
      return callMethod( BigInteger.class, "add", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "-" ) ||
             strOp.equals( "?-" ) )
    {
      return callMethod( BigInteger.class, "subtract", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "*" ) ||
             strOp.equals( "?*" ) )
    {
      return callMethod( BigInteger.class, "multiply", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "/" ) ||
             strOp.equals( "?/" ) )
    {
      return callMethod( BigInteger.class, "divide", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( strOp.equals( "%" ) ||
             strOp.equals( "?%" ) )
    {
      return callMethod( BigInteger.class, "mod", new Class[]{BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    throw new IllegalStateException();
  }

  final IRExpression primitiveArithmetic()
  {
    IType type = _expr().getType();

    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    lhs = numberConvert( _expr().getLHS().getType(), type, lhs );

    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
    rhs = numberConvert( _expr().getRHS().getType(), type, rhs );

    return new IRArithmeticExpression( getDescriptor( type ), lhs, rhs, IRArithmeticExpression.Operation.fromString( _expr().getOperator() ) );
  }
}
