/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public interface IEnumType extends IType
{
  public List<IEnumValue> getEnumValues();

  public IEnumValue getEnumValue( String strName );

  List<String> getEnumConstants();
}
