package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithExplicitInstanceConstruction {
  RED, GREEN, BLUE

  private construct() { }

  function doStuff() : Object {
    return new Errant_EnumWithExplicitInstanceConstruction()
  }
}
