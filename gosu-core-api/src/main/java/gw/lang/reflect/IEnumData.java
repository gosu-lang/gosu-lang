package gw.lang.reflect;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */


import java.util.List;

public interface IEnumData {
  List<IEnumValue> getEnumValues();

  IEnumValue getEnumValue(String strName);

  List<String> getEnumConstants();
}
