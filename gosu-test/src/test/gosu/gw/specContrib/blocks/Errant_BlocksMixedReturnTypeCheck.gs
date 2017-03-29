package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksMixedReturnTypeCheck {

  function oneInt() : int { return 1 }
  function oneInteger() : Integer { return 1 as Integer }

  function testReturnType() {
    var l1: block(): Object = \-> {    // l1: Object:  //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInt()
      return
    }

    var l2: block(): int = \-> {      // l2: int:     //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInt()
      return
    }

    var l3 = \-> {           // l3: <nothing>    Good: OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInt()
      return
    }

    var l4: block(): Object = \-> {    // l4: Object:  //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInteger()
      return
    }

    var l5: block(): int = \-> {      // l5: int:      //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInteger()
      return
    }

    var l6 = \-> {                    // l6: <nothing>    Good: OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInteger()
      return
    }

    var l7: block() = \-> {           // l7: <nothing>    Good: OS Gosu accepts this because block return type is not specified
      if (true)
        return oneInt()  
      return
    }

    var l8: block(): void = \-> {      // l8: void:
      if (true)
        return oneInt()      //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
      return
    }

    var l9: block() = \-> { }             // l9: <nothing>  Good

    var l10 = \-> { }                   // l10: <nothing>   Good

  }


}
