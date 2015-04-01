package gw.specContrib.interfaces

class Errant_AmbiguousInterfaceFieldReference {

  interface Interface1 {
    var field: String = "Interface1"
  }

  interface Interface2 {
    var field: String = "Interface2"
  }

  class A implements Interface1, Interface2 {
    construct() {
      print(field)  //## issuekeys: MSG_AMBIGUOUS_SYMBOL_REFERENCE
    }
  }


}
