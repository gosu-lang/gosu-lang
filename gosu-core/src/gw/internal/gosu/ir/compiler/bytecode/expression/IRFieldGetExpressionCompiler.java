/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRFieldGetExpression;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRFieldGetExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRFieldGetExpression expression, IRBytecodeContext context ) {
    int opCode;
    if ( expression.getLhs() != null ) {
      IRBytecodeCompiler.compileIRExpression( expression.getLhs(), context );
      opCode = Opcodes.GETFIELD;
    } else {
      opCode = Opcodes.GETSTATIC;
    }

    context.getMv().visitFieldInsn( opCode,
                                    expression.getOwnersType().getSlashName(),
                                    expression.getName(),
                                    expression.getFieldType().getDescriptor() );  
  }
}
