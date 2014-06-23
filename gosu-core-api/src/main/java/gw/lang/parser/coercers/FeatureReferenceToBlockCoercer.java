/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;
import gw.lang.reflect.features.BlockWrapper;
import gw.lang.reflect.features.FeatureReference;

public class FeatureReferenceToBlockCoercer extends BaseCoercer
{
  private static final FeatureReferenceToBlockCoercer INSTANCE = new FeatureReferenceToBlockCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    return BlockWrapper.toBlock((FeatureReference) value);
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return MAX_PRIORITY;
  }

  public static FeatureReferenceToBlockCoercer instance()
  {
    return INSTANCE;
  }
}
