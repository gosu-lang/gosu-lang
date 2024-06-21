/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.Res;
import gw.lang.parser.IParserState;
import gw.internal.gosu.util.StringPool;

public class ParseWarningForDeprecatedMember extends ParseWarning
{
  public ParseWarningForDeprecatedMember( IParserState state, String featureName, String featureContainerName)
  {
    super( state, Res.MSG_DEPRECATED_MEMBER, StringPool.get( featureName ), StringPool.get( featureContainerName ) );
  }
}