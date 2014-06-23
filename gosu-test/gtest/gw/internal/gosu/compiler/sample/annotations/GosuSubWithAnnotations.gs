package gw.internal.gosu.compiler.sample.annotations;

@InheritedJavaAnnotation(42)
class GosuSubWithAnnotations extends JavaSuperWithAnnotations
{

  @InheritedJavaAnnotation(42)
  construct() {}

  @InheritedJavaAnnotation(42)
  function methodWithAnnotation() {}

  @InheritedJavaAnnotation(42)
  property get PropertyWithAnnotation() : String {
    return null
  }
}
