package gw.specification.statements.theUsingStatement

uses java.io.Closeable
uses java.util.concurrent.locks.Lock

class Errant_TheUsingStatementTest {

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
    x.clear();
    using(var closeMe2 : Closeable = \ -> { x.add(2) }) {
      closeMe2 = null  //## issuekeys: MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR
      x.add(1)
    } finally {
      x.add(3)
    }
    x.clear();
    using(new Object() { function  close() { x.add(2)}}) {
      x.add(1)
    } finally {
      x.add(3)
    }
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
    using(var foo : Closeable )  {}  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    using(var foo : Closeable = null) print("bug")  //## issuekeys: MSG_EXPECTING_LEFTBRACE_STMTBLOCK
    using(var foo : Closeable = null) finally {}  //## issuekeys: MSG_EXPECTING_LEFTBRACE_STMTBLOCK
    using(var foo : Runnable = null)  {}    //## issuekeys: MSG_BAD_TYPE_FOR_USING_STMT
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
