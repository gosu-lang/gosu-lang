package gw.specContrib.expressions.cast

uses java.lang.Cloneable

class Errant_TypeCastInterfaceClass {
  interface MyInterface {
  }

  class MyClass {
  }

  var myInterface: MyInterface
  var myClass: MyClass
  var cloneable : Cloneable

  function testAssignability() {
    var a111: MyInterface = myClass      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.GENERICS.GENERICSASSIGNMENT.INTERFACES.ERRANT_ASSIGNABILITYCASTABILITYINTERFACECLASS.MYCLASS', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.GENERICS.GENERICSASSIGNMENT.INTERFACES.ERRANT_ASSIGNABILITYCASTABILITYINTERFACECLASS.MYINTERFACE'
    var b111: MyClass = myInterface      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.GENERICS.GENERICSASSIGNMENT.INTERFACES.ERRANT_ASSIGNABILITYCASTABILITYINTERFACECLASS.MYINTERFACE', REQUIRED: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.GENERICS.GENERICSASSIGNMENT.INTERFACES.ERRANT_ASSIGNABILITYCASTABILITYINTERFACECLASS.MYCLASS'
  }

  function testCastability() {
    var a111: MyInterface = myClass as MyInterface
    var a112: MyInterface = cloneable as MyInterface

    var b111: MyClass = myInterface as MyClass
    var b112: MyClass = cloneable as MyClass

    var c111: Cloneable = myClass as Cloneable
    var c112: Cloneable = myInterface as Cloneable
  }
}