/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRDoWhileStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRDoWhileStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IRDoWhileStatement doWhileStatement, IRBytecodeContext context )
  {
    Label breakLabel = new Label();
    Label conditionLabel = new Label();
    Label loopBodyStart = new Label();

    context.pushBreakLabel( breakLabel);
    context.pushContinueLabel( conditionLabel );
    context.pushScope();
    try
    {
      context.visitLabel( loopBodyStart ); // body start
      IRBytecodeCompiler.compileIRStatement( doWhileStatement.getBody(), context );

      context.visitLabel( conditionLabel );
      context.setLineNumber( doWhileStatement.getLoopTest().getLineNumber() );
      IRBytecodeCompiler.compileIRExpression( doWhileStatement.getLoopTest(), context );
      context.getMv().visitJumpInsn( Opcodes.IFNE, loopBodyStart );

      context.getMv().visitLabel( breakLabel );
    }
    finally
    {
      context.popScope();
      context.popBreakLabel();
      context.popContinueLabel();
    }
  }
}
