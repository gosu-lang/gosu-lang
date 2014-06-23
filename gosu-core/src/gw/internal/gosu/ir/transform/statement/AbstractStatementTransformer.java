/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRStatement;
import gw.lang.parser.IStatement;

/**
 */
public abstract class AbstractStatementTransformer<T extends IStatement> extends AbstractElementTransformer<T> {
  public AbstractStatementTransformer( TopLevelTransformationContext cc, T parsedElem ) {
    super( cc, parsedElem );
  }

  public T _stmt() {
    return getParsedElement();
  }

  protected final IRStatement compile() {
    IRStatement irStmt = compile_impl();
    visitStatementLineNumber( irStmt );

    return irStmt;
  }

  protected abstract IRStatement compile_impl();

  protected void visitStatementLineNumber( IRStatement irStatement ) {
    visitStatementLineNumber( irStatement, _stmt() );
  }

  protected void visitStatementLineNumber( IRStatement irStatement, IStatement stmt ) {
    if( irStatement != null ) {
      int lineNumber = stmt.getLineNum();
      irStatement.setLineNumber( lineNumber );

      //## todo: this should be turned on only in cases where it's needed
      if( lineNumber > 0 ) {
        irStatement.setOriginalSourceStatement( _cc().getSourceFileRef() + ":" + lineNumber + " " + _cc().getSourceLine( lineNumber ) );
      }
    }
  }
}
