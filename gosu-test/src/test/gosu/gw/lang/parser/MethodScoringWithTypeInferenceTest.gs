package gw.lang.parser

uses gw.test.TestClass

class MethodScoringWithTypeInferenceTest extends TestClass {

  private var _fooObjObjCtr : int as FooObjectObjectCounter = 0
  private var _fooStrStrCtr : int as FooStringStringCounter = 0

  private function foo(o1 : Object, o2 : Object) {
    print( "MethodScoringWithTypeInferenceTest#foo(Object, Object)" )
    FooObjectObjectCounter++
  }

  private function foo(s1 : String, s2 : String) {
    print( "MethodScoringWithTypeInferenceTest#foo(String, String)" )
    FooStringStringCounter++
  }

  private function inferReturnType<U>() : U {
    return new U()
  }

  function testParserMethodScoringInfersReturnTypeCorrectly() {

    //try once; should execute foo( Object, Object )
    foo("", new Object())

    //try again; should execute foo(String, String)
    foo("", inferReturnType())

    assertEquals(1, FooObjectObjectCounter)
    assertEquals(1, FooStringStringCounter)

  }

}