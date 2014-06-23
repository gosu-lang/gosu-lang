package gw.internal.gosu.parser.flow
uses java.lang.RuntimeException
uses java.lang.Exception
uses gw.testharness.DoNotVerifyResource

class Errant_TryCatchFinallyStmt {
  function f0() : int {
    do {
      try {
        break
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f0_1() : int {
    do {
      try {
        continue
      }
      finally {

      }
    } while( true )
  }

  function f1() : int {
    try {
      return 1
    }
    finally {

    }
  }

  function f2() : int {
    try {
      throw new RuntimeException()
    }
    finally {

    }
  }

  function f3() : int {
    try {
      assert 1 == 2
    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4() : int {
    try {
      while( true ) {
      }
    }
    finally {

    }
  }

  function f5() : int {
    try {
      while( true ) {
        break
      }
    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
    } while( true )
  }

  function f1_1() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        break
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        break
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_3() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        continue
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_3() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        continue
      }
    } while( true )
  }

  //--------

  function f00_4() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        return 1
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_4() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        return 1
      }
    } while( true )
  }

  //--------

  function f00_5() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_5() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
    } while( true )
  }

  //--------

  function f00_6() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_6() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
    } while( true )
  }

  //--------

  function f00_7() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_7() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
    } while( true )
  }

  ///////////////////////////////////

  //--------

  function f00_1_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      finally {

      }
    } while( true )
  }

  function f1_1_1() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_1() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_1() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_1() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_1() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    finally {

    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_2_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        break
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_2_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        break
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_3_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        continue
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_3_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        continue
      }
      finally {

      }
    } while( true )
  }

  //--------

  function f00_4_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        return 1
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_4_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        return 1
      }
      finally {

      }
    } while( true )
  }

  //--------

  function f00_5_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_5_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
      finally {

      }
    } while( true )
  }

  //--------

  function f00_6_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_6_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
      finally {

      }
    } while( true )
  }

  //--------

  function f00_7_1() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
      finally {

      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_7_1() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
      finally {

      }
    } while( true )
  }

  ///////////////////////////////////

  //--------

  function f00_1_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_2() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_2() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_2() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_2() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_2() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_2_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        break
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_2_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        break
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  //--------

  function f00_3_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        continue
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_3_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        continue
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  //--------

  function f00_4_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        return 1
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_4_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        return 1
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  //--------

  function f00_5_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_5_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        throw new RuntimeException()
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  //--------

  function f00_6_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_6_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        assert e != null
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  //--------

  function f00_7_2() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_7_2() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        while( true ) {
        }
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  //----------

  function f00_1_02() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_02() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_02() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_02() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_02() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_02() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_02() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //----------

  function f00_1_03() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        break
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_03() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        break
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f1_1_03() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_03() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_03() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_03() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_03() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //----------

  function f00_1_04() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        continue
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_04() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        continue
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_04() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      throw e
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_04() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      throw e
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_04() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      throw e
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_04() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      throw e
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_04() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      throw e
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //----------

  function f00_1_05() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        return 1
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_05() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        return 1
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_05() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      assert 1 == 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_05() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      assert 1 == 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_05() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      assert 1 == 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_05() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      assert 1 == 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_05() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      assert 1 == 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  //----------

  function f00_1_06() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        throw e
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_06() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {

      }
      catch( e : Exception ) {
        throw e
      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_06() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      while( true ) {}
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_06() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      while( true ) {}
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_06() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      while( true ) {}
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_06() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      while( true ) {}
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_06() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      while( true ) {}
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

 //----------

  function f00_1_07() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        break
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_07() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        break
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f1_1_07() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f2_1_07() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f3_1_07() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {

    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_07() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f5_1_07() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {

    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

 //----------

  function f00_1_08() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        continue
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_08() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        continue
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_08() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f2_1_08() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f3_1_08() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_08() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f5_1_08() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {
      return 1
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

 //----------

  function f00_1_09() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        return 1
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_09() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        return 1
      }
      catch( e : Exception ) {

      }
      finally {
        while( true ) {}
      }
    } while( true )
  }

  function f1_1_09() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {
      throw e
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f2_1_09() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {
      throw e
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f3_1_09() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {
      throw e
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_09() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {
      throw e
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }

  function f5_1_09() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {
      throw e
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
      while( true ) {}
    }
  }  //## issuekeys: MSG_MISSING_RETURN

 //----------

  function f00_1_012() : int {
    do {
      try {
        break
      }
      catch( e : RuntimeException ) {
        assert 1 == 1
      }
      catch( e : Exception ) {

      }
      finally {
      }
    } while( true )
  }  //## issuekeys: MSG_MISSING_RETURN

  function f01_1_012() : int {
    do {
      try {
        continue
      }
      catch( e : RuntimeException ) {
        assert 1 == 1
      }
      catch( e : Exception ) {

      }
      finally {
      }
    } while( true )
  }

  function f1_1_012() : int {
    try {
      return 1
    }
    catch( e : RuntimeException ) {
      while( true ) {}
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
    }
  }

  function f2_1_012() : int {
    try {
      throw new RuntimeException()
    }
    catch( e : RuntimeException ) {
      while( true ) {}
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
    }
  }

  function f3_1_012() : int {
    try {
      assert 1 == 2
    }
    catch( e : RuntimeException ) {
      while( true ) {}
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
    }
  }  //## issuekeys: MSG_MISSING_RETURN

  function f4_1_012() : int {
    try {
      while( true ) {
      }
    }
    catch( e : RuntimeException ) {
      while( true ) {}
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
    }
  }

  function f5_1_012() : int {
    try {
      while( true ) {
        break
      }
    }
    catch( e : RuntimeException ) {
      while( true ) {}
    }
    catch( e : Exception ) {
      return 1
    }
    finally {
    }
  }  //## issuekeys: MSG_MISSING_RETURN
}