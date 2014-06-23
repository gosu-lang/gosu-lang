/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRNullLiteral;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRNullLiteralCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRNullLiteral expression, IRBytecodeContext context ) {
    context.getMv().visitInsn( Opcodes.ACONST_NULL );
  }
}
