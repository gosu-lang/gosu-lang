/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.parser.ParserBase;
import gw.internal.gosu.parser.expressions.RelationalExpression;
import gw.internal.gosu.parser.expressions.ConditionalExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.lang.ir.expression.IRRelationalExpression;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICoercionManager;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.config.CommonServices;

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
    else if( lhsType == rhsType && JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
    {
      return compareWithCompareTo();
    }
    else
    {
      return compareDynamically();
    }
  }

  private IRExpression compareWithCompareTo()
  {
    // Generates following code:
    // Comparable lhs = <lhs-expr>
    // Comparable rhs = <rhs-expr>
    // (lhs != null && rhs != null && lhs.compareTo( rhs ) [>, <, >=, <=] 0)

    IRSymbol lhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.COMPARABLE() ) );
    IRAssignmentStatement tempLhsAssignment = buildAssignment( lhsTemp, ExpressionTransformer.compile( _expr().getLHS(), _cc() ) );
    IRSymbol rhsTemp = _cc().makeAndIndexTempSymbol( getDescriptor( JavaTypes.COMPARABLE() ) );
    IRAssignmentStatement tempRhsAssignment = buildAssignment( rhsTemp, ExpressionTransformer.compile( _expr().getRHS(), _cc() ) );
    IRExpression callCompareTo = callMethod( Comparable.class, "compareTo", new Class[]{Object.class},
                                             identifier( lhsTemp ),
                                             Collections.singletonList( (IRExpression)identifier( rhsTemp ) ) );
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
    List<IRExpression> args = new ArrayList<IRExpression>();

    // Push the LHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getLHS().getType(), ExpressionTransformer.compile( _expr().getLHS(), _cc() ) ) );

    // Push the LHS Type
    args.add( pushType( _expr().getLHS().getType() ) );

    // Push operator
    args.add( pushConstant( _expr().getOperator() ) );

    // Push the RHS expression value and make sure it's boxed for the method call
    args.add( boxValue( _expr().getRHS().getType(), ExpressionTransformer.compile( _expr().getRHS(), _cc() ) ) );

    // Push the RHS Type
    args.add( pushType( _expr().getRHS().getType() ) );

    // Call into Gosu's runtime for the Boolean answer
    return callStaticMethod( RelationalExpressionTransformer.class, "evaluate",
            new Class[]{Object.class, IType.class, String.class, Object.class, IType.class},
            args);
  }

  public static boolean evaluate( Object lhsValue, IType lhsType, String strOperator, Object rhsValue, IType rhsType )
  {
    if( lhsValue == null )
    {
      return false;
    }
    if( rhsValue == null )
    {
      return false;
    }

    final ICoercionManager coercionMgr = CommonServices.getCoercionManager();

    if( strOperator.equals( ">" ) )
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
    else if( strOperator.equals( "<" ) )
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
    else if( strOperator.equals( ">=" ) )
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
    else // if( _strOperator.equals( "<=" ) )
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
  }

}
