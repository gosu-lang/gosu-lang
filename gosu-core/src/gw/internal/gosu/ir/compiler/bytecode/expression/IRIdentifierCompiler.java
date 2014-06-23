/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRIdentifier;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRIdentifierCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRIdentifier identifier, IRBytecodeContext context ) {
    int opcode = getIns(Opcodes.ILOAD, identifier.getType());
    context.getMv().visitVarInsn(opcode, context.getLocalVar(identifier.getSymbol()).getIndex());
  }
}
