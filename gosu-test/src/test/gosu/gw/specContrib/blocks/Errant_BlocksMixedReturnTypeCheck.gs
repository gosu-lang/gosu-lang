package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksMixedReturnTypeCheck {

   function oneInt() : int { return 1 }
  function oneInteger() : Integer { return 1 as Integer }
  var One : Integer = 1

  function testReturnType() {
    var l1: block(): Object = \-> {    // l1: Object:   ERROR:                  //## issuekeys: MISSING RETURN VALUE
      if (true) 
        return oneInt() + 1
      return
    }

    var l2: block(): int = \-> {      // l2: int:     ERROR:                  //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInt() + 1
      return
    }

    var l3 = \-> {           // l3: <nothing>    Good: OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInt() + 1
      return
    }

    var l4: block(): Object = \-> {    // l4: Object:   ERROR:                  //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInteger() + One
      return
    }

    var l5: block(): int =  \-> {      // l5: int:   ERROR:                  //## issuekeys: MISSING RETURN VALUE
      if (true)
        return oneInteger() + One
      return
    }

    var l6 = \-> {                    // l6: <nothing>    Good:     OS Gosu accepts this because the block return type is not specified
      if (true)
         return oneInteger() + One
      return
    }

    var l7: block() = \-> {           // l7: void    ERROR:
      if (true)
         return       //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
      oneInt() + 1
      return
    }

    var l8: block(): void = \-> {      // l8: void:    ERROR:
      if (true)
        return oneInt() + 1                       //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
      return
    }



    var l10: block() =  \-> { }             // l10: void  Good    return not required because block return type not specified

    var l11 = \-> {}                     // l11: <nothing>   Good    return not required because block return type not specified

    var l12: block() = \-> {            // l12: void   ERROR:
      if (true)
        return 3       //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD OR BLOCK WITH VOID RESULT TYPE
    }

    var l13 = \-> {                     // l13: <nothing>  Good
      if (true)
        return 3
    }
  }


}
