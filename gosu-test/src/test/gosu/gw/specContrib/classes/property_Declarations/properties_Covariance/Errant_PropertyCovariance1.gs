package gw.specContrib.classes.property_Declarations.properties_Covariance

uses java.lang.Integer
uses java.lang.Float

/**
 * Positive case of covariance in properties
 * The property getter returns more specific type in the subclass
 * The property setter argument is supertype of the getter
 */

class Errant_PropertyCovariance1 {

  interface IHasNum {
    property get Num1() : java.lang.Number
    property set Num1(value:java.lang.Number )
  }

  class Impl implements IHasNum {
    var _value : Integer

    property get Num1() : Integer {
      return _value
    }
    property set Num1(value:java.lang.Number ) {
      _value = value.intValue()
    }
  }

  function test2() {
    var impl : IHasNum = new Impl()
    impl.Num1 = new Float( 9 )

  }



}