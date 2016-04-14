package gw.internal.gosu.compiler.sample.annotations

class AnyArgGosuAnnotation implements IAnnotation {

  var _s : Object as Value

  construct( s : Object ) {
    _s = s
  }

}