package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass

class Errant_GosuSub_TypeDisagreement_5 extends Errant_JavaSuper_TypeDisagreement_5 {
  //Case 5 : Conflict check with disagreement on type

  //IDE-1818 - Since the generated methods have different arguments because of only setter. It is good.
  // No error expected
  var list1 : String as MyProperty1

  property set MyProperty2(a : String){}

  var list3 : String as MyProperty3      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY

  property get MyProperty4() : String { return null }      //## issuekeys: 'GETMYPROPERTY4()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUSUBCLASS.ERRANT_GOSUSUB_TYPEDISAGREEMENT_51' CLASHES WITH 'GETMYPROPERTY4()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUSUBCLASS.ERRANT_JAVASUPER_TYPEDISAGREEMENT_51'; ATTEMPTING TO USE INCOMPATIBLE RETURN TYPE

  //More cases could be added for type disagreement with difference in first letter 'case' of the property name
  // but they are mostly covered without type disagreement in '4' test
  //so it is low priority and to be done only after IDE-1853 is fixed.
}