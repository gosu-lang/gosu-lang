package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLStaticVsNonStatic {
  function nonstaticfun() {}
  static function staticfun() {}

  //OS Gosu bad here  IDE-1587
  //Bound : non-static function and non-static context
  //{{Expected}} : Bound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  var nnn11 :gw.lang.reflect.features.BoundMethodReference = #nonstaticfun()

  var nnn12 :gw.lang.reflect.features.BoundMethodReference = this#nonstaticfun()

  //Unbound because static context
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Unbound
  static var nnn2 : gw.lang.reflect.features.MethodReference = #nonstaticfun()

  //Parser bad here : IDE-1586
  //Unbound : refering to static fun so unbound.
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  var sss1 : gw.lang.reflect.features.MethodReference = #staticfun()

  //Unbound : because refereing to static as well as the context is static
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  static var sss2 : gw.lang.reflect.features.MethodReference = #staticfun()
}