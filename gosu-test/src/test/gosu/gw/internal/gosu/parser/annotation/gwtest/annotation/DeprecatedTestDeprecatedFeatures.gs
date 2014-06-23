package gw.internal.gosu.parser.annotation.gwtest.annotation

class DeprecatedTestDeprecatedFeatures {
  @Deprecated("deprecated")
  public var varFeature : String
  public var varFeature2 : String
  @Deprecated("deprecated")
  public var varFeatureWithProperty : String as VarAsProperty
  public var varFeatureWithProperty2 : String as VarAsProperty2
  @Deprecated("deprecated")
  public property get varProperty() : String {return "" }
  public property get varProperty2() : String {return "" }
  @Deprecated("deprecated")
  public function varFunction() : String {return "" }
  public function varFunction2() : String {return "" }
  @Deprecated("deprecated")
  public function varFunctionWithParam(arg : String) : String {return "" }
  public function varFunctionWithParam2(arg : String) : String {return "" }
  @Deprecated("deprecated")
  public function DeprecatedTestDeprecatedFeatures() {}
  public function DeprecatedTestDeprecatedFeatures(arg : String) {}
}
