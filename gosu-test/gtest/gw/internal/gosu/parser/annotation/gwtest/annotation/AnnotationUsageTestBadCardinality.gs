package gw.internal.gosu.parser.annotation.gwtest.annotation

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
@AnnotationUsageTestOneOnClass
@AnnotationUsageTestOneOnClass
class AnnotationUsageTestBadCardinality {
  @AnnotationUsageTestOneOnProperty
  @AnnotationUsageTestOneOnProperty
  public var varFeature : String

  @AnnotationUsageTestOneOnProperty
  @AnnotationUsageTestOneOnProperty
  public property get varProperty() : String { return "" }

  @AnnotationUsageTestOneOnMethod
  @AnnotationUsageTestOneOnMethod
  public function varFunction() : String { return "" }

  @AnnotationUsageTestOneOnConstructor
  @AnnotationUsageTestOneOnConstructor
  public construct() {}
}
