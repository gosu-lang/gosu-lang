package gw.internal.gosu.compiler.sample.annotations

class AnnotationThatThrowsInConstructor implements IAnnotation {
  construct() {
    throw new java.lang.RuntimeException("I hate method overloading")
  }
}