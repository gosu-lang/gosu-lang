/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRIfStatement;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRIfStatementCompiler extends AbstractBytecodeCompiler {
  public static void compile(IRIfStatement statement, IRBytecodeContext context) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( statement.getExpression(), context );

    Label afterIf = new Label();
    mv.visitJumpInsn( Opcodes.IFEQ, afterIf );
    IRBytecodeCompiler.compileIRStatement( statement.getIfStatement(), context );
    if( statement.getElseStatement() != null )
    {
      Label afterElse = new Label();
      boolean bTerminal = statement.getLeastSignificantTerminalStatement() != null;
      if( !bTerminal )
      {
        mv.visitJumpInsn( Opcodes.GOTO, afterElse );
      }
      mv.visitLabel( afterIf );

      IRBytecodeCompiler.compileIRStatement( statement.getElseStatement(), context );

      if( !bTerminal )
      {
        mv.visitLabel( afterElse );
      }
    }
    else
    {
      mv.visitLabel( afterIf );
    }
  }
}
