package gw.specContrib.classes.modifiers

uses gw.specContrib.classes.modifiers.other.Errant_OtherTestClass

class Errant_TestInnerClassesAreDefaultPublic {
  function test() {
    var inner1 : Errant_OtherTestClass.InnerPublicClass
    var inner2 : Errant_OtherTestClass.InnerPublicInterface
    var inner3 : Errant_OtherTestClass.InnerPublicStructure
    var inner4 : Errant_OtherTestClass.InnerPublicEnum
    var inner5 : Errant_OtherTestClass.InnerPublicAnnotation

    var inner6 : Errant_OtherTestClass.InnerPrivateClass       //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
    var inner7 : Errant_OtherTestClass.InnerPrivateInterface   //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
    var inner8 : Errant_OtherTestClass.InnerPrivateStructure   //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
    var inner9 : Errant_OtherTestClass.InnerPrivateEnum        //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
    var inner10 : Errant_OtherTestClass.InnerPrivateAnnotation        //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
  }
}