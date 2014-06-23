/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.ir.transform.statement.*;
import gw.internal.gosu.parser.statements.*;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.lang.parser.IStatement;

/**
 */
public class StatementTransformer
{
  public static IRStatement compile( TopLevelTransformationContext context, IStatement stmt )
  {
    try
    {
      if( stmt instanceof StatementList )
      {
        return StatementListTransformer.compile( context, (StatementList)stmt );
      }
      else if( stmt instanceof VarStatement )
      {
        return VarStatementTransformer.compile( context, (VarStatement)stmt );
      }
      else if( stmt instanceof AssignmentStatement )
      {
        return AssignmentStatementTransformer.compile( context, (AssignmentStatement)stmt );
      }
      else if( stmt instanceof MemberAssignmentStatement )
      {
        return MemberAssignmentStatementTransformer.compile( context, (MemberAssignmentStatement)stmt );
      }
      else if( stmt instanceof ArrayAssignmentStatement )
      {
        return ArrayAssignmentStatementTransformer.compile( context, (ArrayAssignmentStatement)stmt );
      }
      else if( stmt instanceof MapAssignmentStatement)
      {
        return MapAssignmentStatementTransformer.compile( context, (MapAssignmentStatement)stmt );
      }
      else if( stmt instanceof MethodCallStatement )
      {
        return MethodCallStatementTransformer.compile( context, (MethodCallStatement)stmt );
      }
      else if( stmt instanceof NewStatement )
      {
        return NewStatementTransformer.compile( context, (NewStatement)stmt );
      }
      else if( stmt instanceof BlockInvocationStatement )
      {
        return BlockInvocationStatementTransformer.compile( context, (BlockInvocationStatement)stmt );
      }
      else if( stmt instanceof BeanMethodCallStatement )
      {
        return BeanMethodCallStatementTransformer.compile( context, (BeanMethodCallStatement)stmt );
      }
      else if( stmt instanceof ReturnStatement )
      {
        return ReturnStatementTransformer.compile( context, (ReturnStatement)stmt );
      }
      else if( stmt instanceof BreakStatement )
      {
        return BreakStatementTransformer.compile( context, (BreakStatement)stmt );
      }
      else if( stmt instanceof ContinueStatement )
      {
        return ContinueStatementTransformer.compile( context, (ContinueStatement)stmt );
      }
      else if( stmt instanceof IfStatement)
      {
        return IfStatementTransformer.compile( context, (IfStatement)stmt );
      }
      else if( stmt instanceof WhileStatement )
      {
        return WhileStatementTransformer.compile( context, (WhileStatement)stmt );
      }
      else if( stmt instanceof DoWhileStatement )
      {
        return DoWhileStatementTransformer.compile( context, (DoWhileStatement)stmt );
      }
      else if( stmt instanceof ForEachStatement )
      {
        return ForEachStatementTransformer.compile( context, (ForEachStatement)stmt );
      }
      else if( stmt instanceof SwitchStatement)
      {
        return SwitchStatementTransformer.compile( context, (SwitchStatement)stmt );
      }
      else if( stmt instanceof TryCatchFinallyStatement)
      {
        return TryCatchFinallyStatementTransformer.compile( context, (TryCatchFinallyStatement)stmt );
      }
      else if( stmt instanceof ThrowStatement )
      {
        return ThrowStatementTransformer.compile( context, (ThrowStatement)stmt );
      }
      else if( stmt instanceof AssertStatement )
      {
        return AssertStatementTransformer.compile( context, (AssertStatement) stmt );
      }
      else if( stmt instanceof UsingStatement )
      {
        return UsingStatementTransformer.compile( context, (UsingStatement)stmt );
      }
      else if( stmt instanceof EvalStatement )
      {
        return EvalStatementTransformer.compile( context, (EvalStatement)stmt );
      }
      else if( stmt instanceof SyntheticFunctionStatement )
      {
        throw new IllegalArgumentException("SyntheticFunctionStatements need to be compiled explicitly, since they require the backing DFS");
      }
      else if( stmt instanceof SyntheticMemberAccessStatement )
      {
        return SyntheticMemberAccessStatementTransformer.compile( context, (SyntheticMemberAccessStatement)stmt );
      }
      else if( stmt instanceof HideFieldNoOpStatement )
      {
        return VarStatementTransformer.compile( context, ((HideFieldNoOpStatement)stmt).getVarStmt() );
      }
      else if( stmt instanceof NoOpStatement )
      {
        // noop
        return new IRNoOpStatement();
      }
      else if( stmt instanceof UsesStatement || stmt instanceof UsesStatementList )
      {
        // noop
        return null;
      }
      else
      {
        throw new UnsupportedOperationException(
          "Statement Compiler not yet implemented for: " + stmt.getClass().getName() );
      }
    }
    finally
    {
      context.updateSuperInvokedAfterLastExpressionCompiles();
    }
  }

  // This is in its own method because it requires additional context
  public static IRStatement compileInitializerAssignment( TopLevelTransformationContext context, InitializerAssignment stmt, IRExpression root )
  {
    return InitializerAssignmentTransformer.compile( context, stmt, root );
  }
}