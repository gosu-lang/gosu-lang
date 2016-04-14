/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.statement.IRIfStatement;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRIfStatementCompiler extends AbstractBytecodeCompiler {
  public static void compile(IRIfStatement statement, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    IRExpression condition = statement.getExpression();
    IRBytecodeCompiler.compileIRExpression( condition, context );
    ConditionContext conditionContext = condition.getConditionContext();
    mv.visitJumpInsn( negateOpcode( conditionContext.getOperator() ), conditionContext.generateFalseLabel() );
    conditionContext.fixLabels( true, mv );
    IRBytecodeCompiler.compileIRStatement( statement.getIfStatement(), context );
    Label afterIf = new Label();
    if( statement.getElseStatement() != null )
    {
      mv.visitJumpInsn( Opcodes.GOTO, afterIf );
      boolean bTerminal = statement.getLeastSignificantTerminalStatement() != null;
      if( !bTerminal )
      {
        mv.visitJumpInsn( Opcodes.GOTO, afterIf );
      }
      conditionContext.fixLabels( false, mv );
      IRBytecodeCompiler.compileIRStatement( statement.getElseStatement(), context );
    }
    else
    {
      conditionContext.fixLabels( false, mv );
    }
    mv.visitLabel( afterIf );
    conditionContext.clear();
  }
}
