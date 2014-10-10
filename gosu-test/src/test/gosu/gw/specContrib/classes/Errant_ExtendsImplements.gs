package gw.specContrib.classes

class Errant_ExtendsImplements {

  interface Int1 extends List {
  }

  interface Int2 implements List {      //## issuekeys: NO IMPLEMENTS CLAUSE ALLOWED FOR INTERFACE
  }

  class Class2 implements List {
    delegate d : List represents List
  }

  class Class1 extends List {           //## issuekeys: NO INTERFACE EXPECTED HERE
    delegate d : List represents List
  }

}