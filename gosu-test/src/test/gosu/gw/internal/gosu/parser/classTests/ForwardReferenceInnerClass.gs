package gw.internal.gosu.parser.classTests

class ForwardReferenceInnerClass {
  static class Foo {
    static class Inner1 extends C2 {
      function blah() : String { return "blah" }
    }
  }
  static class C2 {}
}
