package gw.spec.core.enums

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum Errant_EnumWithAbstractMethod {
  RED, GREEN, BLUE

  abstract function doStuff()
}
