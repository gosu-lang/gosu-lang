package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLStaticVsNonStaticProperties {
  property get NonStaticProp() : String {return null}
  property set NonStaticProp(str : String) {}
  static property get StaticProp() : String {return null}
  static property set StaticProp(str : String) {}

  // IDE-1587
  var nnn11 : gw.lang.reflect.features.PropertyReference<Errant_FLStaticVsNonStaticProperties, String> = #NonStaticProp
  var nnn12 : gw.lang.reflect.features.BoundPropertyReference<Errant_FLStaticVsNonStaticProperties, String> = this#NonStaticProp

  static var nnn2 : gw.lang.reflect.features.PropertyReference<Errant_FLStaticVsNonStaticProperties, String> = #NonStaticProp

  var sss1 : gw.lang.reflect.features.PropertyReference<Errant_FLStaticVsNonStaticProperties, String> = #StaticProp

  static var sss2 : gw.lang.reflect.features.PropertyReference<Errant_FLStaticVsNonStaticProperties, String> = #StaticProp
}

