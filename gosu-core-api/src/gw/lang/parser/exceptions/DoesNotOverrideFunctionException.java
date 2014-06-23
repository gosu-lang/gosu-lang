/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;

public class DoesNotOverrideFunctionException extends ParseException
{
  public DoesNotOverrideFunctionException( IParserState standardParserState, ResourceKey resourceKey, CharSequence... functionName) {
    super(standardParserState, resourceKey, functionName);
  }
}
