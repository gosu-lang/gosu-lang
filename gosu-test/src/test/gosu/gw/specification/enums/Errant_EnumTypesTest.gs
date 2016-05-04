package gw.specification.enums

class Errant_EnumTypesTest {
enum mix {
    ONE(1), MIX(8)
    
    var f : int
    private construct(x : int) { 
      f = x
    }
    
    construct() {   //## issuekeys: MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE
      f = 1
    }
    
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
   var j0 = (x0 === x1)
   var j1 = (x0 !== TWO)
  }
    
  interface I0 {
    function fooI0()
  }
  
  enum num2 implements I0{
    ONE
    
    override function fooI0(){ }
  }
  
  class B extends num {}  //## issuekeys: MSG_CANNOT_EXTEND_FINAL_TYPE
  
  static class C {
    var i : int
    
    static enum innerE {E2}
    enum innerE2 {
      E2 
      function g() {
        var f = i   //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      }
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
  
  function testEnumValueCreation() {
    new o(1);  //## issuekeys: MSG_ENUM_CONSTRUCTOR_NOT_ACCESSIBLE
  }
  
  function testOrdinalValues() {
    var j0 = (num.ONE.Ordinal == 0)
    var j1 = (num.TWO.Ordinal == 1)
    var i0 : int = num.ONE   //## issuekeys: MSG_TYPE_MISMATCH
    var i1 = num.ONE + 1  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    var i2 = num.ONE > 1  //## issuekeys: MSG_TYPE_MISMATCH
  }
  
  function testInference() {
    var g0  = ONE  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    var g1 : num = ONE
    switch (g1) {
      case num.ONE:
        break
      case TWO:
        break
    }
  }
}
