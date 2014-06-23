/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRStatementList;
import gw.lang.ir.IRStatement;

public class IRStatementListCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRStatementList statementList, IRBytecodeContext context ) {
    if(statementList.hasScope() ) {
      context.pushScope();
    }
    try {
      for (IRStatement statement : statementList.getStatements()) {
        IRBytecodeCompiler.compileIRStatement( statement, context );
      }
    } finally {
      if(statementList.hasScope()) {
        context.popScope();
      }
    }
  }
}
