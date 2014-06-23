/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRArrayLengthExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRArrayLengthExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRArrayLengthExpression expression, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRElement(expression.getRoot(), context);
    MethodVisitor mv = context.getMv();
    mv.visitInsn(Opcodes.ARRAYLENGTH);
  }
}