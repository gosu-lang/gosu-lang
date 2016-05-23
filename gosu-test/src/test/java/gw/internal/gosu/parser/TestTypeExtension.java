package gw.internal.gosu.parser;

import gw.lang.reflect.IType;

public interface TestTypeExtension extends IType {
  String intToString(int i);
  void doThrow() throws Exception;
}
