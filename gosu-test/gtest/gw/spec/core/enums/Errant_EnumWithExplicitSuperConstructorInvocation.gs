package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithExplicitSuperConstructorInvocation {
  RED, GREEN, BLUE

  private construct() {
    super("foo", 0)
  }
}
