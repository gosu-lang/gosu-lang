package gw.specContrib.typeinference

uses java.util.ArrayList
uses java.util.HashMap
uses java.util.Map

class Errant_EnumLiteral {
  enum GosuEnum {
    VAL1, VAL2
  }

  function acceptEnum(p: GosuEnum) {}

  function acceptEnumOverloadWithTypeParameter<T>(p: T) {}  // to make type inference more complex
  function acceptEnumOverloadWithTypeParameter(p: GosuEnum) {}

  function acceptMap(p: Map<GosuEnum, String>) {}

  function test() {
    acceptEnum(VAL2)
    acceptEnumOverloadWithTypeParameter(VAL1)
    acceptMap({VAL1 -> "val"})
    // IDE-1528
    var a = new HashMap<GosuEnum, String>() { VAL1 -> "val1" }
    var b = new ArrayList<GosuEnum>() { VAL1 }
  }
}