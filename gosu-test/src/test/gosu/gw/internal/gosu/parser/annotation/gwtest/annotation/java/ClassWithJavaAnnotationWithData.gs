package gw.internal.gosu.parser.annotation.gwtest.annotation.java

uses gw.internal.gosu.parser.annotation.*

@TestClassAnnotationWithArgs({
  new TestClassAnnotationWithArgsEndpoint("n1", "wsdl1"),
  new TestClassAnnotationWithArgsEndpoint(Http, 1, "n1", "p1", "u1", "wsdl1")
  })
class ClassWithJavaAnnotationWithData {
}
