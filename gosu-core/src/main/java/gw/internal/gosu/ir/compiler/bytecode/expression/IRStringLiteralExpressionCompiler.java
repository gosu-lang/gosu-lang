/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.lang.ir.expression.IRStringLiteralExpression;

public class IRStringLiteralExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRStringLiteralExpression expression, IRBytecodeContext context) {
    context.getMv().visitLdcInsn( expression.getValue() );  
  }
}
