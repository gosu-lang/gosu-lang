package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksMixedReturnTypeCheck {

  function oneInt() : int { return 1 }
  function oneInteger() : Integer { return 1 as Integer }
  var One : Integer = 1

  function testReturnType1() {
    var l1: block(): Object = \-> {   // l1: Object:   ERROR:
      if (true)
        return oneInt() + 1
      return                  //## issuekeys: MISSING RETURN VALUE
    }
  }

  function testReturnType2() {
    var l2: block(): int = \-> {      // l2: int:     ERROR:
      if (true)
        return oneInt() + 1
      return                  //## issuekeys: MISSING RETURN VALUE
    }
  }

  function testReturnType3() {
    var l3 = \-> {                  // l3: <nothing>    Good: OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInt() + 1
      return
    }
  }

  function testReturnType4() {
    var l4: block(): Object = \-> {    // l4: Object:   ERROR:
      if (true)
        return oneInteger() + One
      return                  //## issuekeys: MISSING RETURN VALUE
    }
  }

  function testReturnType5() {
    var l5: block(): int = \-> {      // l5: int:   ERROR:
      if (true)
        return oneInteger() + One
      return                  //## issuekeys: MISSING RETURN VALUE
    }
  }

  function testReturnType6() {
    var l6 = \-> {                    // l6: <nothing>    Good:     OS Gosu accepts this because the block return type is not specified
      if (true)
        return oneInteger() + One
      return
    }
  }

  function testReturnType7() {
    var l7: block() = \-> {           // l7: void    ERROR:
      if (true)
        return
            oneInt() + 1   //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD OR BLOCK WITH VOID RESULT TYPE
      return
    }
  }

  function testReturnType8() {
    var l8: block(): void = \-> {      // l8: void:    ERROR:
      if (true)
        return oneInt() + 1                  //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD OR BLOCK WITH VOID RESULT TYPE
    }
  }

  function testReturnType9() {
    var l9: block(): int = \-> {       // l9: int:    ERROR:

    }            //## issuekeys: MISSING RETURN STATEMENT
  }

  function testReturnType10() {
    var l10: block() = \-> {           // l10: void  Good    return not required because block return type not specified
    }
  }

  function testReturnType11() {
    var l11 = \-> {                    // l11: <nothing>   Good    return not required because block return type not specified
    }
  }

  function testReturnType12() {
    var l12: block() = \-> {            // l12: void   ERROR:
      if (true)
        return 3                  //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD OR BLOCK WITH VOID RESULT TYPE
    }
  }

  function testReturnType13() {
    var l13 = \-> {                     // l13: <nothing>  Good
      if (true)
        return  3
    }
  }

  function testReturnType14() {
    var l14: block(): int = \-> {      // l14: int  ERROR
      if (true) {
        return 0
      }
      return null                  //## issuekeys: INCOMPATIBLE RETURN TYPE NULL IN LAMBDA EXPRESSION
    }
  }

  function testReturnType15() {
    var block0118: block() = \-> 42   // Good because of backward compatibility -- GRRRR
    var block0120: block() = \-> { return 42 }      //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD OR BLOCK WITH VOID RESULT TYPE
  }


}
