package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

class Errant_GosuClass_31 {
  property get NormalProperty() : String { return null }
  property set NormalProperty(s: String) {}

  property get smallCaseProperty1() : String { return null }
  property set smallCaseProperty1(s: String) {}

  property get smallCaseProperty2() : String { return null }
  property set smallCaseProperty2(s: String) {}

  property get SmallCaseProperty3() : String { return null }
  property set SmallCaseProperty3(s: String) {}

  property get OnlyGetter1() : String { return null }
  var onlyGetter2 : String as OnlyGetter2

  property set OnlySetter1(s : String) {}
  var onlySetter2 : String as OnlySetter2

  property get GetterPropInClassSetterFunctionInEnh111() : String { return null }

  property set SetterPropInClassGetterFunctionInEnh222(s : String) {}
}