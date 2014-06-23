package gw.internal.gosu.parser.annotation.gwtest.annotation.param

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
@Param("bad", "bad")
class ParamBad {
  @Param("bad", "bad")
  public var varFeature : String
  @Param("bad", "bad")
  public property get varProperty() : String { return "" }
}
