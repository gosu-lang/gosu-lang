/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRBooleanLiteral;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRBooleanLiteralCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRBooleanLiteral expression, IRBytecodeContext context ) {
    context.getMv().visitInsn( expression.getValue() ? Opcodes.ICONST_1 : Opcodes.ICONST_0 );
  }
}
