package gw.specContrib.classes.property_Declarations.properties_Covariance

/**
 * Negative case of covariance in properties
 * The property getter returns more specific type in the subclass - Fine
 * The property setter argument is more specific type of the type in setter in parent class - BAD
 */

uses java.lang.Integer
uses java.lang.Float

class Errant_PropertyCovariance2 {

  interface IHasNum {
    property get Num1() : java.lang.Number
    property set Num1(value:java.lang.Number )
  }

  class Impl implements IHasNum {      //## issuekeys: CLASS 'IMPL' MUST EITHER BE DECLARED ABSTRACT OR IMPLEMENT ABSTRACT METHOD 'SETNUM1(NUMBER)' IN 'IHASNUM'
    var _value : Integer

    property get Num1() : Integer {
      return _value
    }
    //Setter argument has to be supertype of getter return type. Error expected
    property set Num1(value: Integer ) {     //## issuekeys: ERROR. INCOMPATIBLE TYPES
      _value = value.intValue()
    }
  }

  function test2() {
    var impl : IHasNum = new Impl()
    impl.Num1 = new Float( 9 )

  }

  interface IHasNum2 {
    property get Num1(): java.lang.Number
    property set Num1(v: java.lang.Number)
  }

  class Impl2 implements IHasNum2 {
    var _value: Integer as Num1         //## issuekeys: ERROR. INCOMPATIBLE TYPES

    property get Num1(): Integer {
      return _value
    }
    property set Num1(v: java.lang.Number ) {
      _value = v.intValue()
    }
  }

}