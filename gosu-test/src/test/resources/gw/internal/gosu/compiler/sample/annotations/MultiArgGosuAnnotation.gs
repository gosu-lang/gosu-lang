package gw.internal.gosu.compiler.sample.annotations

class MultiArgGosuAnnotation implements IAnnotation {

  var _s : String as StrValue
  var _i : int as IntValue

  construct( s : String, i : int ) {
    _s = s
    _i = i
  }

}