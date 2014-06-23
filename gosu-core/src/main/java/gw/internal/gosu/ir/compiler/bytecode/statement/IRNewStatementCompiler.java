/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.IRType;
import gw.lang.ir.statement.IRNewStatement;

public class IRNewStatementCompiler extends AbstractBytecodeCompiler {
  public static void compile( IRNewStatement statement, IRBytecodeContext context ) {

    IRBytecodeCompiler.compileIRExpression( statement.getNewExpression(), context );

    IRType returnType = statement.getNewExpression().getType();
    context.getMv().visitInsn( getIns( Opcodes.POP, returnType ) );
  }
}
