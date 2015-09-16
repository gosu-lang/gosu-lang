package gw.specification.statements.theUsingStatement

uses gw.BaseVerifyErrantTest
uses java.io.Closeable
uses java.util.concurrent.locks.Lock

class TheUsingStatementTest extends BaseVerifyErrantTest {

  function testErrant_TheUsingStatementTest() {
    processErrantType(Errant_TheUsingStatementTest)
  }

  function testUsingBasic() {
    var x : List<Integer> = new ArrayList<Integer>();
    var closeMe : Closeable = \ -> { x.add(2) }
    var notFinal : Closeable = null
     using(closeMe) {
       x.add(1)
       notFinal = null
     } finally {
       x.add(3)
     }
    assertTrue({1,2,3}.equals(x))
    x.clear();
    using(var closeMe2 : Closeable = \ -> { x.add(2) }) {
      //closeMe2 = null
      x.add(1)
    } finally {
      x.add(3)
    }
    assertTrue({1,2,3}.equals(x))
    x.clear();
    using(new Object() { function  close() { x.add(2)}}) {
      x.add(1)
    } finally {
      x.add(3)
    }
    assertTrue({1,2,3}.equals(x))
    x.clear();
    var myReentrant = new IReentrant() {
      override function enter() { x.add(0);  }
      override function exit() { x.add(2); }
    }
    using(myReentrant) {
      x.add(1)
    } finally {
      x.add(3)
    }
    assertTrue({0,1,2,3}.equals(x))
    /*
    using(var foo : Closeable )  {}
    using(var foo : Closeable = null) print("bug")
    using(var foo : Closeable = null) finally {}
    using(var foo : Runnable = null)  {}   */
  }

  function  testTypesUsingStatement() {
     using(var x1 : Lock = null,
           var x2 : Closeable = null,
           var x3 : IReentrant = null,
           var x4 : IDisposable = null)  {}
    using(new IMonitorLock())  {}
    using(var x1 = new Object() { function  dispose() {}},
          var x2 = new Object() { function  close() {}},
          var x3 = new Object() { function  lock() {} function  unlock(){}})  {}
  }
}