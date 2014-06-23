/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IConstructorType extends IInvocableType
{
  /**
   * Returns the type being contructed.
   */
  public IType getDeclaringType();

  /**
   * The constructor's argument types.  Can be null.
   */
  public IType[] getParameterTypes();

  /**
   * An associated Constructor. Optional.
   */
  public IConstructorInfo getConstructor();

  public String getArgSignature();
}
