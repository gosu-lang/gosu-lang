/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.lang.ir.statement.IRArrayStoreStatement;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRArrayStoreStatementCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRArrayStoreStatement statement, IRBytecodeContext context ) {

    IRBytecodeCompiler.compileIRExpression( statement.getTarget(), context );
    IRBytecodeCompiler.compileIRExpression( statement.getIndex(), context );
    IRBytecodeCompiler.compileIRExpression( statement.getValue(), context );
    
    context.getMv().visitInsn( getIns( Opcodes.IASTORE, statement.getComponentType() ) );
  }
}
