package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLStaticVsNonStaticProperties {
//   var sfdf : String as NonStaticProp
  property get NonStaticProp() : String {return null}
  property set NonStaticProp(str : String) {}
  static property get StaticProp() : String {return null}
  static property set StaticProp(str : String) {}

  //OS Gosu bad here- IDE-1587
  //Bound : non-static function and non-static context
  //{{Expected}} : Bound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  var nnn11 : gw.lang.reflect.features.BoundPropertyReference = #NonStaticProp
  var nnn12 : gw.lang.reflect.features.BoundPropertyReference = this#NonStaticProp

  //Unbound because static context
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Unbound
  static var nnn2 : gw.lang.reflect.features.PropertyReference = #NonStaticProp

  //Parser bad here
  //Unbound : refering to static fun so unbound.
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  var sss1 : gw.lang.reflect.features.PropertyReference= #StaticProp

  //Unbound : because refereing to static as well as the context is static
  //{{Expected}} : Unbound. {{OS Gosu}} - Unbound. {{Parser}} - Bound
  static var sss2 : gw.lang.reflect.features.PropertyReference = #StaticProp
}

