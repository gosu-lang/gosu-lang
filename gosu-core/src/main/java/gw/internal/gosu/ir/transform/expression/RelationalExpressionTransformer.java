/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ErrorType;
import gw.internal.gosu.parser.ParserBase;
import gw.internal.gosu.parser.expressions.RelationalExpression;
import gw.internal.gosu.parser.expressions.ConditionalExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICoercionManager;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.config.CommonServices;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class RelationalExpressionTransformer extends AbstractExpressionTransformer<RelationalExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, RelationalExpression expr )
  {
    RelationalExpressionTransformer gen = new RelationalExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private RelationalExpressionTransformer( TopLevelTransformationContext cc, RelationalExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();
    if( lhsType.isPrimitive() && rhsType.isPrimitive() )
    {
      return comparePrimitives();
    }
    else {
      if( lhsType == rhsType && JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
      {
        return compareWithCompareTo();
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
    IRAssignmentStatement lhsConversionAssn = convertOperandToPrimitive( type, _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );
    IRSymbol tempRhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRAssignmentStatement rhsConversionAssn = convertOperandToPrimitive( type, _expr().getRHS().getType(), identifier( tempRhs ), tempRhsRet );
    
    IRExpression compareExpr = buildComposite( lhsConversionAssn, rhsConversionAssn,
                                               new IRRelationalExpression( identifier( tempLhsRet ), identifier( tempRhsRet ), IRRelationalExpression.Operation.get( _expr().getOperator() ) ) );
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
    IRAssignmentStatement lhsConversionAssn = convertOperandToBig( type, bigClass, _expr().getLHS().getType(), identifier( tempLhs ), tempLhsRet );
    IRSymbol tempRhsRet = _cc().makeAndIndexTempSymbol( getDescriptor( type ) );
    IRAssignmentStatement rhsConversionAssn = convertOperandToBig( type, bigClass, _expr().getRHS().getType(), identifier( tempRhs ), tempRhsRet );
    
    IRExpression compareExpr = buildComposite( lhsConversionAssn, rhsConversionAssn,
                                               new IRRelationalExpression( callMethod( bigClass, "compareTo", new Class[]{bigClass}, identifier( tempLhsRet ), Collections.<IRExpression>singletonList( identifier( tempRhsRet ) ) ),
                                                                           pushConstant( 0 ), IRRelationalExpression.Operation.get( _expr().getOperator() ) ) );
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

  private IRExpression compareWithCompareTo()
  {
    // Generates following code:
    // Comparable lhs = <lhs-expr>
    // Comparable rhs = <rhs-expr>
    // (lhs != null && rhs != null && lhs.compareTo( rhs ) [>, <, >=, <=] 0)

    IType lhsType = _expr().getLHS().getType();
    IRType lhsIrType = getDescriptor( lhsType );
    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( lhsIrType );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( _expr().getRHS().getType() ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );
    IRExpression callCompareTo = buildMethodCall( lhsIrType, "compareTo", lhsType.isInterface(), getDescriptor( int.class ), Collections.singletonList( getDescriptor( Object.class ) ),
                                                  identifier( lhsTemp ), Collections.singletonList( (IRExpression)identifier( rhsTemp ) ) );
    IRExpression theExpr = new IRConditionalAndExpression( buildNotEquals( identifier( lhsTemp ), nullLiteral() ),
                                                           new IRConditionalAndExpression( buildNotEquals( identifier( rhsTemp ), nullLiteral() ),
                                                                                           new IRRelationalExpression( callCompareTo, pushConstant( 0 ),
                                                                                                                       IRRelationalExpression.Operation.get( _expr().getOperator() ) ) ) );
    return buildComposite( tempLhsAssignment,
                           tempRhsAssignment,
                           theExpr );
  }

  private IRExpression comparePrimitives()
  {
    // Get the upper bound type
    IType lhsType = _expr().getLHS().getType();
    IType rhsType = _expr().getRHS().getType();
    IType type = ParserBase.resolveType( lhsType, '>', rhsType );

    IRExpression lhs = numberConvert( _expr().getLHS().getType(), type, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRExpression rhs = numberConvert( _expr().getRHS().getType(), type, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );

    IRRelationalExpression.Operation op;
    if( _expr().getOperator().equals( ">" ) )
    {
      op = IRRelationalExpression.Operation.GT;
    }
    else if (_expr().getOperator().equals( ">=" ))
    {
      op = IRRelationalExpression.Operation.GTE;
    }
    else if (_expr().getOperator().equals( "<" ))
    {
      op = IRRelationalExpression.Operation.LT;
    }
    else if (_expr().getOperator().equals( "<=" ))
    {
      op = IRRelationalExpression.Operation.LTE;
    }
    else
    {
      throw new IllegalArgumentException( "Unrecognized relational operation " + _expr().getOperator() );
    }

    return new IRRelationalExpression(lhs, rhs, op);
  }

  private IRExpression compareDynamically()
  {
    List<IRExpression> args = new ArrayList<>();

    // Push the LHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );

    // Push operator
    args.add( pushConstant( _expr().getOperator() ) );

    // Push the RHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );

    // Call into Gosu's runtime for the Boolean answer
    return callStaticMethod( RelationalExpressionTransformer.class, "evaluate",
            new Class[]{Object.class, String.class, Object.class},
            args);
  }

  public static boolean evaluate( Object lhsValue, String strOperator, Object rhsValue )
  {
    if( lhsValue == null )
    {
      return false;
    }
    if( rhsValue == null )
    {
      return false;
    }

    IType lhsType = TypeSystem.getFromObject( lhsValue );
    IType rhsType = TypeSystem.getFromObject( rhsValue );

    final ICoercionManager coercionMgr = CommonServices.getCoercionManager();

    switch( strOperator )
    {
      case ">":
      {
        if( BeanAccess.isNumericType( lhsType ) )
        {
          return ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) > 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
        {
          return coercionMgr.makeDateFrom( lhsValue ).after( coercionMgr.makeDateFrom( rhsValue ) ) ? Boolean.TRUE : Boolean.FALSE;
        }
        else
        {
          if( BeanAccess.isBeanType( lhsType ) )
          {
            if( BeanAccess.isBeanType( rhsType ) )
            {
              if( lhsType.isAssignableFrom( rhsType ) )
              {
                if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
                {
                  //noinspection unchecked
                  return ((Comparable)lhsValue).compareTo( rhsValue ) > 0 ? Boolean.TRUE : Boolean.FALSE;
                }
              }
            }
          }
        }

        return coercionMgr.makeStringFrom( lhsValue ).compareTo( coercionMgr.makeStringFrom( rhsValue ) ) > 0 ? Boolean.TRUE : Boolean.FALSE;
      }

      case "<":
      {
        if( BeanAccess.isNumericType( lhsType ) )
        {
          return ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) < 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
        {
          return coercionMgr.makeDateFrom( lhsValue ).before( coercionMgr.makeDateFrom( rhsValue ) ) ? Boolean.TRUE : Boolean.FALSE;
        }
        else
        {
          if( BeanAccess.isBeanType( lhsType ) )
          {
            if( BeanAccess.isBeanType( rhsType ) )
            {
              if( lhsType.isAssignableFrom( rhsType ) )
              {
                if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
                {
                  //noinspection unchecked
                  return ((Comparable)lhsValue).compareTo( rhsValue ) < 0 ? Boolean.TRUE : Boolean.FALSE;
                }
              }
            }
          }
        }

        return coercionMgr.makeStringFrom( lhsValue ).compareTo( coercionMgr.makeStringFrom( rhsValue ) ) < 0 ? Boolean.TRUE : Boolean.FALSE;
      }

      case ">=":
      {
        if( BeanAccess.isNumericType( lhsType ) )
        {
          return ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) >= 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
        {
          Date l = coercionMgr.makeDateFrom( lhsValue );
          Date r = coercionMgr.makeDateFrom( rhsValue );
          return (l.compareTo( r ) >= 0) ? Boolean.TRUE : Boolean.FALSE;
        }
        else
        {
          if( BeanAccess.isBeanType( lhsType ) )
          {
            if( BeanAccess.isBeanType( rhsType ) )
            {
              if( lhsType.isAssignableFrom( rhsType ) )
              {
                if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
                {
                  //noinspection unchecked
                  return ((Comparable)lhsValue).compareTo( rhsValue ) >= 0 ? Boolean.TRUE : Boolean.FALSE;
                }
              }
            }
          }
        }

        return coercionMgr.makeStringFrom( lhsValue ).compareTo( coercionMgr.makeStringFrom( rhsValue ) ) >= 0 ? Boolean.TRUE : Boolean.FALSE;
      }

      case "<=":
      {
        if( BeanAccess.isNumericType( lhsType ) )
        {
          return ConditionalExpression.compareNumbers( lhsValue, rhsValue, lhsType, rhsType ) <= 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
        {
          Date l = coercionMgr.makeDateFrom( lhsValue );
          Date r = coercionMgr.makeDateFrom( rhsValue );
          return (l.before( r ) || l.equals( r )) ? Boolean.TRUE : Boolean.FALSE;
        }
        else
        {
          if( BeanAccess.isBeanType( lhsType ) )
          {
            if( BeanAccess.isBeanType( rhsType ) )
            {
              if( lhsType.isAssignableFrom( rhsType ) )
              {
                if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
                {
                  //noinspection unchecked
                  return ((Comparable)lhsValue).compareTo( rhsValue ) <= 0 ? Boolean.TRUE : Boolean.FALSE;
                }
              }
            }
          }
        }

        return coercionMgr.makeStringFrom( lhsValue ).compareTo( coercionMgr.makeStringFrom( rhsValue ) ) <= 0 ? Boolean.TRUE : Boolean.FALSE;
      }

      default:
        throw new IllegalStateException();
    }
  }
}
