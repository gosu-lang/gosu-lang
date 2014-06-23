package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithBadConstant {
  RED, GREEN("foo", 1), BLUE
}
