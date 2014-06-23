/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface IMethodInfo extends IAttributedFeatureInfo, IHasParameterInfos
{
  public IParameterInfo[] getParameters();

  public IType getReturnType();

  public IMethodCallHandler getCallHandler();

  public String getReturnDescription();

  public List<IExceptionInfo> getExceptions();

  String getName();
}
