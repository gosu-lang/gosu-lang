/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.statement;

import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRStatement;
import gw.lang.UnstableAPI;

@UnstableAPI
public class IRCatchClause {
  private IRSymbol _identifier;
  private IRStatement _body;

  public IRCatchClause(IRSymbol identifier, IRStatement body) {
    _identifier = identifier;
    _body = body;
    // Note that the body of the catch clause gets its parent set to the enclosing
    // IRTryCatchFinallyStatement, rather than the catch clause, which isn't itself really a statement
  }

  public IRSymbol getIdentifier() {
    return _identifier;
  }

  public IRStatement getBody() {
    return _body;
  }
}
