package gw.specContrib.generics

uses java.lang.Integer
uses java.lang.CharSequence

class Errant_InheritWithDiffArgTypes {
  interface A<T> {
  }

  interface AA extends A<CharSequence> {}

  interface AAA extends A<String> {}

  static abstract class B implements A<String> {
  }

  static abstract class C1 implements A<Integer>, A<String> {}  //## issuekeys: MSG_INHEREITED_WITH_DIFF_ARG_TYPES

  static class C2 extends B implements A<Integer> {}  //## issuekeys: MSG_INHEREITED_WITH_DIFF_ARG_TYPES

  static abstract class C3 extends B implements AA {}  //## issuekeys: MSG_INHEREITED_WITH_DIFF_ARG_TYPES

  static abstract class C4 extends B implements AAA {}
}
