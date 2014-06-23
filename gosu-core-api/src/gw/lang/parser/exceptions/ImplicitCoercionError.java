/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;
import gw.lang.reflect.IType;

public class ImplicitCoercionError extends ParseException implements ICoercionIssue
{
  private IType _typeToCoerceTo;

  public ImplicitCoercionError( IParserState standardParserState,
                                  ResourceKey msgImplicitCoercionWarning,
                                  IType typeToCoerceTo,
                                  Object... args )
  {
    super( standardParserState, msgImplicitCoercionWarning, args );
    _typeToCoerceTo = typeToCoerceTo;
  }

  public IType getTypeToCoerceTo()
  {
    return _typeToCoerceTo;
  }
}