/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.IRStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRForEachStatementCompiler extends AbstractBytecodeCompiler
{

  public static void compile( IRForEachStatement forLoop, IRBytecodeContext context )
  {

    Label breakLabel = new Label();
    Label conditionLabel = new Label();

    context.pushBreakLabel( breakLabel );
    context.pushContinueLabel( conditionLabel );
    context.pushScope();
    try
    {
      for( IRStatement initializer : forLoop.getInitializers() )
      {
        IRBytecodeCompiler.compileIRStatement( initializer, context );
      }

      if( forLoop.hasIdentifierToNullCheck() )
      {
        IRBytecodeCompiler.compileIRExpression( forLoop.getIdentifierToNullCheck(), context );
        context.getMv().visitJumpInsn( Opcodes.IFNULL, breakLabel );
      }

      context.visitLabel(conditionLabel);
      context.setLineNumber(forLoop.getLineNumber()); // ensure loop test has line number matching start of for-each stmt
      IRBytecodeCompiler.compileIRExpression(forLoop.getLoopTest(), context);
      context.getMv().visitJumpInsn(Opcodes.IFEQ, breakLabel);
      // increments
      for( IRStatement incrementors : forLoop.getIncrementors() )
      {
        IRBytecodeCompiler.compileIRStatement( incrementors, context );
      }
      IRBytecodeCompiler.compileIRStatement(forLoop.getBody(), context);
      context.getMv().visitJumpInsn(Opcodes.GOTO, conditionLabel); // jump to condition
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
