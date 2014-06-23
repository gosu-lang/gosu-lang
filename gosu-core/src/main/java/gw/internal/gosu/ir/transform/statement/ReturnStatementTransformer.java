/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.expression.IRIdentifier;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.statement.IRSyntheticStatement;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.ITryCatchFinallyStatement;
import gw.lang.parser.expressions.IBlockLiteralExpression;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class ReturnStatementTransformer extends AbstractStatementTransformer<ReturnStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, ReturnStatement stmt )
  {
    ReturnStatementTransformer gen = new ReturnStatementTransformer( cc, stmt );
    return gen.compile();
  }

  private ReturnStatementTransformer( TopLevelTransformationContext cc, ReturnStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {

    IType retType = _stmt().getValue().getType();
    if( retType != JavaTypes.pVOID() )
    {
      IRExpression returnValue = compileReturnExpr( retType );
      return makeReturnStmt( returnValue );
    }
    else if( _cc().isBlockInvoke() )
    {
      // Blocks can return nothing but consist of a single expression that has a value.
      // If that's the case, we wrap that call in a synthetic statement so that the value gets popped off,
      // then follow it with the actual return statement that returns null.
      if( _stmt().getValue() != null )
      {
        return new IRStatementList(false,
          new IRSyntheticStatement( compileReturnExpr( retType ) ),
          makeReturnStmt( nullLiteral() )
        );
      } else {
        return makeReturnStmt( nullLiteral() );
      }
    }
    else
    {
      return buildReturn( );
    }
  }

  private IRReturnStatement makeReturnStmt( IRExpression returnValue )
  {
    if( needsTempVar() )
    {
      IRAssignmentStatement tempVarAssignment = buildAssignment( _cc().makeAndIndexTempSymbol( returnValue.getType() ), returnValue );
      IRIdentifier id = identifier( tempVarAssignment.getSymbol() );
      return new IRReturnStatement( tempVarAssignment, id );
    }
    else
    {
      return new IRReturnStatement( null, returnValue );
    }
  }

  private boolean needsTempVar()
  {
    return finallyBlockInParentHierarchy( _stmt() );
  }

  private boolean finallyBlockInParentHierarchy( IParsedElement stmt )
  {
    if( stmt == null || stmt instanceof IBlockLiteralExpression || stmt instanceof IClassStatement )
    {
      return false;
    }
    else if( stmt instanceof ITryCatchFinallyStatement && ((ITryCatchFinallyStatement) stmt).getFinallyStatement() != null)
    {
      return true;
    }
    else
    {
      return finallyBlockInParentHierarchy( stmt.getParent() );
    }
  }

  private IRExpression compileReturnExpr( IType retType )
  {
    // Compile the return value expression
    IRExpression expression = ExpressionTransformer.compile( _stmt().getValue(), _cc() );

    // handle implicit boxing/unbocking in block invoke methods or 
    if( (_cc().isBlockInvoke() || _cc().isFragmentEvaluation() ) && retType.isPrimitive() )
    {
      expression = boxValue( retType, expression );
    }

    // If we can determine what the return type of the enclosing function is, we're not within a block, and
    // we're not return null, and the return type isn't assignable from the actual type of the expression,
    // then we need to insert a cast.
    if (!_cc().isBlockInvoke() && _cc().getCurrentFunctionReturnType() != null) {
      IRType returnTypeDescriptor = getDescriptor( _cc().getCurrentFunctionReturnType() );
      if ( (!(expression instanceof IRNullLiteral) || finallyBlockInParentHierarchy( _stmt() )) &&
           !returnTypeDescriptor.isAssignableFrom( expression.getType() ) ) {
        expression = buildCast( returnTypeDescriptor, expression );
      }
    }
    return expression;
  }
}