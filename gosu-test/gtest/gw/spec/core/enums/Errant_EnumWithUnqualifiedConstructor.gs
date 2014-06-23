package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithUnqualifiedConstructor {
  RED, GREEN, BLUE

  construct() { }
}
