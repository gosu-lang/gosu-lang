/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.IType;

/**
 */
public interface IGosuEnhancementInternal extends IGosuEnhancement, IGosuClassInternal
{
  void setEnhancedType( IType enhancedType );

  void setFoundCorrectHeader();
}
