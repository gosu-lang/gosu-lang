package gw.lang.spec_old.expressions

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ReadAccessToWriteOnlyPropertyFromDpsFailsWithParseException extends HasWriteOnlyProperty  {

  function test() {
    print( WriteOnly )
  }

}
