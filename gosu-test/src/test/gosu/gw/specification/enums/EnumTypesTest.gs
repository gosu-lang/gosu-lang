package gw.specification.enums

uses gw.BaseVerifyErrantTest

class EnumTypesTest extends BaseVerifyErrantTest {
  function testErrant_EnumModifiersTest() {
    processErrantType(Errant_EnumModifiersTest)
  }

  function testErrant_EnumExtendsTest() {
    processErrantType(Errant_EnumExtendsTest)
  }
  
  function testErrant_EnumTypesTest() {
    processErrantType(Errant_EnumTypesTest)
  }
  
  enum mix {
    ONE(1), MIX(8)
    
    var f : int
    private construct(x : int) { 
      f = x
    }
    
//    construct() { 
//      f = 1
//    }
    
    function m() : String {
      return "hello"
    }
    
    class innerClass {}
    interface innerInterface {}
    structure innerStruture {}
    enum innerEnum {}
  }
  enum num {ONE, TWO} 
   
  function testEnumValueInstance() {
   var x0 : num = ONE
   var x1 : num = num.ONE
   assertTrue(x0 === x1)
   assertTrue(x0 !== TWO)
  }
    
  interface I0 {
    function fooI0()
  }
  
  enum num2 implements I0{
    ONE
    
    override function fooI0(){ }
  }
  
 // class B extends num {}
  
  static class C {
    var i : int
    
    static enum innerE {E2}
    enum innerE2 {
      E2 
//      function g() {
//        var f = i 
//      }
    }
  }
  
  function testEnumSuperclass(){
    var j : Enum<num> = num.ONE
  }
  
  enum o { 
    O(8)
    
    var f : int
    private construct(x : int) { 
      f = x
    }
  }
  
//  function testEnumValueCreation() {
//    new o(1);
//  }
  
  function testOrdinalValues() {
    assertTrue(num.ONE.Ordinal == 0)
    assertTrue(num.TWO.Ordinal == 1)
    //var i0 : int = num.ONE 
    //var i1 = num.ONE + 1
    //var i2 = num.ONE > 1
  }
  
  function testInference() {
    //var g0  = ONE
    var g1 : num = ONE
    switch (g1) {
      case num.ONE:
        break
      case TWO:
        break
    }
  }
}