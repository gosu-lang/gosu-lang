package gw.spec.core.annotations.instances

public class RegressionAnnotationHolderClass {

  @Deprecated( "foo" )
  public static var publicStaticVar : String

  /*
   * This is a test comment
   * @deprecated foo */
  public static var deprecatedWComment : String

  /* @deprecated */
  public function deprecatedFunction(){}

  public function nonDeprecatedFunction(){}

  /* @deprecated */
  public function anotherDeprecatedFunction(){}

  class InnerTypeNotDeprecated{}
}