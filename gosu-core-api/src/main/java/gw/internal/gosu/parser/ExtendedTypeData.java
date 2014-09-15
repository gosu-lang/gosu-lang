package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaType;

public class ExtendedTypeData {

  private final Object _data;
  private final Class<?> _extensionInterface;

  private ExtendedTypeData(Object data, Class<?> extensionInterface) {
    if (data == null) {
      throw new NullPointerException();
    }
    if (extensionInterface == null) {
      throw new NullPointerException();
    }
    _data = data;
    _extensionInterface = extensionInterface;
  }

  public static ExtendedTypeData newInstance(Object data, Class<?> extensionInterface) {
    return new ExtendedTypeData(data, extensionInterface);
  }

  public Object getData() {
    return _data;
  }

  public Class<?> getExtensionInterface() {
    return _extensionInterface;
  }
}
