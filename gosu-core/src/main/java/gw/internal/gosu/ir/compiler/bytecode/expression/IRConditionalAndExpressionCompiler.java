/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.lang.ir.expression.IRConditionalAndExpression;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRConditionalAndExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRConditionalAndExpression expression, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    // Push LHS
    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    Label falseLabel = new Label();
    mv.visitJumpInsn( Opcodes.IFEQ, falseLabel );
    // Push RHS
    IRBytecodeCompiler.compileIRExpression( expression.getRhs(), context );
    mv.visitJumpInsn( Opcodes.IFEQ, falseLabel );
    mv.visitInsn( Opcodes.ICONST_1 );
    Label trueLabel = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, trueLabel );
    mv.visitLabel( falseLabel );
    mv.visitInsn( Opcodes.ICONST_0 );
    mv.visitLabel( trueLabel );
  }
}
