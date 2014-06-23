/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRThrowStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRThrowStatementCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRThrowStatement statement, IRBytecodeContext context ) {

    IRBytecodeCompiler.compileIRExpression( statement.getException(), context );

    context.getMv().visitInsn( Opcodes.ATHROW );
  }
}
