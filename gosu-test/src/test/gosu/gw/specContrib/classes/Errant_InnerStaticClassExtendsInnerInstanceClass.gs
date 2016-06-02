package gw.specContrib.classes

class Errant_InnerStaticClassExtendsInnerInstanceClass {
  class A{}
  static class B extends A  {}       //## issuekeys: NO ENCLOSING INSTANCE OF TYPE 'GW.SPECCONTRIB.CLASSES.ERRANT_INNERSTATICCLASSEXTENDSINNERINSTANCECLASS' IS IN SCOPE
  class C extends A{}
}