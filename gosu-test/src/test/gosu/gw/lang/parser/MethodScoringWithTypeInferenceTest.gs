package gw.lang.parser

uses gw.test.TestClass

class MethodScoringWithTypeInferenceTest extends TestClass {

  private function foo(o1 : Object, o2 : Object) : boolean {
    print( "MethodScoringWithTypeInferenceTest#foo(Object, Object)" )
    return true
  }

  private function foo(s1 : String, s2 : String) : boolean {
    print( "MethodScoringWithTypeInferenceTest#foo(String, String)" )
    return false
  }

  reified private function inferReturnType<U>() : U {
    return new U()
  }

  function testParserMethodScoringInfersReturnTypeCorrectly() {

    //try once; should execute foo( Object, Object )
    assertTrue(foo("", new Object()))

    //try again; should execute foo(String, String)
    assertFalse(foo("", inferReturnType()))

  }

}