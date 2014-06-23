package gw.internal.gosu.compiler.sample.expression

class TemplateStrings {
  static var _staticProp : String as StaticProperty = "staticProp"
  static var _staticVar : String = "staticVar"
  var _classProp : String as ClassProperty = "classProp"
  var _classVar : String = "classVar"

  function simpleExpr1() : String {
    return "${42}"
  }

  function simpleExpr2() : String {
    return "${true}"
  }

  function simpleExpr3() : String {
    return "${"test"}"
  }

  function simpleExpr4() : String {
    return "${typeof ""}"
  }

  function simpleExpr5() : String {
    return "${40 + 2}"
  }

  function simpleExpr6() : String {
    return "${ "test" + "test" }"
  }

  function simpleExpr7() : String {
    return "${ new String("test") }"
  }

  function localVar() : String {
    var x = "localVar"
    return "${x}"
  }

  function classVar() : String {
    return "${_classVar}"
  }

  function thisClassVar() : String {
    return "${this._classVar}"
  }

  function staticVar() : String {
    return "${_staticVar}"
  }

  function thisStaticVar() : String {
    return "${this._staticVar}"
  }

  function classProp() : String {
    return "${ClassProperty}"
  }

  function thisClassProp() : String {
    return "${this.ClassProperty}"
  }

  function staticProp() : String {
    return "${StaticProperty}"
  }

  function thisStaticProp() : String {
    return "${TemplateStrings.StaticProperty}"
  }

  function classFunction() : String {
    return "classFunc"
  }

  function classFunc() : String {
    return "${classFunction()}"
  }

  function thisClassFunc() : String {
    return "${this.classFunction()}"
  }

  static function staticFunction() : String {
    return "staticFunc"
  }

  function staticFunc() : String {
    return "${staticFunction()}"
  }

  function thisStaticFunc() : String {
    return "${TemplateStrings.staticFunction()}"
  }

  function blockExample() : String {
    var blk = \-> "block"
    return "${blk()}"
  }

  function nested_simpleExpr1() : String {
    return "${ "${42}" }"
  }

  function nested_simpleExpr2() : String {
    return "${ "${true}" }"
  }

  function nested_simpleExpr3() : String {
    return "${ "${"test"}" }"
  }

  function nested_simpleExpr4() : String {
    return "${ "${typeof ""}" }"
  }

  function nested_simpleExpr5() : String {
    return "${ "${40 + 2}" }"
  }

  function nested_simpleExpr6() : String {
    return "${ "${ "test" + "test" }" }"
  }

  function nested_simpleExpr7() : String {
    return "${ "${ new String("test") }" }"
  }

  function nested_localVar() : String {
    var x = "localVar"
    return "${ "${x}" }"
  }

  function nested_classVar() : String {
    return "${ "${_classVar}" }"
  }

  function nested_thisClassVar() : String {
    return "${ "${this._classVar}" }"
  }

  function nested_staticVar() : String {
    return "${ "${_staticVar}" }"
  }

  function nested_thisStaticVar() : String {
    return "${ "${this._staticVar}" }"
  }

  function nested_classProp() : String {
    return "${ "${ClassProperty}" }"
  }

  function nested_thisClassProp() : String {
    return "${ "${this.ClassProperty}" }"
  }

  function nested_staticProp() : String {
    return "${ "${StaticProperty}" }"
  }

  function nested_thisStaticProp() : String {
    return "${ "${TemplateStrings.StaticProperty}" }"
  }

  function nested_classFunc() : String {
    return "${ "${classFunction()}" }"
  }

  function nested_thisClassFunc() : String {
    return "${ "${this.classFunction()}" }"
  }

  function nested_staticFunc() : String {
    return "${ "${staticFunction()}" }"
  }

  function nested_thisStaticFunc() : String {
    return "${ "${TemplateStrings.staticFunction()}" }"
  }

  function nested_blockExample() : String {
    var blk = \-> "block"
    return "${ "${blk()}" }"
  }

//## todo: @KnownBreak
//  function nested1() : String {
//    var test = "test"
//    return "${ "${test}" + "${test}" }"
//  }
}