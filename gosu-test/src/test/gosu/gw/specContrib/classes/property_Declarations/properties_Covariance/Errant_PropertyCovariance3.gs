package gw.specContrib.classes.property_Declarations.properties_Covariance

uses java.lang.Integer

/**
 * Covers case property with 'as' keyword in superclass
 */
class Errant_PropertyCovariance3 {

  class A {
    var myProp1: java.lang.Number as MyProp1

    property get MyProp2(): java.lang.Number { return 0 }
    property set MyProp2(p: java.lang.Number) {}
  }

  class B extends A {
    property get MyProp1(): Integer { return 0 }
    property set MyProp1(p: java.lang.Number) {}

    property get MyProp2(): Integer { return 0 }
    property set MyProp2(p: java.lang.Number) {}
  }

}