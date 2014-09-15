/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.IRType;
import gw.lang.ir.expression.IRNullLiteral;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;
import gw.internal.gosu.parser.DynamicSymbol;
import gw.internal.gosu.parser.ScopedDynamicSymbol;
import gw.internal.gosu.parser.Symbol;
import gw.lang.parser.GlobalScope;
import gw.lang.parser.IExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IType;

import java.util.Collections;

/**
 */
public class VarStatementTransformer extends AbstractStatementTransformer<IVarStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, IVarStatement stmt )
  {
    VarStatementTransformer compiler = new VarStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private VarStatementTransformer( TopLevelTransformationContext cc, IVarStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    IExpression asExp = _stmt().getAsExpression();
    Symbol symbol = (Symbol)_stmt().getSymbol();
    IType type = symbol.getType();

    // Determine the initial value
    IRExpression value = null;
    if( asExp != null )
    {
      if( symbol instanceof ScopedDynamicSymbol )
      {
        //## todo: probably not support request/session vars anymore?
        throw new UnsupportedOperationException( "Scoped vars not supported in Java classes files (for now)" );
      }
      else
      {
        value = ExpressionTransformer.compile( asExp, _cc() );
        // If the value we're assigning isn't assignable to the symbol's type, we need to insert a cast.
        // This should only happen in strange cases like assigning a Group to a Group or in the case
        // of a compound type.
        IRType symbolType = getDescriptor( type );
        if (!(value instanceof IRNullLiteral) &&
            !symbolType.isAssignableFrom(value.getType()) &&
            !(symbolType == JavaClassIRType.get( char.class ) && value.getType() == JavaClassIRType.get( int.class ) )) {
          value = buildCast( symbolType, value );
        }
      }
    }
    else if( _stmt().getScope() == null || _stmt().getScope() == GlobalScope.EXECUTION )
    {
      //## todo: perf: handle case where initialization is unnecessary i.e., var is definitely assigned value in following code branch[es]
      // If the scope is request or session, we can't initialize it because we don't want to clobber the current value
      value = getDefaultConstIns( type );
    }

    // If the symbol is boxed, then create an array of size 1 with that value in it
    IRType symbolType;
    if( symbol.isValueBoxed() && value != null )
    {
      value = buildInitializedArray( getDescriptor( type ), Collections.singletonList( value ) );
      symbolType = getDescriptor( symbol.getType() ).getArrayType();
    }
    else
    {
      symbolType = getDescriptor( symbol.getType() );
    }

    IRStatement assignmentStmt;
    if( isProgramVar( symbol ) )
    {
      // Program var (maintained as a field on the generated class)
      assignmentStmt = setInstanceField( getGosuClass(), symbol.getName(), getDescriptor( symbol.getType() ),
                                         AccessibilityUtil.forSymbol( symbol ), pushThis(), value );
    }
    else if ( _stmt().getScope() == null || _stmt().getScope() == GlobalScope.EXECUTION )
    {
      // If the scope is request or session, we don't want to store anything on the stack at all.
      // Otherwise, we treat this as a local variable
      assignmentStmt = buildAssignment( _cc().createSymbol( symbol.getName(), symbolType ), value );
    }
    else
    {
      assignmentStmt = new IRNoOpStatement();
    }
    assignmentStmt.setImplicit( asExp == null );
    return assignmentStmt;
  }

  private boolean isProgramVar( Symbol symbol )
  {
    return _cc().compilingProgram() && symbol instanceof DynamicSymbol;
  }
}
