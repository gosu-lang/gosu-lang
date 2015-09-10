/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IParserState
{
  /**
    * @return the line number of this parser state
    */
  int getLineNumber();

  /**
    * @return the column represented by this parser state
    */
  int getTokenColumn();

  /**
    * @return the program source associated with this parser state
    */
  String getSource();

  /**
    * @return the start of the token represented by this parser state
    */
  int getTokenStart();

  /**
    * @return the end of the token represented by this parser state
    */
  int getTokenEnd();

  /**
    * @return the starting line of this parser state
    */
  int getLineOffset();

  IParserState cloneWithNewTokenStartAndTokenEnd(int newTokenStart, int newLength);
}
