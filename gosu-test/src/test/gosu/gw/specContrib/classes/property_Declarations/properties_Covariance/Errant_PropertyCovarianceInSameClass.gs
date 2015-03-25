package gw.specContrib.classes.property_Declarations.properties_Covariance

uses java.lang.Integer
uses java.lang.CharSequence

/**
 * Property covariance for several types
 * Getter returns type is subtype of setter argument
 */
class Errant_PropertyCovarianceInSameClass {
  property get MyProp1() : Integer { return null }
  property set MyProp1(i :java.lang.Number) {}

  property get MyProp2() :java.lang.Number { return null }      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
  property set MyProp2(i : Integer) {}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY

  property get MyStringProp1() : String { return null}
  property set MyStringProp1(s : CharSequence) {}

  property get MyStringProp2() : CharSequence { return null}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
  property set MyStringProp2(s : String) {}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY

  class A {}
  class B extends A {}
  class C extends B {}

  property get PropA() : C {return null}
  property set PropA(a : A) {}

  property get PropB() : C {return null}
  property set PropB(a : B) {}

  property get PropCNegative() : B {return null}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
  property set PropCNegative(a : C) {}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY

  var f3: java.lang.Number as MyProp3
  property get MyProp3(): Integer { return null }
  property set MyProp3(v: Object) {}
}