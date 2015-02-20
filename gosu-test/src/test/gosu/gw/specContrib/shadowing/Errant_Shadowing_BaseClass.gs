package gw.specContrib.shadowing
/**
 Test Shadowing
 field, property, if, for, while, switch, blocks, method params, methods with default params
 static field, static method, static variable
 */
class Errant_Shadowing_BaseClass {


  var baseVar : String as property123
  var baseInt = 55;
  var baseStr = "hello"
  public var baseSubVar : String

  //Property - Override in subclass and in Enhancement too
  property get baseProperty() : String {
    return "foo"
  }
  property set baseProperty(str1 : String ) {
    print("set baseProperty")
  }
  construct(){}
  public construct(baseInt : int) {            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
  }

  public construct(constructorParam : String) {
    var constructorParam = " hello"            //## issuekeys: VARIABLE 'CONSTRUCTORPARAM' IS ALREADY DEFINED IN THE SCOPE
    var baseInt = 42            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
    var baseVar = "string"            //## issuekeys: VARIABLE 'BASEVAR' IS ALREADY DEFINED IN THE SCOPE
    var property123 : String            //## issuekeys: VARIABLE 'PROPERTY123' IS ALREADY DEFINED IN THE SCOPE
  }

  function testShadowing22(baseInt : int ) {            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
  }
  function testShadowing(subParam : String) {
    var baseVar = "hello"            //## issuekeys: VARIABLE 'BASEVAR' IS ALREADY DEFINED IN THE SCOPE
    var subParam = "hello"            //## issuekeys: VARIABLE 'SUBPARAM' IS ALREADY DEFINED IN THE SCOPE

    var arr1 = new int[]{1,2,3,4}

    //For loop
    for( var baseInt in arr1) {            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
      print("hello")
    }

    //For loop. index
    for( var ele in arr1 index baseInt) {            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
      print("hello")
    }

    //while
    while(5 > 3) {
      var baseInt = 43;            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
    }

    //if
    if( 5 > 3 ) {
      var baseInt = 42;            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
    }
    switch (baseInt) {
        case 1:
        print(1)
            var baseInt = 43            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
          break;
        case 2 :
        print(2)
            break;
        default:
        print("default")
        }
  }

  var blockInt1 : Number
  var blockInt2 : Number
  //BLOCKS
  function testBlocks() {
    var block32: block(blockInt1 : Number, blockInt2 : Number) = \blockInt1: Number, blockInt2: Number -> blockInt1 + blockInt2            //## issuekeys: VARIABLE 'BLOCKINT1' IS ALREADY DEFINED IN THE SCOPE
  }

  //Method params with same name with default values
  function namedParams(int2 : int, str2 : String, baseInt : int ,  baseVar : String = "sdfsd") {            //## issuekeys: VARIABLE 'BASEINT' IS ALREADY DEFINED IN THE SCOPE
  }

//STATIC TESTING
  static var staticInt : int
  public static var staticPublicInt : int
  protected static var staticProtectedInt : int
  internal static var staticInternalInt :int
  var nonStaticVar : String

  //Static function
  static function testStaticFieldsInsideStaticFunction() {
    var staticInt : int            //## issuekeys: VARIABLE 'STATICINT' IS ALREADY DEFINED IN THE SCOPE
    var staticPublicInt : int            //## issuekeys: VARIABLE 'STATICPUBLICINT' IS ALREADY DEFINED IN THE SCOPE
    var staticProtectedInt : int            //## issuekeys: VARIABLE 'STATICPROTECTEDINT' IS ALREADY DEFINED IN THE SCOPE
    var staticInternalInt :int            //## issuekeys: VARIABLE 'STATICINTERNALINT' IS ALREADY DEFINED IN THE SCOPE
    var nonStaticVar : String
  }

  //Non Static function
  function testStaticFieldsInsideNonStaticFunction() {
    var staticInt : int            //## issuekeys: VARIABLE 'STATICINT' IS ALREADY DEFINED IN THE SCOPE
    var staticPublicInt : int            //## issuekeys: VARIABLE 'STATICPUBLICINT' IS ALREADY DEFINED IN THE SCOPE
    var staticProtectedInt : int            //## issuekeys: VARIABLE 'STATICPROTECTEDINT' IS ALREADY DEFINED IN THE SCOPE
    var staticInternalInt :int            //## issuekeys: VARIABLE 'STATICINTERNALINT' IS ALREADY DEFINED IN THE SCOPE
    var nonStaticVar : String            //## issuekeys: VARIABLE 'NONSTATICVAR' IS ALREADY DEFINED IN THE SCOPE
  }
}