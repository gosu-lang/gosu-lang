package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.I4
uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.I6

class Errant_InterfaceInheritanceTest {


  // implement the interface I4, which has ambiguous reference
  class K1 implements I4{
    var tmp : int
    construct(){
      tmp = RED
      tmp = ORANGE
      tmp = YELLOW  //## issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE, MSG_TYPE_MISMATCH
    }

    function test(){
      print(K1.YELLOW)
      print(YELLOW)  //## issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
      print(K1.ORANGE)
      print(ORANGE)
      print(K1.RED)
      print(RED)
    }

    override function firstMethod() : int {
      return 1
    }

    override function secondMethod() : int {
      return 1
    }

    override function thirdMethod() : int {
      return 1
    }
  }



  // implement interface I6, which has NO ambiguous reference
  class K implements I6 {
    override function secondMethod(): int {
      return 2
    }

    override function firstMethod(): int {
      return 1
    }

    override function thirdMethod() {
    }

    function test1(){
      print(K.YELLOW)
      print(YELLOW)
      print(K.ORANGE)
      print(ORANGE)
      print(K.RED)
      print(RED)
    }

    override function toString() : String{
      return "toString()"
    }

    override function equals(o : Object) : boolean{
      return true
    }

    override function hashCode() : int {
      return 1
    }
  }












}