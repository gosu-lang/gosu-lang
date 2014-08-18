/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.ir.nodes.IRTypeFactory;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.ParserBase;
import gw.internal.gosu.parser.expressions.ConditionalExpression;
import gw.internal.gosu.parser.expressions.EqualityExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.lang.ir.expression.IREqualityExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    else
    {
      if( lhsType.isAssignableFrom( rhsType ) &&
          !isDynamic( lhsType ) && !isDynamic( rhsType ) )
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
      else
      {
        IType type = ParserBase.resolveType( lhsType, '>', rhsType );
        if( !(type instanceof ErrorType) && (isNumberType( type ) || isBigType( type )) ) {
          return compareNumbers( type );
        }
        return compareDynamically();
      }
    }
  }

  private boolean isDynamic( IType type ) {
    return type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder();
  }

  private IRExpression compareNumbers( IType type ) {
    if( isBigType( type ) ) {
      return compareNumbersAsBig( type );
    }
    else {
      type = type.isPrimitive() ? type : TypeSystem.getPrimitiveType( type );
      return compareNumbersAsPrimitive( type );
    }
  }

  private IRExpression compareNumbersAsPrimitive( IType type ) {
    IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
    IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol tempRhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsAssn = buildAssignment( tempRhs, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    IRSymbol tempLhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRStatement lhsConversionAssn = convertOperandToPrimitive( type, _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );
    IRSymbol tempRhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRStatement rhsConversionAssn = convertOperandToPrimitive( type, _expr().getRHS().getType(), identifier( tempRhs ), tempRhsRet );

    IRExpression compareExpr = buildComposite( lhsConversionAssn, rhsConversionAssn,
                                               new IREqualityExpression( identifier( tempLhsRet ), identifier( tempRhsRet ), _expr().isEquals() ) );
    IRExpression nullCheckRhs = _expr().getRHS().getType().isPrimitive()
                                ? compareExpr
                                : buildTernary( buildEquals( identifier( tempRhs ), nullLiteral() ),
                                                booleanLiteral( false ),
                                                compareExpr,
                                                getDescriptor( boolean.class ) );
    IRExpression expr = _expr().getLHS().getType().isPrimitive()
                        ? nullCheckRhs
                        : buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                        booleanLiteral( false ),
                                        nullCheckRhs,
                                        getDescriptor( boolean.class ) );
    return buildComposite( tempLhsAssn, tempRhsAssn, expr );
  }

  private IRExpression compareNumbersAsBig( IType type ) {
    Class bigClass = type == JavaTypes.BIG_DECIMAL() ? BigDecimal.class : BigInteger.class;

    IRSymbol tempLhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getLHS().getType() ) );
    IRAssignmentStatement tempLhsAssn = buildAssignment( tempLhs, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol tempRhs = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsAssn = buildAssignment( tempRhs, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    IRSymbol tempLhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRStatement lhsConversionAssn = convertOperandToBig( type, bigClass, _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );
    IRSymbol tempRhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRStatement rhsConversionAssn = convertOperandToBig( type, bigClass, _expr().getRHS().getType(), identifier( tempRhs ), tempRhsRet );

    IRExpression compareExpr = buildComposite( lhsConversionAssn, rhsConversionAssn,
                                               new IREqualityExpression( callMethod( bigClass, "compareTo", new Class[]{bigClass}, identifier( tempLhsRet ), Collections.<IRExpression>singletonList( identifier( tempRhsRet ) ) ),
                                                                         pushConstant( 0 ), _expr().isEquals() ) );
    IRExpression nullCheckRhs = _expr().getRHS().getType().isPrimitive()
                                ? compareExpr
                                : buildTernary( buildEquals( identifier( tempRhs ), nullLiteral() ), booleanLiteral( false ), compareExpr, getDescriptor( boolean.class ) );
    IRExpression expr =  _expr().getLHS().getType().isPrimitive()
                         ? nullCheckRhs
                         : buildTernary( buildEquals( identifier( tempLhs ), nullLiteral() ),
                                         booleanLiteral( false ),
                                         nullCheckRhs,
                                         getDescriptor( boolean.class ) );
    return buildComposite( tempLhsAssn, tempRhsAssn, expr );
  }

  private IRExpression compareArrays() {
    IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
    IRExpression rhs = ExpressionTransformer.compile( _expr().getRHS(), _cc() );

    IType lhsType = _expr().getLHS().getType();

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

    IType lhsType = _expr().getLHS().getType();
    IRType lhsIrType = getDescriptor( lhsType );
    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( lhsIrType );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );
    IRExpression callCompareTo = buildMethodCall( lhsIrType, "compareTo", lhsType.isInterface(), getDescriptor( int.class ), Collections.<IRType>singletonList( getDescriptor( Object.class ) ),
                                                  identifier( lhsTemp ), Collections.singletonList( (IRExpression)identifier( rhsTemp ) ) );
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
    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( Object.class ) );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp,  boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( Object.class ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp,  boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );

    List<IRExpression> args = new ArrayList<IRExpression>();
    args.add( identifier( lhsTemp ) );
    args.add( pushType( _expr().getLHS().getType() ) );
    args.add( pushConstant( _expr().isEquals() ) );
    args.add( identifier( rhsTemp ) );
    args.add( pushType( _expr().getRHS().getType() ) );

    // lhs === rhs ? true : compareDynamically( ... )
    return buildComposite( tempLhsAssignment, tempRhsAssignment,
                           buildTernary( buildEquals( identifier( lhsTemp ), identifier( rhsTemp ) ), booleanLiteral( _expr().isEquals() ),
                                         callStaticMethod( EqualityExpressionTransformer.class, "evaluate",
                                                           new Class[]{Object.class, IType.class, boolean.class, Object.class, IType.class},
                                                           args ), getDescriptor( boolean.class ) ) );
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
