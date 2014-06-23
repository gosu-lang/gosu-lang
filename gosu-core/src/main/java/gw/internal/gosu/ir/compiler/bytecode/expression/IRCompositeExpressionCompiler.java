/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRCompositeExpression;
import gw.lang.ir.IRElement;

public class IRCompositeExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRCompositeExpression expression, IRBytecodeContext context) {
    // Composite expressions can create temp variables, but should never result in something like
    // an assignment to a non-temp variable, so it's safe to push and pop scopes during
    // their compilation
    context.pushScope();
    try {
      for (IRElement element : expression.getElements()) {
        IRBytecodeCompiler.compileIRElement( element, context );
      }
    } finally {
      context.popScope();
    }
  }
}
