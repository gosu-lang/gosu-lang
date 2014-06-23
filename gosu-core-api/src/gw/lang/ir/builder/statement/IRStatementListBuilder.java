/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder.statement;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRStatement;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRStatementBuilder;
import gw.lang.ir.statement.IRStatementList;

import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class IRStatementListBuilder extends IRStatementBuilder {

  private List<IRStatementBuilder> _statements;

  public IRStatementListBuilder(List<IRStatementBuilder> statements) {
    _statements = statements;
  }

  @Override
  protected IRStatement buildImpl(IRBuilderContext context) {
    List<IRStatement> statements = new ArrayList<IRStatement>();
    for ( IRStatementBuilder statement : _statements ) {
      statements.add(statement.build(context));
    }
    return new IRStatementList(false, statements);
  }

  

}
