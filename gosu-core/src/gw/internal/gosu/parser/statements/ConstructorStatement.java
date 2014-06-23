/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.lang.parser.statements.IConstructorStatement;

public class ConstructorStatement extends FunctionStatement implements IConstructorStatement {
  private boolean _bConstructKeyword;
  
  public ConstructorStatement( boolean bConstructKeyword ) {
    _bConstructKeyword = bConstructKeyword;    
  }

  @Override
  public boolean hasConstructKeyword() {
    return _bConstructKeyword;
  }  
}
