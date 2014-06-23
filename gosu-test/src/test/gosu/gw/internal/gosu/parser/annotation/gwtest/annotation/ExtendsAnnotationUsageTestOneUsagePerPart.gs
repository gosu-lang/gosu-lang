package gw.internal.gosu.parser.annotation.gwtest.annotation


@AnnotationUsageTestOneOnClass
class ExtendsAnnotationUsageTestOneUsagePerPart extends AnnotationUsageTestOneUsagePerPart {
  @AnnotationUsageTestOneOnProperty
  public property get varProperty() : String { return "" }

  @AnnotationUsageTestOneOnMethod
  public function varFunction() : String { return "" }

  @AnnotationUsageTestOneOnConstructor
  public construct() {
    super()
  }
}
