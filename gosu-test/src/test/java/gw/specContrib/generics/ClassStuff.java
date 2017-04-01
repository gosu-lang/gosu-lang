package gw.specContrib.generics;

public class ClassStuff {
  public static <T> String forClass(Class<T> ruleClass) {
    return ruleClass.getName();
  }
}