/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import java.util.List;

import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IStatement;
import gw.lang.parser.expressions.IParameterDeclaration;

public interface IFunctionStatement extends IStatement, IParsedElementWithAtLeastOneDeclaration
{
  IDynamicFunctionSymbol getDynamicFunctionSymbol();
  
  List<IParameterDeclaration> getParameters();

  default int getLastLine()
  {
    IDynamicFunctionSymbol dfs = getDynamicFunctionSymbol();
    if( dfs != null )
    {
      Object body = dfs.getValueDirectly();
      if( body instanceof IStatementList )
      {
        return ((IStatementList)body).getLastLine();
      }
    }
    return -1;
  }
}
