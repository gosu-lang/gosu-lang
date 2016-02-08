package gw.specContrib.generics;

import java.util.List;

public class Errant_RawReturnTypeJava {
  public static class JavaClass1<T> {
    public List<T> getProp() {
      return null;
    }
  }

  public static class JavaClass2<T> extends JavaClass1<T> {
    public List getProp() {
      return null;
    }
  }
}