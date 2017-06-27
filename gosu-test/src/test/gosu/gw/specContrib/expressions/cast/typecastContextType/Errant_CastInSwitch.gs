package gw.specContrib.expressions.cast.typecastContextType

class Errant_CastInSwitch {

  function foo() {
    var empty1: Object[] = {} as Integer[]
  }

  function bar(obj: Object[]) {

    switch (obj) {
      case {}:
        print("empty initializer")
        break
      case {1, 2, 3}:
        print("integer list")
        break
      case {"sdf", "sdf"}:
        print("string list")
        break
      case {"sdf", "sdf"} as String[]: //## issuekeys: MSG_UNNECESSARY_COERCION
        print("string list")
        break
      default:
        print("default")
    }
  }


  function bar2(obj: Integer[]) {
    switch (obj) {
      case {} as Integer[]: //## issuekeys: MSG_UNNECESSARY_COERCION
        print("empty initializer")
        break
      case {1, 2, 3} as Integer[]:  //## issuekeys: MSG_UNNECESSARY_COERCION
        print("integer list")
        break
      case {new Object()} as Integer[]:         //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.OBJECT>' TO 'JAVA.LANG.INTEGER[]'
        print("integer list")
        break
      case {"sdf", "sdf"}:           //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>', REQUIRED: 'JAVA.LANG.INTEGER[]'
        print("string list")
        break
      case {"1", "2"} as Integer[]:           //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>' TO 'JAVA.LANG.INTEGER[]'
        print("string list")
        break
      case {1, "2"} as Integer[]:         //## issuekeys: INCONVERTIBLE TYPES; CANNOT CAST 'JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>' TO 'JAVA.LANG.INTEGER[]'
        print("integer list")
        break
      default:
        print("default")
    }
  }

  function bar3(obj: Integer[]) {
    switch (obj) {
      case {} as Integer[]:       //should NOT be error  //## issuekeys: MSG_UNNECESSARY_COERCION
        print("empty initializer")
        break
    }
  }
}
