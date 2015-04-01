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

    // IDE-1846
    var v1: String = {GosuEnum.VAL1 -> ""}[VAL1]
    var v2: String = ({GosuEnum.VAL1 -> ""})[VAL1]
    var v3: String = ({GosuEnum.VAL2 -> ""})[(VAL1)]
    var m = {GosuEnum.VAL1 -> "val1", GosuEnum.VAL2 -> "val2"}
    var v4: String = m[VAL2]
  }
}