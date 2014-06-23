/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.expressions.MultiplicativeExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRArithmeticExpression;
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
public class MultiplicativeExpressionTransformer extends AbstractExpressionTransformer<MultiplicativeExpression>
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
    IMethodInfo operatorOverload = _expr().getOverride();
    if( operatorOverload != null )
    {
      // The operator is overloaded, call into it...
      IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
      IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
      return callMethod( IRMethodFactory.createIRMethod( operatorOverload, null ), lhs, exprList( rhs ) );
    }
    else
    {
      if( isSimpleOperation() )
      {
        return simpleOperation( );
      }
      else if( isBigDecimalMultiplication() )
      {
        return bigDecimalMultiplication();
      }
      else if( isBigIntegerMultiplication() )
      {
        return bigIntegerMultiplication();
      }
      else
      {
        return complexOperation( );
      }
    }
  }

  private boolean isBigDecimalMultiplication()
  {
    return !_expr().isNullSafe() &&
           JavaTypes.BIG_DECIMAL().equals( _expr().getType() ) &&
           JavaTypes.BIG_DECIMAL().equals( _expr().getLHS().getType() ) &&
           JavaTypes.BIG_DECIMAL().equals( _expr().getRHS().getType() );
  }
  private IRExpression bigDecimalMultiplication( )
  {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    if( _expr().getOperator().equals( "*" ) )
    {
      return callMethod( BigDecimal.class, "multiply", new Class[] {BigDecimal.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( _expr().getOperator().equals( "/" ) )
    {
      return callMethod( BigDecimal.class, "divide",
        new Class[] {BigDecimal.class, MathContext.class}, lhs,
        Arrays.asList( rhs, getStaticField( TypeSystem.get( MathContext.class ), "DECIMAL128", JavaClassIRType.get( MathContext.class ), IRelativeTypeInfo.Accessibility.PUBLIC ) ) );
    }
    else if( _expr().getOperator().equals( "%" ) )
    {
      IRExpression remainder = callMethod( BigDecimal.class, "remainder",
        new Class[] {BigDecimal.class, MathContext.class}, lhs,
        Arrays.asList( rhs, getStaticField( TypeSystem.get( MathContext.class ), "DECIMAL128", JavaClassIRType.get( MathContext.class ), IRelativeTypeInfo.Accessibility.PUBLIC ) ) );
      return callMethod( BigDecimal.class, "abs", new Class[] {}, remainder, Collections.<IRExpression>emptyList() );
    }
    throw new IllegalStateException();
  }

  private boolean isBigIntegerMultiplication()
  {
    return !_expr().isNullSafe() &&
           JavaTypes.BIG_INTEGER().equals( _expr().getType() ) &&
           JavaTypes.BIG_INTEGER().equals( _expr().getLHS().getType() ) &&
           JavaTypes.BIG_INTEGER().equals( _expr().getRHS().getType() );
  }
  private IRExpression bigIntegerMultiplication( )
  {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    if( _expr().getOperator().equals( "*" ) )
    {
      return callMethod( BigInteger.class, "multiply", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( _expr().getOperator().equals( "/" ) )
    {
      return callMethod( BigInteger.class, "divide", new Class[] {BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    else if( _expr().getOperator().equals( "%" ) )
    {
      return callMethod( BigInteger.class, "mod", new Class[]{BigInteger.class}, lhs, Collections.singletonList( rhs ) );
    }
    throw new IllegalStateException();
  }
  
  private IRExpression simpleOperation( )
  {
    IType type = _expr().getType();

    // Push LHS
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    // Maybe convert to expression type
    lhs = numberConvert( _expr().getLHS().getType(), type, lhs );

    // Push RHS
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
    // Maybe convert to expression type
    rhs = numberConvert( _expr().getRHS().getType(), type, rhs );


    IRArithmeticExpression.Operation op;
    // Do the addition
    if( _expr().getOperator().equals( "*" ) ||
        _expr().getOperator().equals( "?*" ) )
    {
      op = IRArithmeticExpression.Operation.Multiplication;
    }
    else if( _expr().getOperator().equals( "/" ) ||
             _expr().getOperator().equals( "?/" ) )
    {
      op = IRArithmeticExpression.Operation.Division;
    }
    else if( _expr().getOperator().equals( "%" ) ||
             _expr().getOperator().equals( "?%" ) )
    {
      op = IRArithmeticExpression.Operation.Remainder;
    }
    else
    {
      throw new IllegalArgumentException( "Unexpected operator " + _expr().getOperator() );
    }

    return new IRArithmeticExpression( getDescriptor( type ), lhs, rhs, op );
  }

  private IRExpression complexOperation( )
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

  public boolean isSimpleOperation()
  {
    return _expr().getType().isPrimitive() && BeanAccess.isNumericType( _expr().getType() ) &&
           _expr().getLHS().getType().isPrimitive() && BeanAccess.isNumericType( _expr().getLHS().getType() ) &&
           _expr().getRHS().getType().isPrimitive() && BeanAccess.isNumericType( _expr().getRHS().getType() );
  }
}
