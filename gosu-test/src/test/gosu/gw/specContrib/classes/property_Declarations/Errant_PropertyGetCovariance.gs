package gw.specContrib.classes.property_Declarations

uses java.lang.Integer
uses java.lang.IllegalArgumentException

class Errant_PropertyGetCovariance {

  static class Base {
    var _a : java.lang.Number as A
  }

  static class Sub extends Base {
    override property get A() : Integer {
      return super.A as Integer
    }

    override property set A(a : java.lang.Number)  {
      if(a typeis Integer) {
        super.A = a
      } else {
        throw new IllegalArgumentException()
      }
    }

  }
}