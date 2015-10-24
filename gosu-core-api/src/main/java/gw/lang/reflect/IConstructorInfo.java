/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface IConstructorInfo extends IAttributedFeatureInfo, IHasParameterInfos
{
  public IType getType();

  public IParameterInfo[] getParameters();

  public IConstructorHandler getConstructor();

  public List<IExceptionInfo> getExceptions();

  boolean isDefault();

  default boolean hasRawConstructor( IConstructorInfo rawCtor )
  {
    return rawCtor == this;
  }
}
