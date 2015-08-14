package gw.specification.statements.returnsExitsAndExceptions.theTryCatchFinallyStatement

uses gw.BaseVerifyErrantTest

class TheTryCatchFinallyStatementTest extends BaseVerifyErrantTest {

  function testErrant_TheTryCatchFinallyStatementTest() {
    processErrantType(Errant_TheTryCatchFinallyStatementTest)
  }

  function testTryCatchFinallyNumbers() {
    try {}
    finally {}

    try {}
    finally {}

    try {}
    catch(e : Exception) {}

    try {}
    catch(e : Exception) {}
    finally {}

    try {}
    finally {}
    //catch(e : Exception) {}

    try {}
    catch(e : ArithmeticException) {}
    catch(e : Exception) {}

    try {}
    catch(e : Exception) {}
    //catch(e : ArithmeticException) {}

    try {}
    catch(e : Exception) {}
    //catch(e : Exception) {}

    try {}
    catch(e : ArithmeticException) {}
    catch(e : Exception) {}
    finally {}

    //try  var a = 0
    //finally {}
  }

  function testTryCatchBasic/*<T extends Throwable>*/() {
    var flag = false
    try {
      var f = 1/0
    } catch(e : ArithmeticException) { flag = true}
    assertTrue(flag)

  /* try {
      var f = 1/0
    } catch(e : T) {}
   try {
      var f = 1/0
    } catch(e : String) {}          */
  }

  function testTryCatchNormalTermination() {
    var flag = false
    try {
      var f = 1/2
    } catch(e : ArithmeticException) { flag = true}
    assertFalse(flag)

    try {
      var f = 1/2
      while(true) {
        if(1 == 1)
          break
        f = 1 / 0
      }
    } catch(e : ArithmeticException) { flag = true}
    assertFalse(flag)

    try {
      var f = 1/2
      var done = false
      while(!done) {
        done = true
        if(done)
          continue
        f = 1 / 0
      }
    } catch(e : ArithmeticException) { flag = true}
    assertFalse(flag)

    var runMe = \ -> {
      try {
        var f = 1/2
        if(1 == 1)
          return ;
        f = 1 / 0
      } catch(e : ArithmeticException) { flag = true}
    }
    runMe()
    assertFalse(flag)
  }

  function testTryCatchAbruptTermination() {
    var x = 1
    try {
      var f = 1/0
    }
    catch(e : IndexOutOfBoundsException) { x = 2}
    catch(e : ArithmeticException) { x = 3}
    assertEquals(3, x)

    x = 1
    try {
      try {
      var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = 2
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    assertEquals(2, x)

    x = 1
    var a : int[] = new int[0]
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = a[1]
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    assertEquals(3, x)

    x = 1
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        throw new IndexOutOfBoundsException()
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    assertEquals(3, x)

  }


  function testFinally() {
    var x = 1
    try {
      var f = 1/0
    }
    catch(e : IndexOutOfBoundsException) { x = 2}
    catch(e : ArithmeticException) { x = 3}
    finally {x = 4}
    assertEquals(4, x)

    x = 1
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = 2
      }
      finally {x = 4}
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    assertEquals(4, x)

    x = 1
    var a : int[] = new int[0]
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = a[1]
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    finally {x = 4}
    assertEquals(4, x)

    x = 1
    try {
      try {
        var f = 1/0
      }
        catch(e : ArithmeticException) {
          x = a[1]
        }
        finally {x = 4}
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    assertEquals(3, x)

    x = 1
    var runMe = \ -> {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
         x = 2
         return
      }
      finally { x = 3}
    }

    try {
      runMe()
    }
    catch(e : IndexOutOfBoundsException) { x = 4}
    assertEquals(3, x)
  }
}