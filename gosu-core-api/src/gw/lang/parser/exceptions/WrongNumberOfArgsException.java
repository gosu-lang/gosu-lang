/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;

public class WrongNumberOfArgsException extends ParseException
{
  public WrongNumberOfArgsException( IParserState standardParserState, ResourceKey msgWrongNumberOfArgsToFunction, String paramSignature, int expectedArgs, int iArgs) {
    super(standardParserState, msgWrongNumberOfArgsToFunction, paramSignature, expectedArgs, iArgs);
  }
}
