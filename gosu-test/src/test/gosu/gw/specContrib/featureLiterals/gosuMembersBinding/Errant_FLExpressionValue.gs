package gw.specContrib.featureLiterals.gosuMembersBinding

uses gw.lang.reflect.features.*

uses java.lang.Integer

class Errant_FLExpressionValue {
  var gInstance: Errant_FLExpressionValue
  var _prop1: String as Prop1
  var FLEValue : Errant_FLExpressionValue

  var _prop2: String as Prop2
  var propFL2 = #Prop2

  construct(str : String) {}

  static class Address {
    var _prop2 : Integer as ZipCode
    function showZipCode() : Integer { return null }
  }
  function getAddress(): Address {
    print("getAddress()")
    return new Address()
  }

  static function staticFunction1(str: String, i: int): String {
    return null
  }
  static function staticFunction2() {}

  var ffff = #staticFunction1(String, int)

  function nonStaticFunction1(str: String, i: int): String {
    return null
  }

  //Case 1: Unbound Method FL with NO bound parameters and referes to a static method
  //IDE-1568
  var exprStaticFun111: MethodReference<Errant_FLExpressionValue, block(str: String, i: int): String> = Errant_FLExpressionValue#staticFunction1()
  var exprStaticFun112: MethodReference<Errant_FLExpressionValue, block(str: String, i: int): String> = Errant_FLExpressionValue#staticFunction1(String, int)
  //IDE-1586 - Parser issue. OS Gosu fine
  var exprStaticFun211: MethodReference<Errant_FLExpressionValue, block():void> = #staticFunction2()

  //Case 2: Unbound Method FL with NO bound parameters and referes to a non-static method
  var exprNonStatic211: MethodReference<Errant_FLExpressionValue, block(r: Errant_FLExpressionValue, str: String, i: int): String> = Errant_FLExpressionValue#nonStaticFunction1(String, int)

  //Case 3: Unbound Method FL with bound parameters and referes to a static method
  var exprStaticBoundParams311: MethodReference<Errant_FLExpressionValue, block(): String> = Errant_FLExpressionValue#staticFunction1("mystring", 42)

  //Case 4: Unbound Method FL with bound parameters and referes to a non-static method
  var exprNonStatic411: MethodReference<Errant_FLExpressionValue, block(r: Errant_FLExpressionValue): String> = Errant_FLExpressionValue#nonStaticFunction1("mystring", 42)

  //Bound Method FLs
  //Case 5.1: Bound Method FL with NO bound parameters and referes to a NON-STATIC method
  var exprNonStatic511: BoundMethodReference<Errant_FLExpressionValue, block(str: String, i: int): String> = gInstance#nonStaticFunction1(String, int)

  //Case 6: Bound Method FL with bound parameters and referes to a non-static method
  var exprNonStatic611: BoundMethodReference<Errant_FLExpressionValue, block(): String> = gInstance#nonStaticFunction1("mystring", 42)

  //PROPERTY FEATURE LITERALS

  //Case 7: Unbounded Property Feature Literal
  var expProp711 : PropertyReference<Errant_FLExpressionValue, String> = Errant_FLExpressionValue#Prop1

  //Case 8: Bounded Property Feature Literal
  var expProp811 : BoundPropertyReference<Errant_FLExpressionValue, String> = gInstance#Prop1

  //Case 9: Constructor Feature Literal with NO bound parameters
  //IDE-1571 - OS Gosu Issue. Parser works fine
  var expCons911 : ConstructorReference<Errant_FLExpressionValue, block(str : String): Errant_FLExpressionValue> = Errant_FLExpressionValue#construct()
  var expCons912 : ConstructorReference<Errant_FLExpressionValue, block(str : String): Errant_FLExpressionValue> = Errant_FLExpressionValue#construct(String)

  //Case 10: Constructor Feature Literal with bound parameters
  //IDE-1571 - OS Gosu Issue. Parser works fine
  var expCons1011 : ConstructorReference<Errant_FLExpressionValue, block(): Errant_FLExpressionValue> = Errant_FLExpressionValue#construct("mystring")
  var expCons1012 : ConstructorReference<Errant_FLExpressionValue, block(): Errant_FLExpressionValue> = #construct("mystring")

  //Case 11: Feature Literal Chain consisting only of Property Feature Literals
  var expChainedProp1111 : SimplePropertyChainReference<Errant_FLExpressionValue, String> = Errant_FLExpressionValue#FLEValue#Prop1

  //Case 12: BOUND Feature Literal Chain consisting only of Property Feature Literals
  var expChainedProp1211 : BoundSimplePropertyChainReference<Errant_FLExpressionValue, String> = gInstance#FLEValue#Prop1

  //Case 13 : Feature Literal Chain which consists not only of Propery Feature Literals and ends with a Property Feature Literal
  var expChainedComplex1311 : ComplexPropertyChainReference<Errant_FLExpressionValue, Integer> = Errant_FLExpressionValue#getAddress()#ZipCode

  //Case 14 : Bound Feature Literal Chain which consists not only of Property Feature Literals and ends with a Method Feature Literal
  var expChaingedComplex1411 : gw.lang.reflect.features.MethodChainReference<Errant_FLExpressionValue, block(String, int):String> = gInstance#FLEValue#nonStaticFunction1()

  //The following case is not in spec, so adding one for unbound MethodChainReference
  //Case 14.2 : UNBound Feature Literal Chain which consists not only of Property Feature Literals and ends with a Method Feature Literal
  //IDE-1602 - Parser Issue
  var expChaingedComplex1421 : MethodChainReference<Errant_FLExpressionValue, block(Errant_FLExpressionValue, String, int):String> = Errant_FLExpressionValue#FLEValue#nonStaticFunction1()
  //IDE-1602 - Parser Issue
  var expChaingedComplex1422 : MethodChainReference<Errant_FLExpressionValue, block(Errant_FLExpressionValue):Integer> = Errant_FLExpressionValue#getAddress()#showZipCode()

  //According to current spec, Case #15 is identical to Case #13 so making it bound
  //Case 15 : Feature Literal Chain consisting not only of Property Feature Literals and ending with a Property Feature Literal
  var expChainedComplex1511 : BoundComplexPropertyChainReference<Errant_FLExpressionValue, Integer> = gInstance#getAddress()#ZipCode




}