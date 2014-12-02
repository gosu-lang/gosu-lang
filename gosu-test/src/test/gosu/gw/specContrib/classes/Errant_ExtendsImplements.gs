package gw.specContrib.classes

class Errant_ExtendsImplements {

  interface Int1 extends List {
  }

  interface Int2 implements List {      //## issuekeys: MSG_NO_IMPLEMENTS_ALLOWED
  }

  class Class2 implements List {
    delegate d : List represents List
  }

  class Class1 extends List {   delegate d : List represents List }     //## issuekeys: MSG_CLASS_CANNOT_EXTEND_INTERFACE

}