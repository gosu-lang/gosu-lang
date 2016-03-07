/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.statement.IRReturnStatement;
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
    if( whileLoopStatement.getLeastSignificantTerminalStatement() != null)
    {
      Label breakLabel = new Label();
      Label conditionLabel = new Label();

      context.pushBreakLabel( breakLabel);
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
      ConditionContext conditionContext = whileLoopStatement.getLoopTest().getConditionContext();
      Label breakLabel = conditionContext.generateFalseLabel();
      Label conditionLabel = new Label();

      context.pushBreakLabel(breakLabel);
      context.pushContinueLabel(conditionLabel);
      context.pushScope();
      try
      {
        MethodVisitor mv = context.getMv();
        context.setLineNumber( whileLoopStatement.getLineNumber() ); // ensure loop test has line number matching start of while loop stmt
        //todo: why do we visit from context and not from mv?
        context.visitLabel( conditionLabel );
        IRBytecodeCompiler.compileIRExpression( whileLoopStatement.getLoopTest(), context );
        mv.visitJumpInsn( negateOpcode( conditionContext.getOperator() ), breakLabel );
        conditionContext.fixLabels( true, mv );
        IRBytecodeCompiler.compileIRStatement( whileLoopStatement.getBody(), context );
        mv.visitJumpInsn( Opcodes.GOTO, conditionLabel ); // jump to condition
        conditionContext.fixLabels( false, mv );
        context.getMv().visitLabel( breakLabel );
        // NO CALLS TO IRLoopStatement#setImplicitReturnStatement?
        IRReturnStatement implicitReturn = whileLoopStatement.getImplicitReturnStatement();
        if( implicitReturn != null ) {
          IRBytecodeCompiler.compileIRStatement( implicitReturn, context );
        }
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
