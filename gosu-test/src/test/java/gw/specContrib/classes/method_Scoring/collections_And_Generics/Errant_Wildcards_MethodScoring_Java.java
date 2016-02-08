package gw.specContrib.classes.method_Scoring.collections_And_Generics;

import java.util.Collection;

class Errant_Wildcards_MethodScoring_Java {
  static class A {}
  static class B {}

  static <E> A foo(Collection<? extends E> elements) {
    return null;
  }
  static <E> B foo(Iterable<? extends E> elements) {
    return null;
  }
}