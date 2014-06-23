/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.statement.IRBreakStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRBreakStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IRBreakStatement breakStmt, IRBytecodeContext context )
  {
    context.inlineFinallyStatements( breakStmt );
    context.getMv().visitJumpInsn( Opcodes.GOTO, context.getCurrentBreakLabel() );
  }
}
