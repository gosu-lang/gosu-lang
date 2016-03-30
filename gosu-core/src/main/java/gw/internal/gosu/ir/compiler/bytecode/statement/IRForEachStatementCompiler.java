/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.statement.IRForEachStatement;
import gw.lang.ir.IRStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRForEachStatementCompiler extends AbstractBytecodeCompiler
{

  public static void compile( IRForEachStatement forLoop, IRBytecodeContext context )
  {
    Label conditionLabel = new Label();

    context.pushContinueLabel( conditionLabel );
    context.pushScope();
    try
    {
      for( IRStatement initializer : forLoop.getInitializers() )
      {
        IRBytecodeCompiler.compileIRStatement( initializer, context );
      }

      Label breakNull = new Label();
      if( forLoop.hasIdentifierToNullCheck() )
      {
        IRBytecodeCompiler.compileIRExpression( forLoop.getIdentifierToNullCheck(), context );
        context.getMv().visitJumpInsn( Opcodes.IFNULL,  breakNull );
      }

      context.visitLabel(conditionLabel);
      context.setLineNumber(forLoop.getLineNumber()); // ensure loop test has line number matching start of for-each stmt
      IRBytecodeCompiler.compileIRExpression(forLoop.getLoopTest(), context);
      ConditionContext conditionContext = forLoop.getLoopTest().getConditionContext();
      Label breakLabel = conditionContext.generateFalseLabel();
      conditionContext.getLabels( false ).add( breakNull );
      context.getMv().visitJumpInsn( negateOpcode( conditionContext.getOperator() ), breakLabel );
      context.pushBreakLabel( breakLabel );
      conditionContext.fixLabels( true, context.getMv() );
      // increments
      for( IRStatement incrementors : forLoop.getIncrementors() )
      {
        IRBytecodeCompiler.compileIRStatement( incrementors, context );
      }
      IRBytecodeCompiler.compileIRStatement(forLoop.getBody(), context);
      context.getMv().visitJumpInsn(Opcodes.GOTO, conditionLabel); // jump to condition
      conditionContext.fixLabels( false, context.getMv() );
      conditionContext.clear();
    }
    finally
    {
      context.popScope();
      context.popBreakLabel();
      context.popContinueLabel();
    }
  }
}
