package gw.internal.gosu.compiler.sample.annotations

@gw.lang.Deprecated( "foo" )
interface SampleAnnotatedInterface {

  @gw.lang.Deprecated( "bar" )
  function hasAnnotations()

}