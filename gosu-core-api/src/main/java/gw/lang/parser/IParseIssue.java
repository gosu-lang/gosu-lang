/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IType;

public interface IParseIssue
{
  /**
   * @return the parsed element that this issue is associated with
   */
  IParsedElement getSource();

  /**
   * @return the message for this parse issue formatted for printing out to a console
   */
  String getConsoleMessage();

  /**
   * @return the raw message for this parse issue, with no formatting
   */
  String getPlainMessage();

  /**
   * @return the message formatted for use by an IDE
   */
  String getUIMessage();

  /**
   * Returns true if this issue is relevant to the given position
   */
  boolean appliesToPosition( int iPos );

  /**
   * @return the line that this issue is on
   */
  int getLine();


  /**
   * @return the symbol table state at the creation of this issue.  Can return null if no symbol table is present.
   */
  ISymbolTable getSymbolTable();

  /**
   * @return the resource key for this ParseIssue, which can be used as a kind of identifier for the
   *         type of issue.
   */
  ResourceKey getMessageKey();

  void resolve( IParserPart scriptPart );

  void resetPositions();

  Integer getTokenStart();

  Integer getTokenEnd();

  void printStackTrace();

  Object[] getMessageArgs();

  IType getExpectedType();

  void setExpectedType(IType argType);

  int getColumn();
}