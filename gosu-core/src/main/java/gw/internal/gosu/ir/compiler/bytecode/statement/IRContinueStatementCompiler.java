/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.statement.IRContinueStatement;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRContinueStatementCompiler extends AbstractBytecodeCompiler
{
  public static void compile( IRContinueStatement breakStmt, IRBytecodeContext context )
  {
    context.inlineFinallyStatements( breakStmt );
    context.getMv().visitJumpInsn( Opcodes.GOTO, context.getCurrentContinueLabel() );
  }
}
