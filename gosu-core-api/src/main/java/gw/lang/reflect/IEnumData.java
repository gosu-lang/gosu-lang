package gw.lang.reflect;

import java.util.List;

public interface IEnumData {
  List<IEnumValue> getEnumValues();

  IEnumValue getEnumValue(String strName);

  List<String> getEnumConstants();
}
