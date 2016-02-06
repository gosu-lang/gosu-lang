package gw.specContrib.generics;

import java.util.List;

public class Errant_RawTypeWithRecursiveTypeParameterJava {
  public static class A<T extends B<T>> {
  }

  public static class B<T extends B<T>> {
    A<T> Prop;
    void foo(A<T> p) {}
  }
}