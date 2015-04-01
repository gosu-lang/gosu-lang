package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

class Errant_GosuClass_52 {
  //IDE-1818 - Since the generated methods have different arguments because of only setter. It is good. No error expected
  var list1 : String as MyProperty1

  property set MyProperty2(a : String){}

  var list3 : String as MyProperty3

  property get MyProperty4() : String { return null }
}