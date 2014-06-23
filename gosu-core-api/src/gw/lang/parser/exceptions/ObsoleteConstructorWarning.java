/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;

public class ObsoleteConstructorWarning extends ParseWarning
{
  public ObsoleteConstructorWarning( IParserState standardParserState, ResourceKey msgObsoleteCtorSyntax )
  {
    super( standardParserState, msgObsoleteCtorSyntax );
  }
}
