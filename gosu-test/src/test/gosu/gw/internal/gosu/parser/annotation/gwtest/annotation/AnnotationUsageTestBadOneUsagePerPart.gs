package gw.internal.gosu.parser.annotation.gwtest.annotation

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource 
@AnnotationUsageTestOneOnProperty
class AnnotationUsageTestBadOneUsagePerPart {

  @AnnotationUsageTestOneOnClass  
  public var varFeature : String

  @AnnotationUsageTestOneOnConstructor
  public property get varProperty() : String { return "" }

  @AnnotationUsageTestOneOnProperty
  public function varFunction() : String { return "" }

  @AnnotationUsageTestOneOnMethod
  public construct() {}
}
 