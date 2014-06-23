/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRReturnStatement;
import gw.lang.ir.statement.IRWhileStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRWhileStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IRWhileStatement whileLoopStatement, IRBytecodeContext context )
  {

    Label breakLabel = new Label();
    Label conditionLabel = new Label();
    Label loopBodyStart = new Label();

    context.pushBreakLabel( breakLabel);
    context.pushContinueLabel( conditionLabel );
    context.pushScope();
    try
    {
      // apparently this is true iff the body is guaranteed to execute only once and return
      // Really, this should test if the loop is infinite save returns/throws and
      // then generate the appropriate bytecode
      if( whileLoopStatement.getLeastSignificantTerminalStatement() != null)
      {
        context.visitLabel( conditionLabel );

        IRBytecodeCompiler.compileIRStatement( whileLoopStatement.getBody(), context );

        context.visitLabel( breakLabel );
      }
      else
      {
        context.getMv().visitJumpInsn( Opcodes.GOTO, conditionLabel ); // jump to condition

        context.visitLabel( loopBodyStart ); // body start

        IRBytecodeCompiler.compileIRStatement( whileLoopStatement.getBody(), context );

        context.visitLabel( conditionLabel );

        context.setLineNumber( whileLoopStatement.getLineNumber() ); // ensure loop test has line number matching start of while loop stmt
        IRBytecodeCompiler.compileIRExpression( whileLoopStatement.getLoopTest(), context );

        context.getMv().visitJumpInsn( Opcodes.IFNE, loopBodyStart );

        context.getMv().visitLabel( breakLabel );

        IRReturnStatement implicitReturn = whileLoopStatement.getImplicitReturnStatement();
        if( implicitReturn != null ) {
          IRBytecodeCompiler.compileIRStatement( implicitReturn, context );
        }
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
