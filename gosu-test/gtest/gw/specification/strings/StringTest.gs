package gw.specification.strings

uses gw.BaseVerifyErrantTest

class StringTest extends BaseVerifyErrantTest {
  class A { function toString() : String { return "AAA"}}


  function testErrant_StringTest() {
    processErrantType(Errant_StringTest)
  }

  function testToStringIdentityAndConcatenation() {
    var s1 = "foo"
    var s2 = s1 + ""
    assertFalse(s1 === s2)
    assertTrue(s1.equals(s2))
    var s3 = s1
    assertTrue(s1 === s3)
    assertTrue(s1.equals(s3))
    var s4 = s1.toString()
    assertTrue(s1 === s4)
    assertTrue(s1.equals(s4))
    assertTrue((10 + 25 + "A").equals("35A"))
    assertTrue(("A" + 10 + 25).equals("A1025"))

    var s5 = "bar"
    assertTrue((s1+s2).equals(s1.concat(s2)))
    var x = 3
    var a = new A()
    assertTrue(((""+x+a).equals(String.valueOf(x).concat(a.toString()))))

  }

  function testEscapes() {
    var escapes = "\b\t\n\f\r\v\a\$\<\"\'\\".toCharArray()
    var hex : int[] = {0x08, 0x09, 0x0A, 0x0C, 0x0D, 0x0B, 0x07, '$', '<', '"', 0x27, 0x5C }

    var i = 0
    while(i != escapes.length) {
      print(escapes[i] == hex[i] + " " + i )
      i++
    }
    assertEquals("A\101\u0041", "AAA")
  }
}
