package gw.lang.spec_old.expressions

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ReadAccessToWriteOnlyPropertyFailsWithParseException {

  function test() {
    var x = new HasWriteOnlyProperty()
    print( x.WriteOnly ) // error
  }

}
