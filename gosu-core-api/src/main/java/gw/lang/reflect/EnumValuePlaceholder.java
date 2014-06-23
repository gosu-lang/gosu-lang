/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.lang.reflect;

public class EnumValuePlaceholder implements IEnumValue {

  private final String _code;

  public EnumValuePlaceholder(String code) {
    this._code = code;
  }

  @Override
  public String getCode() {
    return _code;
  }

  @Override
  public String getDisplayName() {
    return _code;
  }

  @Override
  public Object getValue() {
    return _code;
//    throw new UnsupportedOperationException();
  }

  @Override
  public int getOrdinal() {
    throw new UnsupportedOperationException();
  }

  public String toString() {
    return _code;
  }

}
