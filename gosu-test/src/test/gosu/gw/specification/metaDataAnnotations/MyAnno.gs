package gw.specification.metaDataAnnotations

uses gw.lang.IAnnotation
uses manifold.api.sourceprod.ClassType
uses gw.lang.annotation.AnnotationUsage
uses java.lang.annotation.ElementType
uses java.lang.annotation.Target
uses java.lang.annotation.RetentionPolicy
uses java.lang.annotation.Retention

@Retention(RUNTIME)
@Target({TYPE,METHOD,CONSTRUCTOR,FIELD})
annotation MyAnno {
  function foo() : ClassType = Enhancement
  function bar() : ClassType[] = {Annotation}
  function stuff() : String
  function number() : int = 9
  //function baz() : JavaAnno = @JavaAnno(:foo = "hello")
}