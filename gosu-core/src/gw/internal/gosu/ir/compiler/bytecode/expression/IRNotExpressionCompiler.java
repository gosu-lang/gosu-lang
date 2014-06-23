/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRNotExpression;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class IRNotExpressionCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRNotExpression expression, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRElement( expression.getRoot(), context );

    MethodVisitor mv = context.getMv();
    if ( expression.getType().isBoolean() ) {
      Label yea = new Label();
      mv.visitJumpInsn( Opcodes.IFEQ, yea ); // If the expression is false, push a 1 on the stack; otherwise, push a 0 on
      mv.visitInsn( Opcodes.ICONST_0 );
      Label nay = new Label();
      mv.visitJumpInsn( Opcodes.GOTO, nay );
      mv.visitLabel( yea );
      mv.visitInsn( Opcodes.ICONST_1 );
      mv.visitLabel( nay );
    } else if ( expression.getType().isInt() ) {
      mv.visitInsn( Opcodes.ICONST_M1 );
      mv.visitInsn( Opcodes.IXOR );
    } else if ( expression.getType().isLong() ) {
      mv.visitLdcInsn( Long.valueOf( -1 ) );
      mv.visitInsn( Opcodes.LXOR );
    } else {
      throw new IllegalStateException( "Cannot compile a not expression that operates on type " + expression.getType().getName() );
    }
  }
}
