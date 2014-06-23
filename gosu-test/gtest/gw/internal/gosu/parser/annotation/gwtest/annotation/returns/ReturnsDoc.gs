package gw.internal.gosu.parser.annotation.gwtest.annotation.returns

class ReturnsDoc {
  public var varFeature : String
  public property get varProperty() : String { return "" }
  @Returns("docs")
  public function varFunction() : String { return "" }
  function ReturnsDoc() {}
}
