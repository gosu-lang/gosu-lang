/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IScriptPartId;
import gw.util.Pair;


public interface IFunctionType extends IInvocableType
{
  public IType getReturnType();

  public IType[] getParameterTypes();

  /**
   * An associated IMethodInfo. Optional.
   */
  public IMethodInfo getMethodInfo();

  public IFeatureInfo getMethodOrConstructorInfo();

  /**
   * Formatted signature of the form "<function> ( param-list )"
   */
  public String getParamSignature();
  public String getParamSignatureForCurrentModule();

  public IFunctionType inferParameterizedTypeFromArgTypesAndContextType(IType[] eArgs, IType ctxType);

  boolean areParamsCompatible( IFunctionType rhsFunctionType );

  IScriptPartId getScriptPart();

  /**
   * @return a new copy of this IFunctionType with the given parameter and return types
   */
  IType newInstance( IType[] paramTypes, IType returnType );
  Pair<Long, String> getRetainedMemory();
}
