package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksMixedReturnTypeCheck {

  function oneInt() : int { return 1 }
  function oneInteger() : Integer { return 1 as Integer }

  function testReturnType() {
    var l1: block(): Object = \-> {    // l1: Object:   ERROR:
      if (true) 
        return oneInt()
      return      //## issuekeys: MISSING RETURN VALUE
    }

    var l2: block(): int = \-> {      // l2: int:     ERROR:
      if (true)
        return oneInt()
      return      //## issuekeys: MISSING RETURN VALUE
    }

    var l3 = \-> {           // l3: <nothing>    Good: OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInt()
      return
    }

    var l4: block(): Object = \-> {    // l4: Object:   ERROR:
      if (true)
        return oneInteger()
      return      //## issuekeys: MISSING RETURN VALUE
    }

    var l5: block(): int =  \-> {      // l5: int:   ERROR:
      if (true)
        return oneInteger()
      return      //## issuekeys: MISSING RETURN VALUE
    }

    var l6 = \-> {                    // l6: <nothing>    Good:     OS Gosu accepts this because the block return type is not specified
      if (true)
         return oneInteger()
      return
    }

    var l7: block() = \-> {           // l7: <nothing>    Good:     OS Gosu accepts this because block return type is not specified
      if (true)
         return oneInt()
      return
    }

    var l8: block(): void = \-> {      // l8: void:    ERROR:
      if (true)
        return 1      //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
      return
    }

 

    var l10: block() =  \-> { }             // l10: <nothing>  Good    return not required because block return type not specified

    var l11 = \-> {}                     // l11: <nothing>   Good    return not required because block return type not specified

  }

}
