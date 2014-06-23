/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRInstanceOfExpression;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRInstanceOfExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRInstanceOfExpression expression, IRBytecodeContext context ) {
    context.compile( expression.getRoot() );

    String name = expression.getTestType().isArray() ? expression.getTestType().getDescriptor() : expression.getTestType().getSlashName();
    context.getMv().visitTypeInsn( Opcodes.INSTANCEOF, name );
  }
}
