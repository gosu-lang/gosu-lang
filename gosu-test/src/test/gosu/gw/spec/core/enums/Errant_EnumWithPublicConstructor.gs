package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithPublicConstructor {
  RED, GREEN, BLUE

  public construct() { }
}
