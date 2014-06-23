/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.lang.parser.expressions.IDelegateStatement;
import gw.lang.reflect.IType;

import java.util.List;

/**
 */
public class DelegateStatement extends VarStatement implements IDelegateStatement
{
  private List<IType> _constituents;
  
  public DelegateStatement()
  {
  }

  public List<IType> getConstituents()
  {
    return _constituents;
  }

  public void setConstituents( List<IType> constituents )
  {
    _constituents = constituents;
  }

  @Override
  public boolean isImplicitlyUsed() {
    return true;
  }
}
