/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.IExpression;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IAssertStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.TerminalType;

public final class AssertStatement extends Statement implements IAssertStatement
{
  private IExpression _condition;
  private IExpression _detail;

  @Override
  public IExpression getCondition()
  {
    return _condition;
  }
  public void setCondition( IExpression condition )
  {
    _condition = condition;
  }

  @Override
  public IExpression getDetail()
  {
    return _detail;
  }
  public void setDetail( IExpression detail )
  {
    _detail = detail;
  }

  @Override
  public String toString()
  {
    return Keyword.KW_assert + " " + getCondition() + (getDetail() == null ? "" : (" : " + getDetail()));
  }

  @Override
  public TerminalType getTerminalType() {
    return TerminalType.ReturnOrThrow;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
    return this;
  }
}
