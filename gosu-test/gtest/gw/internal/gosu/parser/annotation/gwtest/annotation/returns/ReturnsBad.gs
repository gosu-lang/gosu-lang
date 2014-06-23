package gw.internal.gosu.parser.annotation.gwtest.annotation.returns

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ReturnsBad {
  @Returns("bad")
  public var varFeature : String
  @Returns("bad")
  public property get varProperty() : String { return "" }
  @Returns("good")
  public function varFunction() : String { return "" }
  @Returns("bad")
  function ReturnsBad() {}
}
