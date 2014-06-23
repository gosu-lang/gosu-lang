/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRNewExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRTypeConstants;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRNewExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNewExpression expression, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();
    mv.visitTypeInsn( Opcodes.NEW, expression.getOwnersType().getSlashName() );
    mv.visitInsn( Opcodes.DUP );

    for (IRExpression arg : expression.getArgs()) {
      IRBytecodeCompiler.compileIRExpression( arg, context );
    }

    StringBuilder descriptor = new StringBuilder();
    descriptor.append("(");
    for (IRType param : expression.getParameterTypes()) {
      descriptor.append(param.getDescriptor());
    }
    descriptor.append(")");
    descriptor.append(IRTypeConstants.pVOID().getDescriptor());

    mv.visitMethodInsn( Opcodes.INVOKESPECIAL,
                        expression.getOwnersType().getSlashName(),
                        "<init>",
                        descriptor.toString() ); 
  }
}
