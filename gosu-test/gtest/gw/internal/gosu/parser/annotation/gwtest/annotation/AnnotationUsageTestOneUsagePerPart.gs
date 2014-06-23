package gw.internal.gosu.parser.annotation.gwtest.annotation


@AnnotationUsageTestOneOnClass
class AnnotationUsageTestOneUsagePerPart {
  @AnnotationUsageTestOneOnProperty
  public property get varProperty() : String { return "" }

  @AnnotationUsageTestOneOnMethod
  public function varFunction() : String { return "" }

  @AnnotationUsageTestOneOnConstructor
  public construct() {}
}
