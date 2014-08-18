package gw.specification.temp.generic_signature;

import java.lang.reflect.Type;

public class BaseGenericClass<T> {
  private Type _type;

  protected BaseGenericClass() {
    _type = getClass().getGenericSuperclass();
  }

  public Type getType() {
    return _type;
  }
}
