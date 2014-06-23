/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRConditionalOrExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRConditionalOrExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRConditionalOrExpression expression, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    // Push LHS
    IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
    Label trueLabel = new Label();
    mv.visitJumpInsn( Opcodes.IFNE, trueLabel );
    // Push RHS
    IRBytecodeCompiler.compileIRExpression( expression.getRhs(), context );
    mv.visitJumpInsn( Opcodes.IFNE, trueLabel );
    mv.visitInsn( Opcodes.ICONST_0 );
    Label falseLabel = new Label();
    mv.visitJumpInsn( Opcodes.GOTO, falseLabel );
    mv.visitLabel( trueLabel );
    mv.visitInsn( Opcodes.ICONST_1 );
    mv.visitLabel( falseLabel );
  }
}