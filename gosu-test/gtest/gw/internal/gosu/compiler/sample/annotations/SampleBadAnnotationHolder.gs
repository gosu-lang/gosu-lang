package gw.internal.gosu.compiler.sample.annotations

class SampleBadAnnotationHolder {

  @AnnotationThatThrowsInConstructor //throws
  @AnyArgGosuAnnotation( (null as String).toString() ) //NPE
  @gw.lang.Deprecated( "This annotation should still be found" )
  function hasBadAnnotations() {
  }

}