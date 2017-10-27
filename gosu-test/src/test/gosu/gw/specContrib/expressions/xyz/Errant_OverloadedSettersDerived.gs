package gw.specContrib.expressions.xyz

uses gw.specContrib.expressions.Errant_OverloadedSettersBase

class Errant_OverloadedSettersDerived extends Errant_OverloadedSettersBase {
  function setFoo( iter: Iterable<String> ) {}

  function asdf() {
    var x: List<String>
    setFoo( x.size() );
  }
}