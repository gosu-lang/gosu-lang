/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

import gw.lang.parser.ISymbolTable;

import java.io.Writer;
import java.io.Reader;

public interface ITemplateHost
{
  void pushScope();

  void popScope();

  void putSymbol( String name, Class type, Object value );

  ITemplateGenerator getTemplate( Reader templateReader );
  ITemplateGenerator getTemplate( Reader templateReader, String strFqn );

  void executeTemplate( ITemplateGenerator template, Writer writer );
  void executeTemplate( Reader inputStreamReader, Writer resultsStr );
  void executeTemplate( Reader inputStreamReader, Writer resultsStr, boolean strict);

  ISymbolTable getSymbolTable();
}
