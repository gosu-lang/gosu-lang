/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRArrayLoadExpression;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRArrayLoadExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRArrayLoadExpression expression, IRBytecodeContext context ) {
    IRBytecodeCompiler.compileIRExpression( expression.getRoot(), context );
    IRBytecodeCompiler.compileIRExpression( expression.getIndex(), context );
    
    context.getMv().visitInsn( getIns( Opcodes.IALOAD, expression.getComponentType() ) );
  }
}
