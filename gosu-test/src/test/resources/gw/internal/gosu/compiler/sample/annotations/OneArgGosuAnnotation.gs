package gw.internal.gosu.compiler.sample.annotations

class OneArgGosuAnnotation implements IAnnotation {

  var _s : String as StrValue

  construct( s : String ) {
    _s = s
  }

}