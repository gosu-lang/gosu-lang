/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRTypeFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ParserBase;
import gw.internal.gosu.parser.expressions.ConditionalExpression;
import gw.internal.gosu.parser.expressions.EqualityExpression;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 */
public class EqualityExpressionTransformer extends AbstractExpressionTransformer<EqualityExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, EqualityExpression expr )
  {
    EqualityExpressionTransformer gen = new EqualityExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private EqualityExpressionTransformer( TopLevelTransformationContext cc, EqualityExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();
    if( _expr().getLHS() instanceof NullExpression ||
        _expr().getRHS() instanceof NullExpression )
    {
      return compareToNull();
    }
    else if( lhsType.isPrimitive() && rhsType.isPrimitive() )
    {
      return comparePrimitives();
    }
    else if( lhsType == rhsType )
    {
      if( lhsType.isArray() )
      {
        return compareArrays();
      }

      if( (JavaTypes.NUMBER().isAssignableFrom( lhsType ) ||
           JavaTypes.IDIMENSION().isAssignableFrom( lhsType )) &&
          JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
      {
        // Standard Number types in Java implement Comparable to handle ==, <, > etc.
        return compareWithCompareTo();
      }
      return compareWithEquals();
    }
    //## todo: maybe do a quick identity compare, if same we can avoid calling into gs runtime
//    else if( !lhsType.isPrimitive() && !rhsType.isPrimitive() )
//    {
//
//    }
    else
    {
      return compareDynamically();
    }
  }

  private IRExpression compareArrays() {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();

    if( isBytecodeType( lhsType ) )
    {
      IRType arrayType;
      if( lhsType.getComponentType().isPrimitive() )
      {
        arrayType = IRTypeFactory.get(lhsType);
      }
      else
      {
        arrayType = JavaClassIRType.get( Object.class ).getArrayType();
      }
      return buildMethodCall(JavaClassIRType.get( Arrays.class ), "equals", false, JavaClassIRType.get( boolean.class ), Arrays.asList( arrayType, arrayType ), null, Arrays.asList( lhs, rhs ) );
    }
    return compareDynamically();
  }

  private IRExpression comparePrimitives()
  {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    // Get the upper bound type
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();

    // If neither side is a boolean, then find the upper-bound of their types
    // for a number conversion
    if (lhsType != JavaTypes.pBOOLEAN() && rhsType != JavaTypes.pBOOLEAN()) {
      IType type = ParserBase.resolveType( lhsType, '>', rhsType );
      lhs = numberConvert( _expr().getLHS().getType(), type, lhs );
      rhs = numberConvert( _expr().getRHS().getType(), type, rhs );
    }

    return new IREqualityExpression( lhs, rhs, _expr().isEquals() );
  }

  private IRExpression compareToNull()
  {

    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    if ( !( _expr().getLHS() instanceof NullExpression ) ) {
      lhs = boxValue( _expr().getLHS().getType(), lhs );
    }
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );
    if ( !( _expr().getRHS() instanceof NullExpression ) ) {
      rhs = boxValue( _expr().getRHS().getType(), rhs );
    }
    return new IREqualityExpression( lhs, rhs, _expr().isEquals() );
  }

  private IRExpression compareWithEquals()
  {
    // Generates following code:
    // Object lhs = <lhs-expr>
    // Object rhs = <rhs-expr>
    // [!](lhs == rhs || (lhs != null && rhs != null && lhs.equals( rhs )))

    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.OBJECT() ) );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.OBJECT() ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );
    IRExpression callEquals = callMethod( Object.class, "equals", new Class[]{Object.class},
                                          identifier( lhsTemp ),
                                          Collections.singletonList( (IRExpression)identifier( rhsTemp ) ) );
    IRExpression theExpr = new IRConditionalOrExpression( buildEquals( identifier( lhsTemp ), identifier( rhsTemp ) ),
                                                          new IRConditionalAndExpression( buildNotEquals( identifier( lhsTemp ), nullLiteral() ),
                                                                                          new IRConditionalAndExpression(
                                                                                            buildNotEquals( identifier( rhsTemp ), nullLiteral() ),
                                                                                            callEquals ) ) );
    return buildComposite( tempLhsAssignment,
                           tempRhsAssignment,
                           _expr().isEquals() ? theExpr : new IRNotExpression( theExpr ) );
  }

  private IRExpression compareWithCompareTo()
  {
    // Generates following code:
    // Comparable lhs = <lhs-expr>
    // Comparable rhs = <rhs-expr>
    // [!](lhs == rhs || (lhs != null && rhs != null && lhs.compareTo( rhs ) == 0))

    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.COMPARABLE() ) );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.COMPARABLE() ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );
    IRExpression callCompareTo = callMethod( Comparable.class, "compareTo", new Class[]{Object.class},
                                             identifier( lhsTemp ),
                                             Collections.singletonList( (IRExpression)identifier( rhsTemp ) ) );
    IRExpression theExpr = new IRConditionalOrExpression( buildEquals( identifier( lhsTemp ), identifier( rhsTemp ) ),
                                                          new IRConditionalAndExpression( buildNotEquals( identifier( lhsTemp ), nullLiteral() ),
                                                                                          new IRConditionalAndExpression(
                                                                                            buildNotEquals( identifier( rhsTemp ), nullLiteral() ),
                                                                                            buildEquals( callCompareTo, pushConstant( 0 ) ) ) ) );
    return buildComposite( tempLhsAssignment,
                           tempRhsAssignment,
                           _expr().isEquals() ? theExpr : new IRNotExpression( theExpr ) );
  }

  private IRExpression compareDynamically()
  {
    List<IRExpression> args = new ArrayList<IRExpression>();

    // Push the LHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );

    // Push the LHS Type
    args.add( pushType( _expr().getLHS().getType() ) );

    // Push is-equals
    args.add( pushConstant( _expr().isEquals() ) );

    // Push the RHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );

    // Push the RHS Type
    args.add( pushType( _expr().getRHS().getType() ) );

    // Call into Gosu runtime for equality
    return callStaticMethod( EqualityExpressionTransformer.class, "evaluate",
                             new Class[]{Object.class, IType.class, boolean.class, Object.class, IType.class},
                             args);
  }

  public static boolean evaluate( Object lhsValue, IType lhsType, boolean bEquals, Object rhsValue, IType rhsType )
  {
    boolean bValue;
    if( lhsValue != null && rhsValue != null && BeanAccess.isNumericType( lhsType ) )
    {
      bValue = ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
    else
    {
      bValue = BeanAccess.areValuesEqual( lhsType, lhsValue,
                                          rhsType, rhsValue );
    }
    return bEquals ? bValue : !bValue;
  }

}
