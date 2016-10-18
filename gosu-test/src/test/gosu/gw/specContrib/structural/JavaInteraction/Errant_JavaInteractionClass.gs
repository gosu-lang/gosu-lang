package gw.specContrib.structural.JavaInteraction

/**
 * Created by vdahuja on 10/9/2014.
 */
class Errant_JavaInteractionClass {

  function javaFun() : String { return null}

  function structFun() : Integer { return null}

  var jProp : String as JavaProp1

  var sProp1 : Integer as StructProp1

  property get JavaProp2(): String { return null }

  property get StructProp2(): Integer { return null }

  function main() {
    var myStructure : MyStructure = new gw.specContrib.structural.JavaInteraction.Errant_JavaInteractionClass()
  }

}