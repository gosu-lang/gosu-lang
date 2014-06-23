package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_GetterSetterDisagreeOnStaticMod {

  property get GetterStaticProp() : String { return "" }
  static property set GetterStaticProp(s : String ) {}

  property get SetterStaticProp() : String { return "" }
  static property set SetterStaticProp(s : String ) {}

  var _field1 : String as AsStaticSetter
  static property set AsStaticSetter(s : String ) {}

  var _field2 : String as AsStaticGetter
  static property get AsStaticGetter() : String { return "" }

  var _field3 : String as AsStaticGetterAndSetter
  static property set AsStaticGetterAndSetter(s : String ) {}
  static property get AsStaticGetterAndSetter() : String { return "" }

  static var _sfield1 : String as StaticAsSetter
  property set StaticAsSetter(s : String ) {}

  static var _sfield2 : String as StaticAsGetter
  property get StaticAsGetter() : String { return "" }

  static var _sfield3 : String as StaticAsGetterAndSetter
  property set StaticAsGetterAndSetter(s : String ) {}
  property get StaticAsGetterAndSetter() : String { return "" }


}
