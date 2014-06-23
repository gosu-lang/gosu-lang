package gw.internal.gosu.parser.classTests.gwtest.ctor
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadConstructors<T> {

  var _instanceVar : String as InstanceProperty
  static var _staticVar : String as StaticProperty

  construct(o : Object) {}

  construct() {this(_instanceVar)}
  construct(i:int) {this(_staticVar)}
  construct(i:int,j:int) {this(instanceMethod())}
  construct(i:int,j:int,k:int) {this(staticMethod())}
  construct(i:int,j:int,k:int,l:int) {this(InstanceProperty)}
  construct(i:int,j:int,k:int,l:int,m:int) {this(StaticProperty)}
  construct(i:int,j:int,k:int,l:int,m:int,n:int) {this(T)}

  function instanceMethod() : String { return null }
  static function staticMethod() : String { return null }
}
