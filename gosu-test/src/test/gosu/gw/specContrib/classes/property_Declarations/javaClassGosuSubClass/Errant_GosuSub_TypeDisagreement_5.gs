package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass

class Errant_GosuSub_TypeDisagreement_5 extends Errant_JavaSuper_TypeDisagreement_5 {
  //Case 5 : Conflict check with disagreement on type

  //IDE-1818 - Since the generated methods have different arguments because of only setter. It is good.
  // No error expected
  // update: setter with no getter is a property in java now, so this is an error
  var list1 : String as MyProperty1 //## issuekeys: MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE

  property set MyProperty2(a : String){}  //## issuekeys: MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE

  var list3 : String as MyProperty3      //## issuekeys: MSG_FUNCTION_CLASH

  property get MyProperty4() : String { return null }      //## issuekeys: MSG_FUNCTION_CLASH

  //More cases could be added for type disagreement with difference in first letter 'case' of the property name
  // but they are mostly covered without type disagreement in '4' test
  //so it is low priority and to be done only after IDE-1853 is fixed.
}
