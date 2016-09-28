/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.ConditionContext;
import gw.lang.ir.expression.IRTernaryExpression;
import gw.lang.ir.IRExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRTernaryExpressionCompiler extends AbstractBytecodeCompiler {

 public static void compile( IRTernaryExpression expression, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    IRExpression condition = expression.getTest();
    IRBytecodeCompiler.compileIRExpression( condition, context );
    ConditionContext conditionContext = condition.getConditionContext();
    mv.visitJumpInsn( negateOpcode( conditionContext.getOperator() ), conditionContext.generateFalseLabel() );
    conditionContext.fixLabels( true, mv );
    IRBytecodeCompiler.compileIRExpression( expression.getTrueValue(), context );
    Label afterIf = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, afterIf );
    conditionContext.fixLabels( false, mv );
    IRBytecodeCompiler.compileIRExpression( expression.getFalseValue(), context );
    mv.visitLabel( afterIf );
    conditionContext.clear();
  }
}
