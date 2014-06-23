package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithProtectedConstructor {
  RED, GREEN, BLUE

  protected construct() { }
}
