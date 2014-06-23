package gw.internal.gosu.parser.classTests.gwtest.interfaces

uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable

@DoNotVerifyResource
interface Errant_InterfaceWithImplemetation {

  var _instanceVar : String = null
  var _staticVar : String as StaticVarProp
  delegate _badDelegate represents Runnable 
  construct() {} 
  function Errant_InterfaceWithImplemetation( b : boolean ) {} 
  static function staticMethodWithImpl() {}  
  function instanceMethodWithImpl() {} 
  static function staticMethodWithImpl() {}  
  static property get StaticGetterProp() : String { return "foo" }  
  static property set StaticSetterProp( x : String ) {}
   
} 