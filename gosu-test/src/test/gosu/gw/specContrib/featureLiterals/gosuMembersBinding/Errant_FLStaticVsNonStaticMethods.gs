package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLStaticVsNonStaticMethods {
  function nonstaticfun() {}
  static function staticfun() {}

  // IDE-1587 - without root expression it's unbound
  var nnn11 :gw.lang.reflect.features.MethodReference<Errant_FLStaticVsNonStaticMethods, block(Errant_FLStaticVsNonStaticMethods)> = #nonstaticfun()

  var nnn12 :gw.lang.reflect.features.BoundMethodReference<Errant_FLStaticVsNonStaticMethods, block()> = this#nonstaticfun()

  static var nnn2 : gw.lang.reflect.features.MethodReference<Errant_FLStaticVsNonStaticMethods, block(Errant_FLStaticVsNonStaticMethods)> = #nonstaticfun()

  // IDE-1586 - without root expression it's unbound
  var sss1 : gw.lang.reflect.features.MethodReference<Errant_FLStaticVsNonStaticMethods, block()> = #staticfun()

  static var sss2 : gw.lang.reflect.features.MethodReference<Errant_FLStaticVsNonStaticMethods, block()> = #staticfun()
}