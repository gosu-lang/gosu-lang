package gw.specContrib.classes

class Errant_UnqualifiedInheritedInnerTypeTestForGosu extends GosuClassWithInners implements GosuInterfaceWithInners {
  var a: InterfaceA
  var b: PublicInnerInterface
  var c: PrivateInnerInterface  //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
  var d: PublicInnerClass
  var e: PrivateInnerClass  //## issuekeys: MSG_TYPE_HAS_XXX_ACCESS
}