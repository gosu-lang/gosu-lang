package gw.specContrib.statements

class Errant_FinallyExit {

  // IDE-1549

  function testMethod1() {
    for (i in {1, 2, 3}) {
      try {
        if (i > 2) continue // this is okay
        if (i > 1) return   // this is okay
        if (i > 0) break    // this is okay
      } catch (e: Exception) {
      } finally {
        if (i == 2) break            //## issuekeys: BREAK OUTSIDE SWITCH OR LOOP, OR THAT EXITS A FINALLY BLOCK
        if (i == 1) continue            //## issuekeys: CONTINUE OUTSIDE OF LOOP OR THAT EXITS A FINALLY BLOCK
        return            //## issuekeys: RETURN THAT EXITS A FINALLY BLOCK NOT ALLOWED
      }
    }
  }

}
