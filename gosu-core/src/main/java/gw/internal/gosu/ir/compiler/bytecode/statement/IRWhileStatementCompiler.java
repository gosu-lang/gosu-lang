/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.statement.IRWhileStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRWhileStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IRWhileStatement whileLoopStatement, IRBytecodeContext context )
  {
    // apparently this is true iff the body is guaranteed to execute only once and return
    // Really, this should test if the loop is infinite save returns/throws and
    // then generate the appropriate bytecode
    if( whileLoopStatement.getLeastSignificantTerminalStatement() != null )
    {
      Label breakLabel = new Label();
      Label conditionLabel = new Label();

      context.pushBreakLabel( breakLabel );
      context.pushContinueLabel( conditionLabel );
      context.pushScope();
      try
      {
        context.visitLabel( conditionLabel );

        IRBytecodeCompiler.compileIRStatement( whileLoopStatement.getBody(), context );

        context.visitLabel( breakLabel );
      }
      finally
      {
        context.popScope();
        context.popBreakLabel();
        context.popContinueLabel();
      }
    }
    else
    {
      Label conditionLabel = new Label();

      context.pushContinueLabel( conditionLabel );
      context.pushScope();
      try
      {
        context.visitLabel( conditionLabel );
        context.setLineNumber( whileLoopStatement.getLineNumber() ); // ensure loop test has line number matching start of while loop stmt
        IRBytecodeCompiler.compileIRExpression( whileLoopStatement.getLoopTest(), context );
        ConditionContext conditionContext = whileLoopStatement.getLoopTest().getConditionContext();
        Label breakLabel = conditionContext.generateFalseLabel();
        context.getMv().visitJumpInsn( negateOpcode( conditionContext.getOperator() ), breakLabel );
        context.pushBreakLabel( breakLabel );
        conditionContext.fixLabels( true, context.getMv() );
        IRBytecodeCompiler.compileIRStatement( whileLoopStatement.getBody(), context );
        context.getMv().visitJumpInsn( Opcodes.GOTO, conditionLabel ); // jump to condition
        conditionContext.fixLabels( false, context.getMv() );
      }
      finally
      {
        context.popScope();
        context.popBreakLabel();
        context.popContinueLabel();
      }
    }
  }
}
