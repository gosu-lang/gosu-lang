package gw.specification.genericTypesAndMethods.howCanTypeParametersBeUsed

uses gw.BaseVerifyErrantTest

class HowCanTypeParametersBeUsedTest extends BaseVerifyErrantTest {
  function testErrant_HowCanTypeParametersBeUsedTest() {
    processErrantType(Errant_HowCanTypeParametersBeUsedTest)
  }

  static class Y<T> {
    function array() : T[] {
      return new T[1];
    }

    function stringTypeIs() : boolean  {
      return "foo" typeis T
    }

    function stringCast() : T {
      return "" as T
    }

    function typeOfT() : Type<T> {
      return T.Type
    }

    function newInstance() : T {
      return new T()
    }

    function typeOfopT() : Type<T> {
      return typeof T
    }

    function typeOfopY() : Type<Y<T>> {
      return typeof Y<T>
    }
  }

  function testTypeParametersUsage() {
    var y = new Y<String>()
    var strs : String[] = y.array()
    strs[0] = "Hello"
    assertTrue(y.stringTypeIs())
    assertFalse(new Y<Integer>().stringTypeIs())
    try {
      y.stringCast()
    } catch( e : ClassCastException) { fail() }

    try {
      new Y<Integer>().stringCast()
      fail()
    } catch( e : ClassCastException) {  }
    assertEquals(String, y.typeOfT())
    assertEquals(typeof String, y.typeOfopT())
    assertEquals(typeof Y<String>, y.typeOfopY())
    assertTrue(y.newInstance().Empty)
  }
}