package gw.specContrib.expressions

uses java.lang.Math
uses gw.test.TestClass

class TernaryContribTest extends TestClass {

  function testTernaryCommutativityWrtBoxing() {
    var r1 = true ? true : null
    var r3 = true ? null : true
    var r5 = true ? true : false
    assertEquals( Boolean, statictypeof r1 )
    assertEquals( Boolean, statictypeof r3 )
    assertEquals( boolean, statictypeof r5 )
    
    var r2 = true ? true : Boolean.TRUE
    var r4 = true ? Boolean.TRUE : true
    var r6 = true ? Boolean.TRUE : Boolean.FALSE
    assertEquals( Boolean, statictypeof r2)
    assertEquals( Boolean, statictypeof r4 )
    assertEquals( Boolean, statictypeof r6 )
    
    var r8 = true ? Boolean.TRUE : null
    var r10 = true ? null : Boolean.TRUE
    assertEquals( Boolean, statictypeof r8 )
    assertEquals( Boolean, statictypeof r10 )
  }
}