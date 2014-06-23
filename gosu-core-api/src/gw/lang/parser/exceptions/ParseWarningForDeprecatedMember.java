/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.Res;
import gw.lang.parser.IParserState;

public class ParseWarningForDeprecatedMember extends ParseWarning
{
  public ParseWarningForDeprecatedMember( IParserState state, String featureName, String featureContainerName)
  {
    super( state, Res.MSG_DEPRECATED_MEMBER, featureName.intern(), featureContainerName.intern() );
  }
}