package gw.specContrib.scopes

class Errant_PropertyDeclaration {
  var fLocal : int
  var Property1: int
  var f1: int as Property1     //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var f2: int as Property2
  var Property2: int          //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var Property11 = 45
  property get Property11( ) : String { return null }   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  property get Property12( ) : String { return null }
  var Property12 = 42                                  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var Zxc : int as Zxc  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_PROPERTY_ALREADY_DEFINED

  var f21: int as Property21  //## issuekeys: MSG_PROPERTY_ALREADY_DEFINED
  var f22: int as Property21             //## issuekeys: MSG_PROPERTY_ALREADY_DEFINED

  function foo(final Param: int) {
    final var Local = 2
    var o = new Object() {
      property get Local(): int { return 0 }      //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

      property get Param(): int { return 0 }      //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }

    var o2 = new Object() {
      var _local : int as Local  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var _param : int as Param  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }
  }

  function foo2() {
    var o = new Object() {
      property get fLocal(): int { return 0 }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

      property get Param(): int { return 0 }
    }

    var o2 = new Object() {
      var _local : int as fLocal  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }
  }

  var f3: int as Property3
  property get Property3(): int { return 0 }      // overriding getter for field property
  property set Property3(v: int) {}               // overriding setter for field property

  class A {
    var a: int as MyParentProp
  }

  class B extends A {
    var b: int as MyParentProp
  }

  class Hello {
    var _bar : int as Bar
    property get Foo() : int { return 0 }
    property get Bar() : int { return 1 }
    override property set Foo(x : int) { }  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE
  }

  class World extends Hello{
    override property get Foo() : int { return 2 }
    property get Bar() : int { return 3 }  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
  }

  class WorldAno {
    var _x : String as Hi

    function m()  {
      new World() {
        override property get Foo() : int { return 4 }
        property get Bar() : int { return 5 }  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
      }
    }
  }

  class PropertyShadowingFromInner {
    var y: int as X

    class Inner {
      var z: int as X
    }
  }
}