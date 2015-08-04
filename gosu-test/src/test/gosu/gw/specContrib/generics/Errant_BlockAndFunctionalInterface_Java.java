package gw.specContrib.generics;

public class Errant_BlockAndFunctionalInterface_Java {
  static interface Fun1<T> {
    void fun(T p);
  }

  static interface Fun2<T> {
    T fun();
  }

  static interface I {}

  static <K extends I> void acceptFun1(Fun1<K> p) {}
  static <K extends I> void acceptFun2(Fun2<K> p) {}
}